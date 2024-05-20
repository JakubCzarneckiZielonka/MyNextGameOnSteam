package com.example.mynextgameonsteam;

import android.os.AsyncTask;
import java.util.List;

public class LoadGamesTask extends AsyncTask<Void, Void, List<GameEntity>> {
    private MainActivity activity;

    public LoadGamesTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<GameEntity> doInBackground(Void... voids) {
        AppDatabase db = AppDatabase.getInstance(activity.getApplicationContext());
        return db.gameDao().getAllGames();
    }

    @Override
    protected void onPostExecute(List<GameEntity> games) {
        for (GameEntity game : games) {
            activity.updateUI(game);
        }
    }
}
