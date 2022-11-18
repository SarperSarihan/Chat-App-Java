package com.example.proje.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proje.databinding.ActivitySignupBinding;
import com.example.proje.utilities.Constants;
import com.example.proje.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager=new PreferenceManager(getApplicationContext());
        setListeners();

    }
    private void setListeners(){
        binding.TextHesabimVar.setOnClickListener(v ->onBackPressed());
        binding.SignUpButton.setOnClickListener(v ->{
            if (isValidSignUpDetails()){
                signUp();
            }
        });

        binding.layoutImage.setOnClickListener(v ->{
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        } );
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void signUp(){
        loading(true);
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        HashMap<String,Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, binding.Name.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE,encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME,binding.Name.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception ->{
                    loading(false);
                    showToast(exception.getMessage());
                });

    }
    private String encodeImage(Bitmap bitmap){
        int previewWidht = 150;
        int previewHeight = bitmap.getHeight() * previewWidht/ bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidht,previewHeight,false);
        ByteArrayOutputStream byteArrayOutPutStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutPutStream);
        byte[] bytes= byteArrayOutPutStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    private final ActivityResultLauncher <Intent> pickImage= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()== RESULT_OK){
                    if (result.getData()!=null){
                        Uri imageUri =result.getData().getData();
                        try {
                            InputStream inputStream=getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                            binding.imageProfil.setImageBitmap(bitmap);
                            binding.TextAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }

    );



    private Boolean isValidSignUpDetails(){
        if (encodedImage == null){
            showToast("Profil Fotoğrafı Seç");
            return false;
        }else if (binding.Name.getText().toString().trim().isEmpty()){
            showToast("İsim Giriniz");
            return false;
        }else if (binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Email Giriniz");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Geçerli Bir Resim Girin");
            return false;
        }else if (binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Şifrenizi Girin");
            return false;
        }else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()){
            showToast("Şifrenizi Doğrulayın");
            return false;
        }else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            showToast("Şifre ve Doğrulama Şifresi Aynı Olmalıdır");
            return false;
        }else {
            return true;
        }
    }
    private void loading(boolean isLoading){
        if (isLoading){
            binding.SignUpButton.setVisibility(View.INVISIBLE);
            binding.ProgressBar.setVisibility(View.INVISIBLE);
        }else {
            binding.ProgressBar.setVisibility(View.INVISIBLE);
            binding.SignUpButton.setVisibility(View.VISIBLE);
        }
    }
}