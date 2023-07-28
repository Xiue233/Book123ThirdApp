package io.github.xiue233.book123.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.xiue233.book123.repository.BookRepository
import io.github.xiue233.book123.ui.main.home.HomeViewModel

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideHomeViewModel(
        bookRepository: BookRepository
    ) = HomeViewModel(bookRepository)
}