package com.huanli233.dex_mixin.api.annotations.injection;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface At {

    enum Pos {
        HEAD, RETURN, TAIL, INVOKE, INVOKE_ASSIGN, FIELD
    }

    enum Shift {

        /**
         * Do not shift the returned opcodes
         */
        NONE,

        /**
         * Shift the returned opcodes back one instruction
         */
        BEFORE,

        /**
         * Shift the returned opcodes forward one instruction
         */
        AFTER

    }

    enum Opcode {
        ANY, GET_VALUE, PUT_VALUE
    }

    @NotNull
    Pos value();

    @NotNull
    String target() default "";

    @NotNull
    Shift shift() default Shift.NONE;

    @NotNull
    Opcode opcode() default Opcode.ANY;

}
