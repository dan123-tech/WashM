package com.example.loginform;

public class EtajInfo {
    private String[] camere;
    private int numarMasiniSpalat;

    public EtajInfo(String[] camere, int numarMasiniSpalat) {
        this.camere = camere;
        this.numarMasiniSpalat = numarMasiniSpalat;
    }

    public String[] getCamere() {
        return camere;
    }

    public int getNumarMasiniSpalat() {
        return numarMasiniSpalat;
    }
}
