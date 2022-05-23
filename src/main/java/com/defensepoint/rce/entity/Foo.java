package com.defensepoint.rce.entity;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "foo")
public class Foo {

    private String foo;

    public Foo() {
    }

    public Foo(String foo) {
        this.foo = foo;
    }

    @XmlElement(name = "foo")
    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    @Override
    public String toString() {
        return "Foo{" +
                "foo='" + foo + '\'' +
                '}';
    }
}
