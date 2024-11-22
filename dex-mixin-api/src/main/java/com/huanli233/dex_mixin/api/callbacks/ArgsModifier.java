package com.huanli233.dex_mixin.api.callbacks;

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

    public void setAll(Object... values) {
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

}
