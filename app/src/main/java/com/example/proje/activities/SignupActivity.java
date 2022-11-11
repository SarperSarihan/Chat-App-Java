package com.example.proje.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proje.R;
import com.example.proje.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();

    }
    private void setListeners(){
        binding.TextHesabMVar.setOnClickListener(v ->onBackPressed());
    }


}