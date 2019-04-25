package com.huawei.cse.houseapp.edge;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.servicecomb.edge.core.AbstractEdgeDispatcher;
import org.apache.servicecomb.foundation.vertx.VertxUtils;
import org.apache.servicecomb.serviceregistry.RegistryUtils;
import org.apache.servicecomb.serviceregistry.api.registry.MicroserviceInstance;
import org.apache.servicecomb.serviceregistry.cache.InstanceCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class WebPageDispatcher extends AbstractEdgeDispatcher {
    private static Logger LOGGER = LoggerFactory.getLogger(WebPageDispatcher.class);

    private Vertx vertx = VertxUtils.getOrCreateVertxByName("web-client", null);

    @Override
    public int getOrder() {
        return 12;
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

        URI uri = null;
        try {
            InstanceCache cache = RegistryUtils.getServiceRegistry()
                    .getInstanceCacheManager()
                    .getOrCreate(RegistryUtils.getAppId(), microserviceName, "0+");
            // 查找UI服务的实例。由于只是演示项目，并且UI服务不是重点，假设只部署一个实例。
            for (MicroserviceInstance instance : cache.getInstanceMap().values()) {
                uri = new URI(instance.getEndpoints().get(0));
                break;
            }
        } catch (URISyntaxException e) {
            LOGGER.error("can not find ui service address. ", e);

        }

        if (uri == null) {
            context.response().setStatusCode(404);
            context.response().end();
            return;
        }

        HttpClient client = vertx.createHttpClient(new HttpClientOptions());
        HttpClientRequest clietRequest =
            client.request(context.request().method(), uri.getPort(), uri.getHost(), "/" + path, clientResponse -> {
                context.request().response().setChunked(true);
                context.request().response().setStatusCode(clientResponse.statusCode());
                context.request().response().headers().setAll(clientResponse.headers());
                clientResponse.handler(data -> {
                    context.request().response().write(data);
                });
                clientResponse.endHandler((v) -> context.request().response().end());
            });
        clietRequest.setChunked(true);
        clietRequest.headers().setAll(context.request().headers());
        context.request().handler(data -> {
            clietRequest.write(data);
        });
        context.request().endHandler((v) -> clietRequest.end());
    }
}
