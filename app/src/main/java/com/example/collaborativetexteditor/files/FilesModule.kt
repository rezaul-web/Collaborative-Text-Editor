package com.example.collaborativetexteditor.files

import com.example.collaborativetexteditor.files.data.repository.DocRepository
import com.example.collaborativetexteditor.files.data.repository.DocRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FilesModule {

    @Binds
    @Singleton
    abstract fun provideFileRepository(impl: DocRepositoryImpl): DocRepository
}