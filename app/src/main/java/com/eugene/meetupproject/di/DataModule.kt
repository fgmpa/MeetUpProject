package com.eugene.meetupproject.di

import com.eugene.meetupproject.data.repository.AuthRepositoryImpl
import com.eugene.meetupproject.data.repository.EventRepositoryImpl
import com.eugene.meetupproject.data.repository.RoomRepositoryImpl
import com.eugene.meetupproject.data.repository.UserRepositoryImpl
import com.eugene.meetupproject.domain.repository.AuthRepository
import com.eugene.meetupproject.domain.repository.EventRepository
import com.eugene.meetupproject.domain.repository.RoomRepository
import com.eugene.meetupproject.domain.repository.UserRepository
import dagger.Binds
import dagger.Module


@Module
interface DataModule {

    @Binds
    fun  bindsAuthRepository(impl: AuthRepositoryImpl) : AuthRepository

    @Binds
    fun bindEventRepository(impl: EventRepositoryImpl): EventRepository
    @Binds
    fun bindRoomRepository(impl: RoomRepositoryImpl): RoomRepository
    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}