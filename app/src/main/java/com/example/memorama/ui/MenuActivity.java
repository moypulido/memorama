package com.example.memorama.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memorama.R;
import com.example.memorama.databinding.ActivityMenuBinding;

public class
MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategories.setAdapter(categoriesAdapter);

        String[] sizes = getResources().getStringArray(R.array.game_sizes);
        ArrayAdapter<String> sizesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizes);
        sizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGameSize.setAdapter(sizesAdapter);

        binding.spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                checkEnableButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                checkEnableButton();
            }
        });

        binding.spinnerGameSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                checkEnableButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                checkEnableButton();
            }
        });

        binding.buttonStartGame.setOnClickListener(v -> {
            String selectedCategory = binding.spinnerCategories.getSelectedItem().toString();
            String selectedSize = binding.spinnerGameSize.getSelectedItem().toString();

            boolean isCategorySelected = !selectedCategory.equals("Select Category");
            boolean isSizeSelected = !selectedSize.equals("Select Game Size");

            if (!isCategorySelected || !isSizeSelected) {
                Toast.makeText(this, "Please select both category and game size", Toast.LENGTH_SHORT).show();
                return;
            }

            startGame(selectedCategory, selectedSize);
        });
    }

    private void checkEnableButton() {
        String selectedCategory = binding.spinnerCategories.getSelectedItem().toString();
        String selectedSize = binding.spinnerGameSize.getSelectedItem().toString();

        boolean isCategorySelected = !selectedCategory.equals("Select Category");
        boolean isSizeSelected = !selectedSize.equals("Select Game Size");

        binding.buttonStartGame.setEnabled(isCategorySelected && isSizeSelected);
    }

    private void startGame(String category, String size) {
        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("size", Integer.parseInt(size));
        startActivity(intent);
    }
}
