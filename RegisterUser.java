package com.example.loginform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class RegisterUser extends AppCompatActivity {
    private EditText emailTextView, passwordTextView;
    private Button Btn, back,inregistrare;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private ImageButton viewPass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
       inregistrare = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressbar);
        viewPass = findViewById(R.id.ViewPass);

        final boolean[] passwordVisible = {false};
        viewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordVisible[0]) {
                    passwordTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    passwordTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                passwordVisible[0] = !passwordVisible[0];
            }
        });
        inregistrare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });

    }

    private void registerNewUser() {
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
            Toast.makeText(getApplicationContext(), "Te rugăm să introduci o adresă de email validă!", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            Toast.makeText(getApplicationContext(), "Te rugăm să introduci o parolă validă!", Toast.LENGTH_LONG).show();
            return;
        }

        Btn.setEnabled(false); // Dezactivează butonul de înregistrare
        progressBar.setVisibility(View.VISIBLE); // Afișează bara de progres

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Înregistrare reușită!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterUser.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Închide activitatea curentă după înregistrare
                        } else {
                            String errorMessage = task.getException().getMessage(); // Obține mesajul de eroare
                            Toast.makeText(getApplicationContext(), "Înregistrare eșuată: " + errorMessage, Toast.LENGTH_LONG).show();
                            Btn.setEnabled(true); // Reactivează butonul de înregistrare
                            progressBar.setVisibility(View.GONE); // Ascunde bara de progres
                        }
                    }
                });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@student.unitbv.ro";
        return email.matches(emailPattern);
    }

    private String getPasswordValidity(String password) {
        StringBuilder message = new StringBuilder();

        if (password.length() < 8) {
            message.append("Parola trebuie să conțină cel puțin 8 caractere.\n");
        }

        // Verificăm dacă parola conține cel puțin o cifră
        boolean containsDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                containsDigit = true;
                break;
            }
        }
        if (!containsDigit) {
            message.append("Parola trebuie să conțină cel puțin o cifră.\n");
        }

        // Verificăm dacă parola conține cel puțin un caracter special
        boolean containsSpecialChar = false;
        String specialChars = "!@#$%^&*()-_+=[]{}|:;<>,.?/~";
        for (char c : password.toCharArray()) {
            if (specialChars.contains(String.valueOf(c))) {
                containsSpecialChar = true;
                break;
            }
        }
        if (!containsSpecialChar) {
            message.append("Parola trebuie să conțină cel puțin un caracter special.\n");
        }

        return message.toString();
    }


    private boolean isPasswordValid(String password) {
        String validityMessage = getPasswordValidity(password);
        return validityMessage.isEmpty();
    }
}
