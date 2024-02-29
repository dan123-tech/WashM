package com.example.loginform;

import java.util.Random;

public class CardNumber {
    private int cardnumber;

    // Constructorul cu parametru care generează un cod de 4 cifre
    public CardNumber(int cardnumber) {
        this.cardnumber = generateRandom4DigitNumber();
    }

    // Constructorul simplu care returnează valoarea cardnumber
    public CardNumber() {
        this.cardnumber = getCardnumber();
    }

    // Metodă privată pentru generarea unui număr de 4 cifre
    private int generateRandom4DigitNumber() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // Generează un număr de 4 cifre
    }

    // Metodă pentru returnarea valorii cardnumber
    public int getCardnumber() {
        return cardnumber;
    }
}
