package com.huanli233.dex_mixin.api.callbacks;

public class CallbackInfoReturnable<R> extends CallbackInfo {

    private R returnValue;

    public CallbackInfoReturnable(String name, boolean cancellable) {
        super(name, cancellable);
        this.returnValue = null;
    }

    public CallbackInfoReturnable(String name, boolean cancellable, R returnValue) {
        super(name, cancellable);
        this.returnValue = returnValue;
    }

    public void setReturnValue(R returnValue)  {
        super.cancel();

        this.returnValue = returnValue;
    }

    public R getReturnValue() {
        return this.returnValue;
    }

}
