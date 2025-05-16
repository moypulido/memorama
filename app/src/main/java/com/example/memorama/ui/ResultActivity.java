package com.example.memorama.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memorama.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {
    private ActivityResultBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int player1Score = getIntent().getIntExtra("player1Score", 0);
        int player2Score = getIntent().getIntExtra("player2Score", 0);
        String player1Name = getIntent().getStringExtra("player1Name");
        String player2Name = getIntent().getStringExtra("player2Name");

        guardarMejorPuntaje(player1Name, player1Score);
        guardarMejorPuntaje(player2Name, player2Score);

        int best1 = obtenerMejorPuntaje(player1Name);
        int best2 = obtenerMejorPuntaje(player2Name);

        binding.textScore.setText(player1Name + ": " + player1Score + "\n" +
                player2Name + ": " + player2Score);

        binding.textHighScore.setText("Best scores:\n" + player1Name + ": " + best1 + "\n" + player2Name + ": " + best2);

        // Mostrar ganador
        String winner;
        if (player1Score > player2Score) {
            winner = player1Name + " wins!";
        } else if (player2Score > player1Score) {
            winner = player2Name + " wins!";
        } else {
            winner = "It's a tie!";
        }

        binding.textPlayer.setText("Winner: " + winner);

        binding.buttonBackToMenu.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuActivity.class));
            finish();
        });
    }

    public void guardarMejorPuntaje(String username, int nuevoPuntaje) {
        SharedPreferences prefs = getSharedPreferences("MemoramaPrefs", MODE_PRIVATE);
        String key = "highScore_" + username;

        int puntajeGuardado = prefs.getInt(key, 0);
        if (nuevoPuntaje > puntajeGuardado) {
            prefs.edit().putInt(key, nuevoPuntaje).apply();
        }
    }

    public int obtenerMejorPuntaje(String username) {
        SharedPreferences prefs = getSharedPreferences("MemoramaPrefs", MODE_PRIVATE);
        String key = "highScore_" + username;

        return prefs.getInt(key, 0);
    }


}
