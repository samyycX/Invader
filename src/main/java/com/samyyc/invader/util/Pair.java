package com.samyyc.invader.util;

public class Pair <K,V> {

    private K k;
    private V v;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K K() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public V V() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }

    public static <K,V> Pair<K,V> of(K k, V v) {
        return new Pair<K,V>(k,v);
    }
}
