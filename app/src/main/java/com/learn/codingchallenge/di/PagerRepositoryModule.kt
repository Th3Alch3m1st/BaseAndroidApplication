package com.learn.codingchallenge.di

import com.learn.codingchallenge.repository.PagingRepository
import com.learn.codingchallenge.repository.PagingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created By Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@Module
@InstallIn(ViewModelComponent::class)
abstract class PagerRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun providePagingRepository(repositoryImpl: PagingRepositoryImpl): PagingRepository
}