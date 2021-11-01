package com.learn.codingchallenge.network.di

import com.learn.codingchallenge.network.repository.GifRepository
import com.learn.codingchallenge.network.repository.GifRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideRepository(api: GifRepositoryImpl): GifRepository
}