package com.example.loginform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class ProgramariPage1 extends AppCompatActivity {
    private Spinner camineSpinner, etajeSpinner, camereSpinner, campusSpinner, masinaSpinner;
    private HashMap<String, HashMap<String, Object[]>> camereMap = new HashMap<>();
    private Button programare;
    private String selectedDate;
    static int codProgramareGenerat;
    private TimePicker timePickerInceput, timePickerSfarsit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programari_page1);

        camineSpinner = findViewById(R.id.camineSpinner);
        etajeSpinner = findViewById(R.id.etajeSpinner);
        camereSpinner = findViewById(R.id.camereSpinner);
        campusSpinner = findViewById(R.id.campusSpinner);
        masinaSpinner = findViewById(R.id.masinaSpalatSpinner);
        programare = findViewById(R.id.btnProgramare);
        timePickerSfarsit = findViewById(R.id.timePickerSfarsit);
        timePickerInceput = findViewById(R.id.timePickerInceput);

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            }
        });

        timePickerInceput.setIs24HourView(true);
        timePickerInceput.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timePickerInceput.setMinute(0);
            }
        });

        timePickerSfarsit.setIs24HourView(true);
        timePickerSfarsit.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timePickerSfarsit.setMinute(0);
            }
        });

        programare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String masinaSpalat = masinaSpinner.getSelectedItem().toString();
                String etaj = etajeSpinner.getSelectedItem().toString();
                String camera = camereSpinner.getSelectedItem().toString();
                String camin = camineSpinner.getSelectedItem().toString();

                int hourSfarsit = timePickerSfarsit.getHour();
                int minSfarsit = timePickerSfarsit.getMinute();
                int hourInceput = timePickerInceput.getHour();
                int minInceput = timePickerInceput.getMinute();

                String oraInceput = String.format(Locale.getDefault(), "%02d:%02d", hourInceput, minInceput);
                String oraSfarsit = String.format(Locale.getDefault(), "%02d:%02d", hourSfarsit, minSfarsit);

                boolean isValidTimeRange = hourSfarsit - hourInceput == 1 && minSfarsit == 0 && minInceput == 0;
                boolean isWithinTwoHours = hourSfarsit - hourInceput <= 2;
                boolean isOverlapping = false;
                if (isValidTimeRange && isWithinTwoHours) {
                    checkExistingAppointments(camin, masinaSpalat, selectedDate, oraInceput, oraSfarsit,etaj);
                } else {
                    if (!isValidTimeRange) {
                        Toast.makeText(ProgramariPage1.this, "Ora de sfârșit trebuie să fie cu exact o oră mai mare decât ora de început.", Toast.LENGTH_SHORT).show();
                    } else if (!isWithinTwoHours) {
                        Toast.makeText(ProgramariPage1.this, "Distanța maximă între orele de început și sfârșit trebuie să fie de maxim 2 ore.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        HashMap<String, Object[]> memoCamere = new HashMap<>();
        memoCamere.put("1", new Object[]{new String[]{"101", "102", "103", "104", "105", "106", "107", "108", "109", "110"}, 5});
        memoCamere.put("2", new Object[]{new String[]{"201", "202", "203", "204", "205", "206", "207", "208", "209", "210"}, 3});
        memoCamere.put("3", new Object[]{new String[]{"301", "302", "303", "304", "305", "306", "307", "308", "309", "310"}, 6});
        memoCamere.put("4", new Object[]{new String[]{"401", "402", "403", "404", "405", "406", "407", "408", "409", "410"}, 4});

        HashMap<String, Object[]> colinaCamere = new HashMap<>();
        colinaCamere.put("Etaj 1", new Object[]{new String[]{"101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120"}, 5});
        colinaCamere.put("Etaj 2", new Object[]{new String[]{"201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213", "214", "215", "216", "217", "218", "219", "220"}, 6});
        colinaCamere.put("Etaj 3", new Object[]{new String[]{"301", "302", "303", "304", "305", "306", "307", "308", "309", "310", "311", "312", "313", "314", "315", "316", "317", "318", "319", "320"}, 7});
        colinaCamere.put("Etaj 4", new Object[]{new String[]{"401", "402", "403", "404", "405", "406", "407", "408", "409", "410", "411", "412", "413", "414", "415", "416", "417", "418", "419", "420"}, 8});
        colinaCamere.put("Etaj 5", new Object[]{new String[]{"501", "502", "503", "504", "505", "506", "507", "508", "509", "510", "511", "512", "513", "514", "515", "516", "517", "518", "519", "520"}, 9});
        colinaCamere.put("Etaj 6", new Object[]{new String[]{"601", "602", "603", "604", "605", "606", "607", "608", "609", "610", "611", "612", "613", "614", "615", "616", "617", "618", "619", "620"}, 10});
        colinaCamere.put("Etaj 7", new Object[]{new String[]{"701", "702", "703", "704", "705", "706", "707", "708", "709", "710", "711", "712", "713", "714", "715", "716", "717", "718", "719", "720"}, 11});
        colinaCamere.put("Etaj 8", new Object[]{new String[]{"801", "802", "803", "804", "805", "806", "807", "808", "809", "810", "811", "812", "813", "814", "815", "816", "817", "818", "819", "820"}, 12});

        camereMap.put("Memo", memoCamere);
        camereMap.put("Colina", colinaCamere);

        ArrayAdapter<CharSequence> campusAdapter = ArrayAdapter.createFromResource(this, R.array.campus_array, android.R.layout.simple_spinner_item);
        campusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campusSpinner.setAdapter(campusAdapter);

        campusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCampus = (String) parentView.getSelectedItem();
                if (selectedCampus.equals("Memo")) {
                    ArrayAdapter<CharSequence> memoCamineAdapter = ArrayAdapter.createFromResource(ProgramariPage1.this, R.array.memo_camine_array, android.R.layout.simple_spinner_item);
                    memoCamineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    camineSpinner.setAdapter(memoCamineAdapter);
                } else if (selectedCampus.equals("Colina")) {
                    ArrayAdapter<CharSequence> colinaCamineAdapter = ArrayAdapter.createFromResource(ProgramariPage1.this, R.array.colina_camine_array, android.R.layout.simple_spinner_item);
                    colinaCamineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    camineSpinner.setAdapter(colinaCamineAdapter);
                }

                if (camereMap.containsKey(selectedCampus)) {
                    HashMap<String, Object[]> etajeCamere = camereMap.get(selectedCampus);

                    String[] etaje = etajeCamere.keySet().toArray(new String[0]);
                    ArrayAdapter<String> etajeAdapter = new ArrayAdapter<>(ProgramariPage1.this, android.R.layout.simple_spinner_item, etaje);
                    etajeSpinner.setAdapter(etajeAdapter);

                    etajeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            String selectedEtaj = (String) parentView.getSelectedItem();
                            Object[] camereEtaj = etajeCamere.get(selectedEtaj);

                            if (camereEtaj != null && camereEtaj.length >= 2 && camereEtaj[0] instanceof String[] && camereEtaj[1] instanceof Integer) {
                                String[] camere = (String[]) camereEtaj[0];
                                int numarMasiniSpalat = (int) camereEtaj[1];

                                ArrayAdapter<String> camereAdapter = new ArrayAdapter<>(ProgramariPage1.this, android.R.layout.simple_spinner_item, camere);
                                camereSpinner.setAdapter(camereAdapter);

                                String[] numereMasini = new String[numarMasiniSpalat];
                                for (int i = 0; i < numarMasiniSpalat; i++) {
                                    numereMasini[i] = String.valueOf(i + 1);
                                }
                                ArrayAdapter<String> masinaAdapter = new ArrayAdapter<>(ProgramariPage1.this, android.R.layout.simple_spinner_item, numereMasini);
                                masinaSpinner.setAdapter(masinaAdapter);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void checkExistingAppointments(String camin, String masinaSpalat, String selectedDate, String oraInceput, String oraSfarsit, String etaj) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Verificăm dacă există o programare în același interval orar pentru aceeași mașină
        Task<DataSnapshot> dataSnapshotTask = databaseReference.child("Camine").child("Caminul " + camin).child("Masina " + masinaSpalat).child("Programari").child(selectedDate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    boolean isOverlapping = false;
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String programare = snapshot.getKey();
                        String[] parts = programare.split("-");
                        String existingOraInceput = parts[0];
                        String existingOraSfarsit = parts[1];

                        boolean isSameInterval = (existingOraInceput.compareTo(oraInceput) >= 0 && existingOraInceput.compareTo(oraSfarsit) < 0) ||
                                (existingOraSfarsit.compareTo(oraInceput) > 0 && existingOraSfarsit.compareTo(oraSfarsit) <= 0);

                        if (isSameInterval) {
                            isOverlapping = true;
                            break;
                        }
                    }

                    if (isOverlapping) {
                        Toast.makeText(ProgramariPage1.this, "Există deja o programare în același interval orar pentru aceeași mașină.", Toast.LENGTH_SHORT).show();
                    } else {
                        int codProgramare = generateRandomCode();
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();
                        String programareH = oraInceput + "-" + oraSfarsit;
                        String Idmasina = camin + etaj + masinaSpalat;
                        Intent intent = new Intent(ProgramariPage1.this, Card.class);
                        databaseReference.child("Camine").child("Caminul " + camin).child("Masina " + masinaSpalat).child("Programari").child(selectedDate).child(programareH).child(String.valueOf(codProgramare)).child("User Id").setValue(userId);
                        intent.putExtra("codProgramare", codProgramare);
                        startActivity(intent);
                        codProgramareGenerat = codProgramare;
                        CodGenerator.getInstance().setCodProgramare(codProgramare);
                        Toast.makeText(ProgramariPage1.this, "Programarea a fost adăugată cu succes. Codul tău este: " + codProgramare, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProgramariPage1.this, "Eroare: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int generateRandomCode() {
        // Generăm un cod aleatoriu de 4 cifre
        Random random = new Random();
        return random.nextInt(9000) + 1000;
    }
}
