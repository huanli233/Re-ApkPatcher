package com.huanli233.dex_mixin.api.annotations.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.CLASS)
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

    Pos value();

    String target() default "";

    Shift shift() default Shift.NONE;

    Opcode opcode() default Opcode.ANY;

}
