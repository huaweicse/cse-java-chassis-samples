package com.huawei.cse.porter.gateway;

import java.util.Map;

import org.apache.servicecomb.core.Handler;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.core.NonSwaggerInvocation;
import org.apache.servicecomb.edge.core.AbstractEdgeDispatcher;
import org.apache.servicecomb.foundation.common.net.URIEndpointObject;
import org.apache.servicecomb.foundation.vertx.VertxUtils;
import org.apache.servicecomb.loadbalance.LoadbalanceHandler;
import org.apache.servicecomb.loadbalance.filter.IsolationDiscoveryFilter;
import org.apache.servicecomb.loadbalance.filter.ServerDiscoveryFilter;
import org.apache.servicecomb.serviceregistry.RegistryUtils;
import org.apache.servicecomb.serviceregistry.discovery.DiscoveryTree;
import org.apache.servicecomb.swagger.invocation.AsyncResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.impl.headers.VertxHttpHeaders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class UiDispatcher extends AbstractEdgeDispatcher {
  private static Logger LOGGER = LoggerFactory.getLogger(UiDispatcher.class);

  private static Vertx vertx = VertxUtils.getOrCreateVertxByName("transport", null);

  private static HttpClient httpClient = vertx.createHttpClient(new HttpClientOptions());

  private DiscoveryTree discoveryTree = new DiscoveryTree();

  private LoadbalanceHandler loadbalanceHandler;

  class RetriableHandler implements Handler {
    private RoutingContext context;

    private String path;

    private Buffer data;

    private boolean isRetry = false;

    public RetriableHandler(RoutingContext context, String path) {
      this.context = context;
      this.path = path;
      this.context.response().setChunked(true);
    }

    @Override
    public void handle(Invocation invocation, AsyncResponse asyncResponse) throws Exception {
      URIEndpointObject endpoint = (URIEndpointObject) invocation.getEndpoint().getAddress();
      // 使用HttpClient转发请求
      HttpClientRequest clietRequest =
          httpClient.request(context.request().method(),
              endpoint.getPort(),
              endpoint.getHostOrIp(),
              "/" + path,
              clientResponse -> {
                context.response().setStatusCode(clientResponse.statusCode());
                VertxHttpHeaders headers = new VertxHttpHeaders();
                clientResponse.headers().forEach(entry -> {
                  if (!"Content-Length".equalsIgnoreCase(entry.getKey())
                      && !"Transfer-Encoding".equalsIgnoreCase(entry.getKey())) {
                    headers.add(entry.getKey(), entry.getValue());
                  }
                });
                context.response().headers().setAll(headers);
                clientResponse.handler(data -> {
                  context.response().write(data);
                });
                clientResponse.endHandler((v) -> {
                  context.response().end();
                  asyncResponse.success("OK");
                });
              });
      clietRequest.headers().setAll(context.request().headers());
      clietRequest.exceptionHandler(e -> {
        asyncResponse.consumerFail(e);
      });

      if (!isRetry) {
        // data can not be read twice, so cache it in retry 
        this.context.request().handler(d -> {
          clietRequest.write(d);
          data = d;
        });

        context.request().endHandler((v) -> {
          clietRequest.end();
        });

        isRetry = true;
      } else {
        if (data != null) {
          clietRequest.write(data);
        }
        clietRequest.end();
      }
    }
  }

  public UiDispatcher() {
    discoveryTree.addFilter(new IsolationDiscoveryFilter());
    discoveryTree.addFilter(new ServerDiscoveryFilter());
    discoveryTree.sort();
    loadbalanceHandler = new LoadbalanceHandler(discoveryTree);
  }

  @Override
  public int getOrder() {
    return 10001;
  }

  @Override
  public void init(Router router) {
    String regex = "/ui/([^\\/]+)/(.*)";
    router.routeWithRegex(regex).failureHandler(this::onFailure).handler(this::onRequest);
  }

  protected void onRequest(RoutingContext context) {
    Map<String, String> pathParams = context.pathParams();

    String microserviceName = pathParams.get("param0");
    String path = "/" + pathParams.get("param1");

    Invocation invocation =
        new NonSwaggerInvocation(RegistryUtils.getAppId(), microserviceName, "0+", new RetriableHandler(context, path));
    try {
      loadbalanceHandler.handle(invocation, resp -> {
        if (resp.isFailed()) {
          context.response().setStatusCode(resp.getStatusCode());
          context.response().write(((Exception) resp.getResult()).getMessage());
          context.response().end();
        }
      });
    } catch (Exception e) {
      LOGGER.error("", e);
    }
  }
}
