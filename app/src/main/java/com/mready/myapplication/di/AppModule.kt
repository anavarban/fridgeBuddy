package com.mready.myapplication.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.mready.myapplication.auth.AuthRepository
import com.mready.myapplication.auth.AuthRepositoryImpl
import com.mready.myapplication.data.FridgeDatabase
import com.mready.myapplication.data.FridgeIngredientsRepo
import com.mready.myapplication.data.FridgeIngredientsRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun provideFridgeDatabase(@ApplicationContext context: Context): FridgeDatabase = FridgeDatabase.getDatabase(context)

    @Provides
    fun fridgeIngredientsRepo(db: FridgeDatabase): FridgeIngredientsRepo = FridgeIngredientsRepoImpl(db.fridgeDao())

}