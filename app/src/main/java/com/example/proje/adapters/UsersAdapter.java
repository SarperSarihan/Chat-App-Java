package com.example.proje.adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proje.databinding.ItemContainerUserBinding;
import com.example.proje.listeners.UserListener;
import com.example.proje.models.user;

import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private final List<user> users;
    private final UserListener userListener;

    public UsersAdapter(List<user> users,UserListener userListener) {
        this.users = users;
        this.userListener=userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding=ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        ItemContainerUserBinding binding;

        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding=itemContainerUserBinding;
        }

       void setUserData(user User){
            binding.textName.setText(User.name);
            binding.textName.setText(User.email);
            binding.imageProfil.setImageBitmap(getUserImage(User.image));
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(User));
       }
    }

    private Bitmap getUserImage(String encodedImage){
        byte[]bytes= Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }

}
