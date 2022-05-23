package com.defensepoint.rce.entity;

import java.io.Serializable;
import java.util.List;

public class People implements Serializable {

    private final List<String> names;

    public People(List<String> names) {
        this.names = names;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String name : names) {
            sb.append(name);
            sb.append("\n");
        }

        return sb.toString();
    }
}