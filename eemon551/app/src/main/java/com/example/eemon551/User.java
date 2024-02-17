package com.example.eemon551;

public class User {
    private int id;
    private String name;
    private int level;
    private int money;

    public User(String name, int level, int money) {
        this.name = name;
        this.level = level;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public int getLevel(){
        return level;
    }

    public int getMoney(){
        return money;
    }
}
