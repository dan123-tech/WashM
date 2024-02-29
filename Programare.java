package com.example.loginform;

public class Programare {

    public String data;
    private String oraInceput;
    public String masinaSpalat;
    public String etaj;
    public String camera;
    public String camin;
    public int hourInceput;
    public int minInceput;
    public int hourSfarsit;
    public int minSfarsit;
    public int card;

    public Programare() {
        // Constructor gol necesar pentru Firebase
    }

    public Programare(String data, String masinaSpalat, String camera, String etaj, String camin,int hourInceput, int minInceput, int hourSfarsit, int minSfarsit) {
        this.data=data;
        this.card=card;
        this.masinaSpalat = masinaSpalat;
        this.etaj = etaj;
        this.camera = camera;
        this.camin = camin;
        this.hourInceput = hourInceput;
        this.minInceput = minInceput;
        this.hourSfarsit = hourSfarsit;
        this.minSfarsit = minSfarsit;
    }
    public String getData(){
        return data;
   }

    public int getCard() {
        return card;
    }

    public String getMasinaSpalat(){
        return masinaSpalat;
   }
   public String getCamin(){return camin;}
    public String getEtaj(){return etaj;}

    public String getHourInceput() {
        return oraInceput;
    }

    public int getMinInceput() {
        return minInceput;
    }

    public int getHourSfarsit() {
        return hourSfarsit;
    }

    public int getMinSfarsit() {
        return minSfarsit;
    }
    public String getOraInceput() {
        return oraInceput;
    }
    public void setOraInceput(String oraInceput) {
        this.oraInceput = oraInceput;
    }


}
