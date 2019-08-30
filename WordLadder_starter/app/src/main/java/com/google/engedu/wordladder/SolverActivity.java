package com.google.engedu.wordladder;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

public class SolverActivity extends AppCompatActivity {
    private String[] wordPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordPath = savedInstanceState.getStringArray("solvedPath");
    }

    protected void onSolve(){

    }
}
