package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Card extends AppCompatActivity {

    EditText cifra1, cifra2, cifra3, cifra4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);

        // Inițializați câmpurile de text
        cifra1 = findViewById(R.id.c1);
        cifra2 = findViewById(R.id.c2);
        cifra3 = findViewById(R.id.c3);
        cifra4 = findViewById(R.id.c4);

        // Obțineți codul de programare folosind Singleton-ul CodGenerator
        int codProgramare = CodGenerator.getInstance().getCodProgramare();

        // Afișați fiecare cifră în câmpurile de text corespunzătoare
        cifra1.setText(String.valueOf(codProgramare / 1000));
        cifra2.setText(String.valueOf((codProgramare % 1000) / 100));
        cifra3.setText(String.valueOf((codProgramare % 100) / 10));
        cifra4.setText(String.valueOf(codProgramare % 10));

        ImageButton backButton = findViewById(R.id.register);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Card.this, MainForm.class);
                startActivity(intent);
            }
        });
    }
}
