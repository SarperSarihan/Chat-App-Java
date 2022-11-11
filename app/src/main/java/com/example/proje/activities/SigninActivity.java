package com.example.proje.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proje.R;
import com.example.proje.databinding.ActivitySigninBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SigninActivity extends AppCompatActivity {

    private ActivitySigninBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        binding.SignNButton.setOnClickListener(v -> addDataToFireStore());
    }
    private void setListeners(){
        binding.TextKayitOl.setOnClickListener(v ->
            startActivity(new Intent(getApplicationContext(), SignupActivity.class)));

    }
    private void addDataToFireStore(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String,Object> data =new HashMap<>();
        data.put("first_name","Sarper");
        data.put("last_name","Sarıhan");
        database.collection("users")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(),"Data Inserted",Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception ->{
                    Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                });
    }

}