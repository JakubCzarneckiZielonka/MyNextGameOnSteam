package com.example.mynextgameonsteam;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gameAdapter = new GameAdapter(this);
        recyclerView.setAdapter(gameAdapter);

        // Load games from the database
        new LoadGamesTask(this).execute();

        WebScrapingSteamTask webScrapingSteamTask = new WebScrapingSteamTask(this);
        webScrapingSteamTask.execute();
    }

    public void updateUI(GameEntity game) {
        gameAdapter.addGame(game);
    }
}
