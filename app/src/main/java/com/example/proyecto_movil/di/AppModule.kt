package com.example.proyecto_movil.di

import com.example.proyecto_movil.data.datasource.ArtistRemoteDataSource
import com.example.proyecto_movil.data.datasource.impl.retrofit.*
import com.example.proyecto_movil.data.datasource.services.*
import com.example.proyecto_movil.data.repository.*

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://10.0.2.2:5000/api/"

    // ----------------------------
    // Retrofit
    // ----------------------------
    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ----------------------------
    // Retrofit Services
    // ----------------------------
    @Singleton
    @Provides
    fun provideUserRetrofitService(retrofit: Retrofit): UserRetrofitService =
        retrofit.create(UserRetrofitService::class.java)

    @Singleton
    @Provides
    fun provideAlbumRetrofitService(retrofit: Retrofit): AlbumRetrofitService =
        retrofit.create(AlbumRetrofitService::class.java)

    @Singleton
    @Provides
    fun provideReviewRetrofitService(retrofit: Retrofit): ReviewRetrofitService =
        retrofit.create(ReviewRetrofitService::class.java)

    @Singleton
    @Provides
    fun providePlaylistRetrofitService(retrofit: Retrofit): PlaylistRetrofitService =
        retrofit.create(PlaylistRetrofitService::class.java)

    @Singleton
    @Provides
    fun provideArtistRetrofitService(retrofit: Retrofit): ArtistRetrofitService =
        retrofit.create(ArtistRetrofitService::class.java)

    // ----------------------------
    // Data Sources
    // ----------------------------
    @Singleton
    @Provides
    fun provideAlbumRemoteDataSource(
        service: AlbumRetrofitService
    ): AlbumRetrofitDataSourceImpl = AlbumRetrofitDataSourceImpl(service)

    @Singleton
    @Provides
    fun provideReviewRemoteDataSource(
        service: ReviewRetrofitService
    ): ReviewRetrofitDataSourceImplement = ReviewRetrofitDataSourceImplement(service)

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(
        service: UserRetrofitService
    ): UserRetrofitDataSourceImpl = UserRetrofitDataSourceImpl(service)

    @Singleton
    @Provides
    fun providePlaylistRemoteDataSource(
        service: PlaylistRetrofitService
    ): PlaylistRetrofitDataSourceImpl = PlaylistRetrofitDataSourceImpl(service)

    @Singleton
    @Provides
    fun provideArtistRemoteDataSource(
        service: ArtistRetrofitService
    ): ArtistRemoteDataSource = ArtistRetrofitDataSourceImpl(service)
    // ⚠️ Si tu repo usa interfaz ArtistRemoteDataSource, mantenlo así.
    // Si no existe esa interfaz, devuelve directamente ArtistRemoteDataSourceImpl.

    // ----------------------------
    // Repositories
    // ----------------------------
    @Singleton
    @Provides
    fun provideAlbumRepository(
        dataSource: AlbumRetrofitDataSourceImpl
    ): AlbumRepository = AlbumRepository(dataSource)

    @Singleton
    @Provides
    fun provideReviewRepository(
        dataSource: ReviewRetrofitDataSourceImplement
    ): ReviewRepository = ReviewRepository(dataSource)

    @Singleton
    @Provides
    fun providePlaylistRepository(
        dataSource: PlaylistRetrofitDataSourceImpl
    ): PlaylistRepository = PlaylistRepository(dataSource)

    @Singleton
    @Provides
    fun provideUserRepository(
        dataSource: UserRetrofitDataSourceImpl
    ): UserRepository = UserRepository(dataSource)

    @Singleton
    @Provides
    fun provideArtistRepository(
        dataSource: ArtistRemoteDataSource
    ): ArtistRepository = ArtistRepository(dataSource)
}
