package com.example.login;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase  extends RoomDatabase { // roomdatabase 상속
    public abstract UserDao userDao();
}
