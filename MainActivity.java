package com.example.loginform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button verificare, inregistrare, reconect;
    private ImageButton amprenta, viewPass;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private CheckBox remember;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String EMAIL_KEY = "emailKey";
    private static final String PASSWORD_KEY = "passwordKey";

    private BiometricPrompt biometricPrompt;
    private static BiometricPrompt.PromptInfo promptInfo;

    private Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        verificare = findViewById(R.id.Verificare);
        reconect = findViewById(R.id.Reconect);
        inregistrare = findViewById(R.id.registration);
        remember = findViewById(R.id.Remember);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email.setText(sharedPreferences.getString(EMAIL_KEY, ""));
        password.setText(sharedPreferences.getString(PASSWORD_KEY, ""));
        remember.setChecked(!email.getText().toString().isEmpty());

        amprenta = findViewById(R.id.Amprenta);
        viewPass = findViewById(R.id.ViewPass);

        final boolean[] passwordVisible = {false};

        viewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordVisible[0]) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                passwordVisible[0] = !passwordVisible[0];
            }
        });

        amprenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerPrintLogIn();
            }
        });

        verificare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();

                if (remember.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(EMAIL_KEY, email.getText().toString());
                    editor.putString(PASSWORD_KEY, password.getText().toString());
                    editor.apply();
                }
            }
        });

        inregistrare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterUser.class);
                startActivity(intent);
            }
        });

        reconect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Recovery.class);
                startActivity(intent);
            }
        });
    }

    private void fingerPrintLogIn() {
        executor = Executors.newSingleThreadExecutor();
        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "Nu funcționează", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "Dispozitivul nu are amprentă", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "Dispozitivul nu are nicio amprentă salvată", Toast.LENGTH_SHORT).show();
                break;
        }

        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }


            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, MainForm.class);
                        startActivity(intent);
                        finish();
                    }
                });
                super.onAuthenticationSucceeded(result);
            }


            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("UNITBV")
                .setDescription("Folosește amprenta pentru a te loga")
                .setDeviceCredentialAllowed(true)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void loginUserAccount() {
        String emailVerification = email.getText().toString();
        String passwordVerification = password.getText().toString();

        if (TextUtils.isEmpty(emailVerification)) {
            Toast.makeText(getApplicationContext(), "Introduceți adresa de email!", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(passwordVerification)) {
            Toast.makeText(getApplicationContext(), "Introduceți parola!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailVerification, passwordVerification)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, MainForm.class);
                            startActivity(intent);
                            //progressbar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Autentificare eșuată!", Toast.LENGTH_LONG).show();
                            // progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }
    private String extractNameFromEmail(String email) {
        // Splităm emailul în funcție de caracterul '@'
        String[] parts = email.split("@");
        // Obținem partea dinaintea caracterului '@'
        String username = parts[0];
        // Splităm numele în funcție de caracterul '.'
        String[] nameParts = username.split("\\.");
        // Construim numele formatat corespunzător (fiecare parte a numelui începe cu litera mare)
        StringBuilder formattedName = new StringBuilder();
        for (String part : nameParts) {
            // Verificăm dacă partea este goală (pentru cazurile în care există puncte consecutive)
            if (!part.isEmpty()) {
                // Transformăm prima literă în majusculă și adăugăm partea la nume
                formattedName.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1)).append(" ");
            }
        }
        // Ștergem spațiul adăugat la sfârșit și returnăm numele formatat
        return formattedName.toString().trim();
    }
}
