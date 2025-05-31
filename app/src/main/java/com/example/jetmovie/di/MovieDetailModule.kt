package com.example.jetmovie.di

import com.example.jetmovie.movie_detail.MovieDetailApiService
import com.example.jetmovie.movie_detail.MovieDetailRepositoryImpl
import com.example.jetmovie.movie_detail.MovieDetailRepository
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
object MovieDetailModule {

    val gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideMovieDetailRepository(
        movieDetailApiService: MovieDetailApiService,
    ): MovieDetailRepository = MovieDetailRepositoryImpl(
        movieDetailApiService = movieDetailApiService,
    )

    @Provides
    @Singleton
    fun provideMovieDetailApiService(
        @Named("network") okHttpClient: OkHttpClient
    ): MovieDetailApiService {
        return Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MovieDetailApiService::class.java)
    }
}

