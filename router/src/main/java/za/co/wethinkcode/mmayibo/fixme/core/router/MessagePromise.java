package za.co.wethinkcode.mmayibo.fixme.core.router;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MessagePromise implements Promise<String> {
    @Override
    public Promise<String> setSuccess(String s) {
        return null;
    }

    @Override
    public boolean trySuccess(String s) {
        return false;
    }

    @Override
    public Promise<String> setFailure(Throwable throwable) {
        return null;
    }

    @Override
    public boolean tryFailure(Throwable throwable) {
        return false;
    }

    @Override
    public boolean setUncancellable() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return false;
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
    public Promise<String> addListener(GenericFutureListener<? extends Future<? super String>> genericFutureListener) {
        return null;
    }

    @Override
    public Promise<String> addListeners(GenericFutureListener<? extends Future<? super String>>... genericFutureListeners) {
        return null;
    }

    @Override
    public Promise<String> removeListener(GenericFutureListener<? extends Future<? super String>> genericFutureListener) {
        return null;
    }

    @Override
    public Promise<String> removeListeners(GenericFutureListener<? extends Future<? super String>>... genericFutureListeners) {
        return null;
    }

    @Override
    public Promise<String> await() throws InterruptedException {
        return null;
    }

    @Override
    public Promise<String> awaitUninterruptibly() {
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
    public String getNow() {
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

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public String get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public Promise<String> sync() throws InterruptedException {
        return null;
    }

    @Override
    public Promise<String> syncUninterruptibly() {
        return null;
    }
}
