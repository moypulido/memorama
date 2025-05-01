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
        return (size <= 4) ? 2 : (size <= 6) ? 3 : 4;
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

        int numPairs;
        switch (size) {
            case 4: numPairs = 2; break;
            case 6: numPairs = 3; break;
            case 8: numPairs = 4; break;
            default:
                throw new IllegalArgumentException("Unsupported game size: " + size);
        }

        for (int i = 0; i < numPairs; i++) {
            int resId = imageResIds.get(i % imageResIds.size());
            cardList.add(new Card(i, resId));
            cardList.add(new Card(i, resId)); // matching pair
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
                break;
            case "fruits":
                allImages.add(R.drawable.fruit_1);
                allImages.add(R.drawable.fruit_2);
                allImages.add(R.drawable.fruit_3);
                allImages.add(R.drawable.fruit_4);
                break;
            case "emojis":
                allImages.add(R.drawable.emoji_1);
                allImages.add(R.drawable.emoji_2);
                allImages.add(R.drawable.emoji_3);
                allImages.add(R.drawable.emoji_4);
                break;
        }

        int numPairs = size / 2;
        return allImages.subList(0, Math.min(numPairs, allImages.size()));
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
