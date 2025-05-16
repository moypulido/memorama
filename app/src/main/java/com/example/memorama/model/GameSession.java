package com.example.memorama.model;

import android.content.res.Resources;

import com.example.memorama.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameSession {
    private final Resources resources;
    private long startTime;
    private long lastMatchTime;
    private int score;
    private int size;
    private String category;
    private List<Card> cards;


    public GameSession(String category, int size, Resources resources) {
        this.score = 0;
        this.startTime = System.currentTimeMillis();
        this.lastMatchTime = this.startTime;
        this.size = size;
        this.category = category;
        this.resources = resources;
        this.cards = generateShuffledCards();
    }
    public int getScore() {
        return score;
    }
    public void decrementScore() {
        this.score = Math.max(0, this.score - 3);
    }
    public void incrementScore() {
        long currentTime = System.currentTimeMillis();
        long elapsed = (currentTime - lastMatchTime) / 1000; //
        lastMatchTime = currentTime;

        int bonus;
        if (elapsed < 2) {
            bonus = 10;
        } else if (elapsed < 5) {
            bonus = 7;
        } else if (elapsed < 10) {
            bonus = 5;
        } else {
            bonus = 3;
        }

        this.score += bonus;
    }
    public int getCardsSize() {
        int columns = getColumsSize();
        int padding = 32;
        int screenWidth = resources.getDisplayMetrics().widthPixels;
        return screenWidth / columns - padding;
    }
    public int getColumsSize() {
        int col = 0;
        switch (size) {
            case 4: col = 2; break;
            case 6: col = 3; break;
            case 8: col = 4; break;
            case 10: col = 5; break;
            case 12: col = 3; break;
            case 14: col = 2; break;
            case 16: col = 4; break;
            default:
                throw new IllegalArgumentException("Unsupported game size: " + size);
        }
        return col;
    }
    public int getRowsSize() {
        int columns = getColumsSize();
        return (size + columns - 1) / columns;
    }
    public List<Card> getCards() {
        return cards;
    }
    private List<Card> generateShuffledCards() {
        List<Integer> imageResIds = getImageListByCategory();
        List<Card> cardList = new ArrayList<>();
        Collections.shuffle(imageResIds);

        int numPairs = size / 2;

        for (int i = 0; i < numPairs; i++) {

            int resId = imageResIds.get(i);
            cardList.add(new Card(i, resId));
            cardList.add(new Card(i, resId));
        }

        Collections.shuffle(cardList);
        return cardList;
    }
    private List<Integer> getImageListByCategory() {
        List<Integer> allImages = new ArrayList<>();

        switch (category.toLowerCase()) {
            case "animals":
                allImages.add(R.drawable.animal_1);
                allImages.add(R.drawable.animal_2);
                allImages.add(R.drawable.animal_3);
                allImages.add(R.drawable.animal_4);
                allImages.add(R.drawable.animal_5);
                allImages.add(R.drawable.animal_6);
                allImages.add(R.drawable.animal_7);
                allImages.add(R.drawable.animal_8);
                allImages.add(R.drawable.animal_9);
                allImages.add(R.drawable.animal_10);
                break;
            case "fruits":
                allImages.add(R.drawable.fruit_1);
                allImages.add(R.drawable.fruit_2);
                allImages.add(R.drawable.fruit_3);
                allImages.add(R.drawable.fruit_4);
                allImages.add(R.drawable.fruit_5);
                allImages.add(R.drawable.fruit_6);
                allImages.add(R.drawable.fruit_7);
                allImages.add(R.drawable.fruit_8);
                allImages.add(R.drawable.fruit_9);
                allImages.add(R.drawable.fruit_10);
                break;
            case "emojis":
                allImages.add(R.drawable.emoji_1);
                allImages.add(R.drawable.emoji_2);
                allImages.add(R.drawable.emoji_3);
                allImages.add(R.drawable.emoji_4);
                allImages.add(R.drawable.emoji_5);
                allImages.add(R.drawable.emoji_6);
                allImages.add(R.drawable.emoji_7);
                allImages.add(R.drawable.emoji_8);
                allImages.add(R.drawable.emoji_9);
                allImages.add(R.drawable.emoji_10);
                break;
        }

        int numPairs = size / 2;
        return allImages;
    }
    public long getTimeElapsed() {
        return System.currentTimeMillis() - startTime;
    }
    public boolean isGameOver() {
        for (Card card : cards) {
            if (!card.isMatched()) {
                return false;
            }
        }
        return true;
    }
    public String getCategory() {
        return category;
    }
    public int getSize() {
        return size;
    }
}
