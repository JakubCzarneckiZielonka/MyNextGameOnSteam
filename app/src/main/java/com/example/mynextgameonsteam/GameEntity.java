package com.example.mynextgameonsteam;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "games")
public class GameEntity {
    @PrimaryKey
    private int appId;
    private String name;

    public GameEntity(int appId, String name) {
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