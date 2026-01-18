package com.example.bikeapp.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.bikeapp.db.TrackDatabase
import com.example.bikeapp.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.bikeapp.other.Constants.KEY_NAME
import com.example.bikeapp.other.Constants.KEY_WEIGHT
import com.example.bikeapp.other.Constants.SHARED_PREFERENCES_NAME
import com.example.bikeapp.other.Constants.TRACK_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTrackDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        TrackDatabase::class.java,
        TRACK_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideTrackDao(db: TrackDatabase) = db.getTrackDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPref: SharedPreferences) = sharedPref.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    fun provideWeight(sharedPref: SharedPreferences) = sharedPref.getFloat(KEY_WEIGHT, 80f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPref: SharedPreferences) =
        sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)
}