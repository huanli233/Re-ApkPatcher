package com.huanli233.dex_mixin.api.callbacks;

import org.jetbrains.annotations.NotNull;

public class ArgsModifier {

    protected final Object[] values;

    protected ArgsModifier(int argsCount) {
        this.values = new Object[argsCount];
    }

    public int size() {
        return this.values.length;
    }

    public <T> void set(int index, T value) {
        values[index] = value;
    }

    public void setAll(@NotNull Object... values) {
        System.arraycopy(values, 0, this.values, 0, values.length);
    }

}
