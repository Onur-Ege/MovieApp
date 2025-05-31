package com.example.jetmovie.di

import com.example.jetmovie.movie.MovieApiService
import com.example.jetmovie.movie.MovieRepositoryImpl
import com.example.jetmovie.movie.MovieRepository
import com.example.jetmovie.utils.K
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {

    val gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieApiService: MovieApiService
    ): MovieRepository = MovieRepositoryImpl(
        movieApiService
    )

    @Provides
    @Singleton
    fun provideMovieApiService(
        @Named("network") okHttpClient: OkHttpClient
    ): MovieApiService {
        return Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MovieApiService::class.java)
    }
}