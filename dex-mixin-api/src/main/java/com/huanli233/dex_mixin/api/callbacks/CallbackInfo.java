package com.huanli233.dex_mixin.api.callbacks;

public class CallbackInfo implements Cancellable {

    private final String name;

    private final boolean cancellable;

    private boolean cancelled;

    public CallbackInfo(String name, boolean cancellable) {
        this.name = name;
        this.cancellable = cancellable;
    }

    public String getMethodName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("CallbackInfo[TYPE=%s,NAME=%s,CANCELLABLE=%s]", this.getClass().getSimpleName(), this.name, this.cancellable);
    }

    @Override
    public final boolean isCancellable() {
        return this.cancellable;
    }

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void cancel() {
        this.cancelled = true;
    }

}
