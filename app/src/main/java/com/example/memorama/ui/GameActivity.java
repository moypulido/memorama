package com.example.memorama.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.memorama.databinding.ActivityGameBinding;
import com.example.memorama.model.GameSession;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.GridLayout;
import android.view.ViewGroup;
import com.example.memorama.R;
import com.example.memorama.model.Card;



public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;
    private Card CardOne;
    private Card Cardtwo;
    private GameSession gameSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonRestart.setOnClickListener(v -> restartGame());
        binding.buttonBackToMenu.setOnClickListener(v -> goToMenu());

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
        binding.scoreTextView.setText("Score: " + gameSession.getScore());
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

            gameSession.incrementScore();
            updateScoreDisplay();

            CardOne = null;
            Cardtwo = null;
        } else {
            Card finalCardOne = CardOne;
            Card finalCardTwo = Cardtwo;

            gameSession.decrementScore();
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
            }, 1000);
        }

        if (gameSession.isGameOver()) {


            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("score", gameSession.getScore());
            intent.putExtra("category", gameSession.getCategory());
            intent.putExtra("size", gameSession.getSize());
            intent.putExtra("time", gameSession.getTimeElapsed());
            startActivity(intent);
            finish();
        }

    }


}
