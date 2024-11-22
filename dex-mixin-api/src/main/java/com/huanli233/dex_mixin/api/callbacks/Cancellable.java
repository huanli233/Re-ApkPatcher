package com.huanli233.dex_mixin.api.callbacks;

public interface Cancellable {

    /**
     * Get whether this is actually cancellable
     *
     * @return whether this is actually cancellable
     */
    boolean isCancellable();

    /**
     * Get whether this is cancelled
     *
     * @return whether this is cancelled
     */
    boolean isCancelled();

    void cancel();

}
