package com.example.memorama.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.memorama.databinding.ActivityGameBinding;
import com.example.memorama.model.GameSession;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.GridLayout;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.memorama.R;
import com.example.memorama.model.Card;



public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;
    private Card CardOne;
    private Card Cardtwo;
    private GameSession gameSession;
    private boolean isPaused = true;
    private int currentPlayer = 1; // Jugador 1 comienza
    private int player1Score = 0;
    private int player2Score = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonRestart.setOnClickListener(v -> restartGame());
        binding.buttonBackToMenu.setOnClickListener(v -> goToMenu());
        binding.buttonPause.setOnClickListener(v -> pauseGame());

        String category = getIntent().getStringExtra("category");
        int size = getIntent().getIntExtra("size", 4);

        Log.d("GameActivity", "Category: " + category + ", Size: " + size);

        gameSession = new GameSession(category, size, getResources());

        buildGrid(gameSession);
    }

    private void restartGame() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void goToMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        finish();
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void updateScoreDisplay() {
        binding.scoreTextView.setText(
                "Player 1: " + player1Score + " | Player 2: " + player2Score +
                        " | Turn: Player " + currentPlayer
        );
    }
    private void buildGrid(GameSession session) {
        GridLayout gridLayout = binding.gridLayout;
        gridLayout.removeAllViews();

        gridLayout.setColumnCount(session.getColumsSize());
        gridLayout.setRowCount(session.getRowsSize());

        int cardSize = session.getCardsSize();

        for (Card card : session.getCards()) {
            ImageButton imageButton = new ImageButton(this);
            imageButton.setLayoutParams(new ViewGroup.LayoutParams(cardSize, cardSize));
            imageButton.setScaleType(ImageButton.ScaleType.CENTER_CROP);
            imageButton.setImageResource(R.drawable.card_back);

            imageButton.setTag(card);

            imageButton.setOnClickListener(v -> onCardClicked(imageButton));
            gridLayout.addView(imageButton);
        }
    }

    private void onCardClicked(ImageButton imageButton) {

        Card clickedCard = (Card) imageButton.getTag();

        if (clickedCard.isFaceUp() || clickedCard.isMatched() || Cardtwo != null) {
            return;
        }

        clickedCard.setFaceUp(true);
        imageButton.setImageResource(clickedCard.getImageResId());

        if (CardOne == null) {
            CardOne = clickedCard;
            return;
        }

        Cardtwo = clickedCard;

        if (CardOne.getImageResId() == Cardtwo.getImageResId()) {
            CardOne.setMatched(true);
            Cardtwo.setMatched(true);

            if (currentPlayer == 1) {
                player1Score++;
            } else {
                player2Score++;
            }

            updateScoreDisplay();

            CardOne = null;
            Cardtwo = null;

        } else {
            Card finalCardOne = CardOne;
            Card finalCardTwo = Cardtwo;

            if (currentPlayer == 1) {
                player1Score = Math.max(0, player1Score - 1);
            } else {
                player2Score = Math.max(0, player2Score - 1);
            }

            updateScoreDisplay();

            binding.gridLayout.setEnabled(false);
            new Handler().postDelayed(() -> {
                finalCardOne.setFaceUp(false);
                finalCardTwo.setFaceUp(false);

                for (int i = 0; i < binding.gridLayout.getChildCount(); i++) {
                    ImageButton button = (ImageButton) binding.gridLayout.getChildAt(i);
                    if (button.getTag() == finalCardOne || button.getTag() == finalCardTwo) {
                        button.setImageResource(R.drawable.card_back);
                    }
                }

                CardOne = null;
                Cardtwo = null;
                binding.gridLayout.setEnabled(true);

                // Cambiar de jugador si falló
                currentPlayer = (currentPlayer == 1) ? 2 : 1;
                updateScoreDisplay();

            }, 1000);
        }


        if (gameSession.isGameOver()) {

            SharedPreferences prefs = getSharedPreferences("MemoramaPrefs", MODE_PRIVATE);
            String player1Name = prefs.getString("currentUser", "Player1");
            String player2Name = player1Name.equals("Player1") ? "Player2" : "Player1";

            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("player1Score", player1Score);
            intent.putExtra("player2Score", player2Score);
            intent.putExtra("player1Name", player1Name); // O usar nombre real si lo tienes
            intent.putExtra("player2Name", player2Name);
            intent.putExtra("category", gameSession.getCategory());
            intent.putExtra("size", gameSession.getSize());
            intent.putExtra("time", gameSession.getTimeElapsed());
            startActivity(intent);
            finish();
        }

    }

    private void pauseGame() {
        isPaused = !isPaused;

        if (isPaused) {
            binding.buttonPause.setText("Resume");
            binding.pauseOverlay.setVisibility(View.VISIBLE);
            binding.buttonPause.bringToFront();
        } else {
            binding.buttonPause.setText("Pause");
            binding.pauseOverlay.setVisibility(View.GONE);
        }

        binding.buttonRestart.setEnabled(!isPaused);
        binding.buttonBackToMenu.setEnabled(!isPaused); // <- aquí

        for (int i = 0; i < binding.gridLayout.getChildCount(); i++) {
            binding.gridLayout.getChildAt(i).setEnabled(!isPaused);
        }
    }


    @Override
    public void onBackPressed() {
        if (!isPaused) {
            super.onBackPressed();
        }
    }

}
