package com.example.loginform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Recovery extends AppCompatActivity {
    private Button recovery;
    private EditText emailRec;
    private FirebaseAuth mAuth;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        mAuth = FirebaseAuth.getInstance();
        recovery = findViewById(R.id.recoveryBtn);
        emailRec = findViewById(R.id.emailRecovery);

        emailRec.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    emailRec.setText("");
                }
            }
        });

        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = emailRec.getText().toString();
                if (!TextUtils.isEmpty(mail)) {
                    resetPassword();
                } else {
                    Toast.makeText(Recovery.this, "Introduceti email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetPassword() {
        mAuth.sendPasswordResetEmail(mail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Recovery.this, "Link-ul de resetare al parolei a fost trimis", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Recovery.this, "Link-ul de resetare al parolei NU a fost trimis", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
