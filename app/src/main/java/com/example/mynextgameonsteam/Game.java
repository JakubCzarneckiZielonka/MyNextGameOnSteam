package com.example.mynextgameonsteam;

public class Game {

    private int appId;
    private String name;

    public Game(int appId, String name) {
        this.appId = appId;
        this.name = name;
    }

    public int getAppId() {
        return appId;
    }

    public String getName() {
        return name;
    }
}