package com.grape.cloud.model;

import java.util.List;

public class Fizzle {
    private List<String> foo;
    /**
     * {
     *     "foo": [
     *         "abc",
     *         "one",
     *         "two",
     *         "three"
     *     ],
     *     "bar": "true",
     *     "baz": "1"
     * }
     */
    private boolean bar;
    private int baz;

    public List<String> getFoo() {
        return foo;
    }

    public boolean isBar() {
        return bar;
    }

    public int getBaz() {
        return baz;
    }



    public void setFoo(List<String> foo) {
        this.foo = foo;
    }

    public void setBar(boolean bar) {
        this.bar = bar;
    }

    public void setBaz(int baz) {
        this.baz = baz;
    }
}
