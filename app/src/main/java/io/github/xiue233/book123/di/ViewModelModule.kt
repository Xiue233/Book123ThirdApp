package io.github.xiue233.book123.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.xiue233.book123.repository.BookRepository
import io.github.xiue233.book123.ui.detail.BookDetailViewModel
import io.github.xiue233.book123.ui.main.home.HomeViewModel
import io.github.xiue233.book123.ui.main.sort.SortViewModel

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideHomeViewModel(
        bookRepository: BookRepository
    ) = HomeViewModel(bookRepository)

    @Provides
    @ViewModelScoped
    fun provideSortViewModel(
        bookRepository: BookRepository
    ) = SortViewModel(bookRepository)

    @Provides
    @ViewModelScoped
    fun provideBookDetailViewModel(
        bookRepository: BookRepository
    ) = BookDetailViewModel(bookRepository)
}