package com.example.mynextgameonsteam;



import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SteamApi {
    @GET("IPlayerService/GetOwnedGames/v0001/")
    Call<JsonObject> getOwnedGames(
            @Query("key") String apiKey,
            @Query("steamid") String steamId,
            @Query("format") String format
    );

    @GET("api/appdetails/")
    Call<JsonObject> getAppDetails(
            @Query("appids") int appId
    );
}
