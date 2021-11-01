package com.learn.codingchallenge.di

import com.learn.codingchallenge.network.di.RepositoryModule
import com.learn.codingchallenge.network.repository.GifRepository
import com.learn.codingchallenge.utils.FakeGifRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class MockRepository {
    @Singleton
    @Binds
    abstract fun provideRepository(impl: FakeGifRepositoryImpl): GifRepository
}