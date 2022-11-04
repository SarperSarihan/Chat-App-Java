package com.example.proje.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.proje.R;
import com.example.proje.databinding.ActivitySigninBinding;

public class SigninActivity extends AppCompatActivity {

    private ActivitySigninBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();

    }

    private void setListeners() {
        binding.textkayitol.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignupActivity.class)));
    }

}