package com.drillmap.service;

/**
 * Created by hmohamed on 8/11/14.
 */
public class Person {
    String firstName;
    String lastName;

    public Person() {}

    public Person(String fn, String ln) {
        firstName = fn;
        lastName = ln;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
