package com.devshiv.dailyquotes.di

import android.content.Context
import androidx.room.Room
import com.devshiv.dailyquotes.R
import com.devshiv.dailyquotes.api.RetrofitApi
import com.devshiv.dailyquotes.db.AppDatabase
import com.devshiv.dailyquotes.db.dao.SettingsDao
import com.devshiv.dailyquotes.db.dao.UsersDao
import com.devshiv.dailyquotes.repository.DataRepository
import com.devshiv.dailyquotes.utils.Constants
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val booleanAsIntAdapter = object : TypeAdapter<Boolean?>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: Boolean?) {
                if (value == null) {
                    out.nullValue()
                } else {
                    out.value(value)
                }
            }

            @Throws(IOException::class)
            override fun read(`in`: JsonReader): Boolean? {
                val peek: JsonToken = `in`.peek()
                return if (peek === JsonToken.BOOLEAN) {
                    `in`.nextBoolean()
                } else if (peek === JsonToken.NULL) {
                    `in`.nextNull()
                    null
                } else if (peek === JsonToken.NUMBER) {
                    `in`.nextInt() !== 0
                } else if (peek === JsonToken.STRING) {
                    `in`.nextString().equals("1", true)
                } else {
                    throw IllegalStateException("Expected BOOLEAN or NUMBER but was $peek")
                }
            }
        }
        val gson = GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, booleanAsIntAdapter)
            .create()
        return Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(gson)
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitAPI(retrofit: Retrofit): RetrofitApi {
        return retrofit.create(RetrofitApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDataRepository(api: RetrofitApi): DataRepository {
        return DataRepository(api)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            appContext.getString(R.string.app_name).lowercase() + "_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideSettingsDao(appDatabase: AppDatabase): SettingsDao {
        return appDatabase.settingsDao()
    }

    @Provides
    fun provideUsersDao(appDatabase: AppDatabase): UsersDao {
        return appDatabase.usersDao()
    }
}