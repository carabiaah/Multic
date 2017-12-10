package com.watchthisspace.multic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.watchthisspace.multic.adapters.ScoreboardAdapter;
import com.watchthisspace.multic.objects.Player;
import com.watchthisspace.multic.renderer.GLView;
import com.watchthisspace.multic.utils.GameManager;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity {

    private GameManager mGameManager;

    private GLView mGLView;

    private ListView mScoreboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);

        // Debug
        // Set up player list
        List<Player> players = new ArrayList<>();
        players.add(new Player((byte) 0, "Bwehmeister"));
        players.add(new Player((byte) 1, "Bob"));
        players.add(new Player((byte) 2, "Billybobthornton"));
        players.add(new Player((byte) 3, "Snotmastersnot"));

        mGameManager = new GameManager(players);

        mGLView = findViewById(R.id.play_gv_game);

        mGLView.init(mGameManager);

        mScoreboard = findViewById(R.id.play_lv_scoreboard_container);
        mScoreboard.setAdapter(new ScoreboardAdapter(this, players));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }
}
