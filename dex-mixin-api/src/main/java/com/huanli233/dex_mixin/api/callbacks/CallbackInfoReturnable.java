package com.huanli233.dex_mixin.api.callbacks;

import org.jetbrains.annotations.NotNull;

public class CallbackInfoReturnable<R> extends CallbackInfo {

    private R returnValue;

    public CallbackInfoReturnable(@NotNull String name, boolean cancellable) {
        super(name, cancellable);
        this.returnValue = null;
    }

    public CallbackInfoReturnable(@NotNull String name, boolean cancellable, R returnValue) {
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
