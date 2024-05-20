package com.example.mynextgameonsteam;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Response;

public class WebScrapingSteamTask extends AsyncTask<Void, GameEntity, Void> {
    private static final String USER_API_KEY = "4237F1EF052B65DDB502B7B0A020AA9A";
    private static final String USER_STEAM_ID = "76561199222400750";

    private WeakReference<MainActivity> activityReference;

    public WebScrapingSteamTask(MainActivity activity) {
        activityReference = new WeakReference<>(activity);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        fetchOwnedGames();
        return null;
    }

    private void fetchOwnedGames() {
        SteamApi apiService = ApiClient.getClient().create(SteamApi.class);
        Call<JsonObject> call = apiService.getOwnedGames(USER_API_KEY, USER_STEAM_ID, "json");

        try {
            Response<JsonObject> response = call.execute();
            if (response.isSuccessful()) {
                processOwnedGamesJson(response.body());
            } else {
                Log.e("WebScrapingSteam", "Response not successful");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processOwnedGamesJson(JsonObject json) {
        if (json.has("response") && json.getAsJsonObject("response").has("games")) {
            JsonArray gamesArray = json.getAsJsonObject("response").getAsJsonArray("games");
            for (JsonElement gameElement : gamesArray) {
                JsonObject gameObject = gameElement.getAsJsonObject();
                int appId = gameObject.get("appid").getAsInt();
                String gameName = getAppName(appId);
                Log.d("WebScrapingSteam", "Owned Game: " + gameName);

                GameEntity game = new GameEntity(appId, gameName);

                // Save game to the database
                MainActivity activity = activityReference.get();
                if (activity != null) {
                    AppDatabase db = AppDatabase.getInstance(activity.getApplicationContext());
                    db.gameDao().insert(game);

                    // Publish progress to update UI
                    publishProgress(game);
                }
            }
        } else {
            Log.d("WebScrapingSteam", "No owned games found for the user.");
        }
    }

    private String getAppName(int appId) {
        SteamApi apiService = ApiClient.getStoreClient().create(SteamApi.class);
        Call<JsonObject> call = apiService.getAppDetails(appId);

        try {
            Response<JsonObject> response = call.execute();
            if (response.isSuccessful()) {
                JsonObject json = response.body();
                if (json.has(String.valueOf(appId))) {
                    JsonObject appData = json.getAsJsonObject(String.valueOf(appId)).getAsJsonObject("data");
                    if (appData != null && appData.has("name")) {
                        return appData.get("name").getAsString();
                    } else {
                        Log.d("WebScrapingSteam", "No name found for appId: " + appId);
                    }
                } else {
                    Log.d("WebScrapingSteam", "No entry found for appId: " + appId);
                }
            } else {
                Log.e("WebScrapingSteam", "Response not successful");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    @Override
    protected void onProgressUpdate(GameEntity... games) {
        super.onProgressUpdate(games);

        MainActivity activity = activityReference.get();
        if (activity != null) {
            activity.runOnUiThread(() -> activity.updateUI(games[0]));
        }
    }
}
