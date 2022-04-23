package com.samyyc.invader.util;

public class Tuple<A, B, C> {

    private A a;
    private B b;
    private C c;

    public Tuple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }

   public static <A,B,C> Tuple<A,B,C> of(A a, B b, C c) {
        return new Tuple<A,B,C>(a,b,c);
   }

}
