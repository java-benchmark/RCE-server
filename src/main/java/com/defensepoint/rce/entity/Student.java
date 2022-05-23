package com.defensepoint.rce.entity;

import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private Department departmentName;

    public Student() {
    }

    public Student(String name, Department departmentName) {
        this.name = name;
        this.departmentName = departmentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(Department departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", departmentName=" + departmentName +
                '}';
    }
}