package com.mready.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import javax.inject.Singleton

@Singleton
@Database(entities = [FridgeIngredients::class], version = 1, exportSchema = false)
abstract class FridgeDatabase: RoomDatabase() {

    abstract fun fridgeDao(): FridgeIngredientsDao

    companion object {

        @Volatile
        private var Instance: FridgeDatabase? = null

        fun getDatabase(context: Context): FridgeDatabase {
            if (Instance == null) {
                synchronized(FridgeDatabase::class) {
                    Instance = androidx.room.Room.databaseBuilder(
                        context.applicationContext,
                        FridgeDatabase::class.java,
                        "fridge_database"
                    ).build()
                }
            }
            return Instance!!
        }

    }

}