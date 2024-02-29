package com.example.loginform;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainForm extends AppCompatActivity {
    private ImageButton home, settings, card,masina;
    private ImageButton  settingsbutton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);

        home = findViewById(R.id.home);
        card = findViewById(R.id.card);
        masina = findViewById(R.id.masiniSpalat);
        settings = findViewById(R.id.settings);

        masina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainForm.this, ProgramariPage1.class);
                startActivity(intent);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainForm.this, MainForm.class);
                startActivity(intent);
            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainForm.this, Card.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainForm.this, Settings.class);
                startActivity(intent);
            }
        });
    }
}
