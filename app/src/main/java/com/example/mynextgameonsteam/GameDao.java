package com.example.mynextgameonsteam;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameEntity game);

    @Query("SELECT * FROM games")
    List<GameEntity> getAllGames();
}