package com.example.pokemon;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Pokemon {
    private String pName;
    private Bitmap pImage;
    private int pHeight;
    private int pWeight;
    private ArrayList<String> pTypes;
    private int pHP;
    private int pAttack;
    private int pDefense;

    public Pokemon(String pName, Bitmap pImage, int pHeight, int pWeight, ArrayList<String> pTypes, int pHP, int pAttack, int pDefense) {
        this.pName = pName;
        this.pImage = pImage;
        this.pHeight = pHeight;
        this.pWeight = pWeight;
        this.pTypes = pTypes;
        this.pHP = pHP;
        this.pAttack = pAttack;
        this.pDefense = pDefense;
    }

    public String getpName() {
        return pName;
    }

    public Bitmap getpImage() {
        return pImage;
    }

    public int getpHeight() {
        return pHeight;
    }

    public int getpWeight() {
        return pWeight;
    }

    public ArrayList<String> getpTypes() {
        return pTypes;
    }

    public int getpHP() {
        return pHP;
    }

    public int getpAttack() {
        return pAttack;
    }

    public int getpDefense() {
        return pDefense;
    }
}
