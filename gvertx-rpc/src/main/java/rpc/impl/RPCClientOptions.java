package rpc.impl;

import io.vertx.core.Vertx;
import rpc.RPCHook;

/**
 * Created by stream.
 */
public class RPCClientOptions<T> {

  private Vertx vertx;
  private String busAddress;
  private Class<T> serviceClass;
  private boolean isHookOnEventLoop = true;
  private long timeout = 10 * 1000L;
  private RPCHook rpcHook;

  public RPCClientOptions(Vertx vertx) {
    this.vertx = vertx;
  }

  public RPCClientOptions(Vertx vertx, RPCHook rpcHook) {
    this.vertx = vertx;
    this.rpcHook = rpcHook;
  }

  public RPCClientOptions(Vertx vertx, RPCClientOptions<T> other) {
    this.vertx = vertx;
    this.busAddress = other.getBusAddress();
    this.timeout = other.getTimeout();
    this.serviceClass = other.getServiceClass();
    this.rpcHook = other.getRpcHook();
    this.isHookOnEventLoop = other.isHookOnEventLoop;
  }

  public RPCClientOptions<T> setBusAddress(String busAddress) {
    this.busAddress = busAddress;
    return this;
  }

  public RPCClientOptions<T> setTimeout(long timeout) {
    this.timeout = timeout;
    return this;
  }

  public RPCClientOptions<T> setServiceClass(Class<T> serviceClass) {
    this.serviceClass = serviceClass;
    return this;
  }

  public RPCClientOptions<T> setHookOnEventLoop(boolean hookOnEventLoop) {
    isHookOnEventLoop = hookOnEventLoop;
    return this;
  }

  public boolean isHookOnEventLoop() {
    return isHookOnEventLoop;
  }

  public RPCHook getRpcHook() {
    return rpcHook;
  }

  public RPCClientOptions<T> setRpcHook(RPCHook rpcHook) {
    this.rpcHook = rpcHook;
    return this;
  }

  public Vertx getVertx() {
    return vertx;
  }

  public String getBusAddress() {
    return busAddress;
  }

  public long getTimeout() {
    return timeout;
  }

  public Class<T> getServiceClass() {
    return serviceClass;
  }

}
