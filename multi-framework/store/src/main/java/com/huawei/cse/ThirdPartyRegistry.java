package com.huawei.cse;

import java.util.ArrayList;
import java.util.List;

import org.apache.servicecomb.core.BootListener;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.core.NonSwaggerInvocation;
import org.apache.servicecomb.foundation.common.cache.VersionedCache;
import org.apache.servicecomb.loadbalance.ServiceCombServer;
import org.apache.servicecomb.loadbalance.filter.IsolationDiscoveryFilter;
import org.apache.servicecomb.loadbalance.filter.ServerDiscoveryFilter;
import org.apache.servicecomb.serviceregistry.RegistryUtils;
import org.apache.servicecomb.serviceregistry.api.registry.MicroserviceInstance;
import org.apache.servicecomb.serviceregistry.discovery.DiscoveryContext;
import org.apache.servicecomb.serviceregistry.discovery.DiscoveryTree;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartyRegistry implements BootListener {

  @Override
  public void onBootEvent(BootEvent event) {
    if (event.getEventType() == EventType.AFTER_REGISTRY) {
      DiscoveryTree discoveryTree = new DiscoveryTree();
      discoveryTree.addFilter(new IsolationDiscoveryFilter());
      discoveryTree.addFilter(new ServerDiscoveryFilter());
      discoveryTree.sort();

      DiscoveryContext context = new DiscoveryContext();
      Invocation invocation =
          new NonSwaggerInvocation("default", "auth", "0+", null);
      context.setInputParameters(invocation);
      VersionedCache serversVersionedCache = discoveryTree.discovery(context,
          "default",
          "auth",
          "0+");
      List<ServiceCombServer> servers = serversVersionedCache.data();
      List<MicroserviceInstance> instances = new ArrayList<>(servers.size());
      servers.forEach(item -> {
        MicroserviceInstance instance = new MicroserviceInstance();
        List<String> endpoints = item.getInstance().getEndpoints();
        List<String> endpointsWithoutPrefix = new ArrayList<>(endpoints.size());
        endpoints.forEach(e -> {
          System.out.println(e);
          endpointsWithoutPrefix.add(e.substring(0, e.indexOf("?")));
        });
        instance.setEndpoints(endpointsWithoutPrefix);
        instances.add(instance);
      });
      RegistryUtils.getServiceRegistry()
          .registerMicroserviceMapping(
              "authStub",
              "1.1.1",
              instances,
              AuthService.class);
    }
  }
}
