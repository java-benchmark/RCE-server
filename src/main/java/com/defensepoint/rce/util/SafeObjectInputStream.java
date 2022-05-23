package com.defensepoint.rce.util;

import java.io.*;
import java.util.Set;

public class SafeObjectInputStream extends ObjectInputStream {
    public Set whitelist;

    public SafeObjectInputStream(InputStream inputStream, Set whitelist) throws IOException {
        super(inputStream);
        this.whitelist = whitelist;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass cls)
            throws IOException, ClassNotFoundException
    {
        if (!whitelist.contains(cls.getName())) {
            throw new InvalidClassException("Unauthorized deserialization attempt => ", cls.getName());
        }
        return super.resolveClass(cls);
    }
}
