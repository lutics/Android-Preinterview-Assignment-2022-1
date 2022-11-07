package com.example.android.preinterview.assignment

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun database(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context, Database::class.java, "database.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun httpClient() = HttpClient(OkHttp) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "gateway.marvel.com"
            }
        }
        engine {
            addNetworkInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }
        install(ContentNegotiation) {
            gson()
        }
    }
}