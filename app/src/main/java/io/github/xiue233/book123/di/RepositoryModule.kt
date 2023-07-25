package io.github.xiue233.book123.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.xiue233.book123.network.Book123Service
import io.github.xiue233.book123.repository.BookRepository
import io.github.xiue233.book123.repository.BookRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideBookRepository(
        book123Service: Book123Service
    ): BookRepository = BookRepositoryImpl(book123Service)

}