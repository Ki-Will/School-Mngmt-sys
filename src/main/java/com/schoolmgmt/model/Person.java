package com.schoolmgmt.model;

import java.util.Objects;

public abstract class Person implements Comparable<Person>, Identifiable<String> {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String fullName;
    private String email;
    private String phoneNumber;

    protected Person(String id, String fullName, String email, String phoneNumber) {
        this.id = Objects.requireNonNull(id, "id");
        this.fullName = Objects.requireNonNull(fullName, "fullName");
        this.email = Objects.requireNonNull(email, "email");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "phoneNumber");
    }

    @Override
    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = Objects.requireNonNull(fullName, "fullName");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "email");
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "phoneNumber");
    }

    public abstract String getRole();

    @Override
    public int compareTo(Person other) {
        int nameComparison = this.fullName.compareToIgnoreCase(other.fullName);
        if (nameComparison != 0) {
            return nameComparison;
        }
        return this.id.compareTo(other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id.equals(person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, name=%s, email=%s]", getRole(), id, fullName, email);
    }
}
