package com.example.mynextgameonsteam;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//This class initially had a different purpose, but the name will be changed in the future.

public class WebScrapingSteamTask extends AsyncTask<Void, Game, Void> {
    // Go to this link and generate an API key -> https://steamcommunity.com/dev/apikey and insert it here "USER_API_KEY"
    private static final String USER_API_KEY = "Your API key";
    private static final String USER_STEAM_ID = "Your steam account id";
    private static final String USER_GAME_LIST_API_URL = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/";
    private static final String APP_DETAILS_API_URL = "https://store.steampowered.com/api/appdetails/";

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
        OkHttpClient client = new OkHttpClient();

        try {
            String userGameListUrl = buildSteamApiUrl(USER_GAME_LIST_API_URL);
            Response userGameListResponse = client.newCall(new Request.Builder().url(userGameListUrl).build()).execute();
            String userGameListResponseBody = userGameListResponse.body().string();

            processOwnedGamesJson(userGameListResponseBody);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildSteamApiUrl(String apiUrl) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUrl).newBuilder();
        urlBuilder.addQueryParameter("key", USER_API_KEY);
        urlBuilder.addQueryParameter("steamid", USER_STEAM_ID);
        urlBuilder.addQueryParameter("format", "json");
        return urlBuilder.build().toString();
    }

    private void processOwnedGamesJson(String responseBody) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(responseBody).getAsJsonObject();

        if (json.has("response") && json.getAsJsonObject("response").has("games")) {
            JsonArray gamesArray = json.getAsJsonObject("response").getAsJsonArray("games");
            for (JsonElement gameElement : gamesArray) {
                JsonObject gameObject = gameElement.getAsJsonObject();
                int appId = gameObject.get("appid").getAsInt();
                String gameName = getAppName(appId);
                Log.d("WebScrapingSteam", "Owned Game: " + gameName);

                Game game = new Game(appId, gameName);

                // Przekazanie wyniku do wątku interfejsu użytkownika
                publishProgress(game);
            }
        } else {
            Log.d("WebScrapingSteam", "No owned games found for the user.");
        }
    }

    private String getAppName(int appId) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(APP_DETAILS_API_URL).newBuilder();
        urlBuilder.addQueryParameter("appids", String.valueOf(appId));

        String url = urlBuilder.build().toString();

        try {
            Response response = client.newCall(new Request.Builder().url(url).build()).execute();
            String responseBody = response.body().string();

            if (responseBody != null && !responseBody.trim().isEmpty()) {
                JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();

                if (json.has(String.valueOf(appId))) {
                    JsonObject appData = json.getAsJsonObject(String.valueOf(appId));

                    if (appData != null && appData.has("data")) {
                        JsonObject data = appData.getAsJsonObject("data");

                        if (data.has("name")) {
                            return data.get("name").getAsString();
                        } else {
                            Log.d("WebScrapingSteam", "No name found for appId: " + appId);
                        }
                    } else {
                        Log.d("WebScrapingSteam", "No data found for appId: " + appId);
                    }
                } else {
                    Log.d("WebScrapingSteam", "No entry found for appId: " + appId);
                }
            } else {
                Log.d("WebScrapingSteam", "Response body is null or empty.");
            }

            return "Unknown";
        } catch (IOException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    @Override
    protected void onProgressUpdate(Game... games) {
        super.onProgressUpdate(games);

        // Pobrane gry przekazujemy do MainActivity
        MainActivity activity = activityReference.get();
        if (activity != null) {
            activity.runOnUiThread(() -> activity.updateUI(games[0])); // Aktualizacja UI z pojedynczą grą
        }
    }
}