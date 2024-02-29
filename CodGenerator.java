package com.example.loginform;

public class CodGenerator {
    private static CodGenerator instance;
    private int codProgramare;

    private CodGenerator() {
        // Constructor privat pentru Singleton
    }

    public static CodGenerator getInstance() {
        if (instance == null) {
            instance = new CodGenerator();
        }
        return instance;
    }

    public int getCodProgramare() {
        return codProgramare;
    }

    public void setCodProgramare(int codProgramare) {
        this.codProgramare = codProgramare;
    }
}
