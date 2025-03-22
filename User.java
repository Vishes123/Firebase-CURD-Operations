package com.example.myapplication;

public class User {
    public String name;
    public String email;
    public String city;
    public String number;
    public String Password;
    public String confirmPassword;

    public User() {}

//    public User(String name, String email, String city, String number, String password, String confirmPassword) {
//        this.name = name;
//        this.email = email;
//        this.city = city;
//        this.number = number;
//       this.Password = password;
//        this.confirmPassword = confirmPassword;
//    }

    public User(String name, String email, String city, String number) {
        this.name = name;
        this.email = email;
        this.city = city;
        this.number = number;
    }
}

