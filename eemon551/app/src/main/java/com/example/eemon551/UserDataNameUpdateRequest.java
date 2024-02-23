package com.example.eemon551;


public class UserDataNameUpdateRequest {
    private String name;
    private int level;
    private int money;

    public UserDataNameUpdateRequest(String name, int level, int money) {
        this.name = name;
        this.level = level;
        this.money = money;
    }

    // Getter and Setter
}