package com.example.memorama.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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

        // Obtener datos del intent
        int score = getIntent().getIntExtra("score", 0);
        String category = getIntent().getStringExtra("category");
        int size = getIntent().getIntExtra("size", 0);
        long timeMillis = getIntent().getLongExtra("time", 0);

        // Formatear tiempo a segundos
        int seconds = (int) (timeMillis / 1000);

        // Mostrar datos
        binding.textScore.setText("Punctuation: " + score);
        binding.textCategory.setText("Categoría: " + category);
        binding.textSize.setText("Tamaño: " + size + "x" + size);
        binding.textTime.setText("Tiempo: " + seconds + " segundos");

        // Volver al menú
        binding.buttonBackToMenu.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuActivity.class));
            finish();
        });
    }
}
