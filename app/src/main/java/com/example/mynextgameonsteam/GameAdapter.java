package com.example.mynextgameonsteam;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private Context context;
    private List<Game> games;

    public GameAdapter(Context context) {
        this.context = context;
        this.games = new ArrayList<>();
    }

    public void addGame(Game game) {
        games.add(game);
        notifyDataSetChanged(); // Powiadom adapter o zmianach
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = games.get(position);
        holder.bind(game);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {

        private TextView gameNameTextView;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameNameTextView = itemView.findViewById(R.id.gameNameTextView);
        }

        public void bind(Game game) {
            gameNameTextView.setText(game.getName());
        }
    }
}