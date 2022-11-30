package com.example.ssz.db.dto;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    //database 만들기
    @PrimaryKey(autoGenerate = false) // 알아서 id값을 넣어준다

    @NonNull private String id; // 하나의 사용자에 대한 고유 id값
    private String password;

    //getter & setter


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}