package za.co.wethinkcode.mmayibo.fixme.core;


import java.util.concurrent.*;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.thavam.util.concurrent.blockingMap.BlockingHashMap;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;

public class ResponseFuture implements Future<FixMessage> {

    BlockingHashMap<String, FixMessage> blockingHashMap = new BlockingHashMap<>();

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public FixMessage get() throws InterruptedException, ExecutionException {
        return null;
    }


    public FixMessage get(String key) throws InterruptedException {
        FixMessage aux = blockingHashMap.take(key);
        blockingHashMap.put(key, aux);
        return aux;
    }

    @Override
    public FixMessage get(long timeout, TimeUnit unit) {
        return null;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public Throwable cause() {
        return null;
    }

    @Override
    public Future<FixMessage> addListener(GenericFutureListener<? extends Future<? super FixMessage>> genericFutureListener) {
        return null;
    }

    @Override
    public Future<FixMessage> addListeners(GenericFutureListener<? extends Future<? super FixMessage>>... genericFutureListeners) {
        return null;
    }

    @Override
    public Future<FixMessage> removeListener(GenericFutureListener<? extends Future<? super FixMessage>> genericFutureListener) {
        return null;
    }

    @Override
    public Future<FixMessage> removeListeners(GenericFutureListener<? extends Future<? super FixMessage>>... genericFutureListeners) {
        return null;
    }

    @Override
    public Future<FixMessage> sync() throws InterruptedException {
        return null;
    }

    @Override
    public Future<FixMessage> syncUninterruptibly() {
        return null;
    }

    @Override
    public Future<FixMessage> await() throws InterruptedException {
        return null;
    }

    @Override
    public Future<FixMessage> awaitUninterruptibly() {
        return null;
    }

    @Override
    public boolean await(long l, TimeUnit timeUnit) throws InterruptedException {
        return false;
    }

    @Override
    public boolean await(long l) throws InterruptedException {
        return false;
    }

    @Override
    public boolean awaitUninterruptibly(long l, TimeUnit timeUnit) {
        return false;
    }

    @Override
    public boolean awaitUninterruptibly(long l) {
        return false;
    }

    @Override
    public FixMessage getNow() {
        return null;
    }

    @Override
    public boolean cancel(boolean b) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }


    public void set(String key, FixMessage msg) {
        blockingHashMap.put(key, msg);
    }

}
