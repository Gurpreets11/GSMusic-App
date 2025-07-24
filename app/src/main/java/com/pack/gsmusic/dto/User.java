package com.pack.gsmusic.dto;

public class User {

    public String name;
    public String email;
    public int age;

    public User() {
        // Required for Firebase
    }

    public User(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
