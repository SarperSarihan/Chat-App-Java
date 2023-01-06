package com.example.proje.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.proje.adapters.UsersAdapter;
import com.example.proje.databinding.ActivityUsersBinding;
import com.example.proje.listeners.UserListener;
import com.example.proje.models.user;
import com.example.proje.utilities.Constants;
import com.example.proje.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager=new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }
    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void getUsers(){
        loading(true);
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId=preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() !=null){
                        List<user>users=new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                            if (currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            user User= new user();
                            User.name=queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            User.email=queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            User.image=queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            User.token=queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            User.id=queryDocumentSnapshot.getId();
                            users.add(User);
                        }
                        if (users.size()>0){
                            UsersAdapter usersAdapter=new UsersAdapter(users,this);
                            binding.usersRecyclerView.setAdapter(usersAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        }else{
                            showErrorMessage();
                        }
                    }else {
                        showErrorMessage();
                    }
                });
    }

    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s","no user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }


    private void loading(Boolean isLoading){
        if (isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(user User) {
        Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
        intent.putExtra(Constants.KEY_USER,User);
        startActivity(intent);
        finish();
    }
}