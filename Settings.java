package com.example.loginform;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {

    private EditText editTextName, editTextEmail;
    private Button buttonSave;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // Inițializăm FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSave = findViewById(R.id.buttonSave);

        // Obținem emailul utilizatorului autentificat
        String userEmail = mAuth.getCurrentUser().getEmail();
        // Extragem numele din email
        String userName = extractNameFromEmail(userEmail);

        // Setăm numele și emailul în EditText-uri
        editTextName.setText(userName);
        editTextEmail.setText(userEmail);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        ImageButton backButton = findViewById(R.id.register);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MainForm.class);
                startActivity(intent);
            }
        });
    }

    // Funcția pentru extragerea numelui din email
    private String extractNameFromEmail(String email) {
        String[] parts = email.split("@");
        String username = parts[0];
        String[] nameParts = username.split("\\.");
        StringBuilder formattedName = new StringBuilder();
        for (String part : nameParts) {
            if (!part.isEmpty()) {
                formattedName.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1)).append(" ");
            }
        }
        return formattedName.toString().trim();
    }

    private void saveSettings() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        // Perform your save settings logic here
        // For simplicity, we'll just show a toast message
        String message = "Settings saved:\nName: " + name + "\nEmail: " + email;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
