package com.example.collaborativetexteditor.auth


import com.example.collaborativetexteditor.auth.data.AuthRepository
import com.example.collaborativetexteditor.auth.data.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository = FirebaseAuthRepository(firebaseAuth)
}
