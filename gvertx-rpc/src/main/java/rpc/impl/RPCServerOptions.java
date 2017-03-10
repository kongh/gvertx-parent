package rpc.impl;


import io.vertx.core.Vertx;
import io.vertx.core.shareddata.LocalMap;

/**
 *
 */
public class RPCServerOptions {

  private Vertx vertx;
  private String busAddress;
  private int maxBufferedMessages;
  LocalMap<String, SharedWrapper> serviceMapping;
  private static final String SERVICE_MAP_NAME = "VERTX_RPC_SERVICE";

  public RPCServerOptions(Vertx vertx) {
    this.vertx = vertx;
    this.serviceMapping = vertx.sharedData().getLocalMap(SERVICE_MAP_NAME);
  }

  public RPCServerOptions(Vertx vertx, RPCServerOptions other) {
    this.vertx = vertx;
    this.busAddress = other.getBusAddress();
    this.serviceMapping = vertx.sharedData().getLocalMap(SERVICE_MAP_NAME);
  }

  LocalMap<String, SharedWrapper> getServiceMapping() {
    return serviceMapping;
  }

  public RPCServerOptions addService(Object service) {
    serviceMapping.put(service.getClass().getInterfaces()[0].getCanonicalName(), new SharedWrapper<>(service));
    return this;
  }

  public int getMaxBufferedMessages() {
    return maxBufferedMessages;
  }

  public RPCServerOptions setMaxBufferedMessages(int maxBufferedMessages) {
    this.maxBufferedMessages = maxBufferedMessages;
    return this;
  }

  public Vertx getVertx() {
    return vertx;
  }

  public RPCServerOptions setBusAddress(String busAddress) {
    this.busAddress = busAddress;
    return this;
  }

  public String getBusAddress() {
    return busAddress;
  }

}
