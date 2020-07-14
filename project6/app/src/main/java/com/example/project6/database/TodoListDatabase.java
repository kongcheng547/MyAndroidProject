package com.example.project6.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
@Database(entities = {TodoListEntity.class}, version = 2)
@TypeConverters(DateConverter.class)
public abstract class TodoListDatabase extends RoomDatabase {
    private static volatile TodoListDatabase INSTANCE;

    public abstract TodoListDao todoListDao();

    public TodoListDatabase() {

    }
    public static TodoListDatabase inst(Context context) {
        if (INSTANCE == null) {
            synchronized (TodoListDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TodoListDatabase.class, "todo.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
