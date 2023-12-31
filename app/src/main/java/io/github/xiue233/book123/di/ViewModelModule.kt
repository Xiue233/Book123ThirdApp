package io.github.xiue233.book123.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.xiue233.book123.repository.BookRepository
import io.github.xiue233.book123.ui.detail.BookDetailViewModel
import io.github.xiue233.book123.ui.main.home.HomeViewModel
import io.github.xiue233.book123.ui.main.sort.SortViewModel
import io.github.xiue233.book123.ui.search.SearchViewModel

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
        @ApplicationContext context: Context,
        bookRepository: BookRepository
    ) = BookDetailViewModel(context, bookRepository)

    @Provides
    @ViewModelScoped
    fun provideSearchViewModel(
        bookRepository: BookRepository
    ) = SearchViewModel(bookRepository)
}