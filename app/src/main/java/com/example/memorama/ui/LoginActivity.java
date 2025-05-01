package com.example.memorama.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.memorama.databinding.ActivityLoginBinding;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private final Map<String, String> users = Map.of(
            "Player1", "123456",
            "Player2", "abcdef"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonLogin.setOnClickListener(view -> {
            String username = binding.editTextUsername.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();

            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
//            if (users.containsKey(username) &&  users.get(username).equals(password)) {
//            } else {
//                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
//            }
        });
    }
}
