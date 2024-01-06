package com.example.mynextgameonsteam;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gameAdapter = new GameAdapter(this); // Inicjalizacja adaptera
        recyclerView.setAdapter(gameAdapter);

        WebScrapingSteamTask webScrapingSteamTask = new WebScrapingSteamTask(this);
        webScrapingSteamTask.execute();
    }

    // Metoda do aktualizacji interfejsu użytkownika po pobraniu gier
    public void updateUI(Game game) {
        gameAdapter.addGame(game); // Przekazanie pojedynczej gry do adaptera
    }

}