package com.eugene.meetupproject.di

import com.eugene.meetupproject.data.repository.AuthRepositoryImpl
import com.eugene.meetupproject.domain.repository.AuthRepository
import com.eugene.meetupproject.domain.repository.EventRepository
import com.eugene.meetupproject.domain.repository.RoomRepository
import com.eugene.meetupproject.domain.usecase.AddEventUseCase
import com.eugene.meetupproject.domain.usecase.AddRoomUseCase
import com.eugene.meetupproject.domain.usecase.ChangeRoomUseCase
import com.eugene.meetupproject.domain.usecase.DeleteRoomUseCase
import com.eugene.meetupproject.domain.usecase.GetAllRoomsUseCase
import com.eugene.meetupproject.domain.usecase.GetEventsForRoomUseCase
import com.eugene.meetupproject.domain.usecase.SignInUseCase
import com.eugene.meetupproject.domain.usecase.SignOutUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class DomainModule {
    @Provides
    fun provideSignInUseCase(authRepository: AuthRepository): SignInUseCase {
        return SignInUseCase(authRepository)
    }
    @Provides
    fun provideSignOutUseCase(authRepository: AuthRepository): SignOutUseCase{
        return SignOutUseCase(authRepository)
    }
    @Provides
    fun provideAddEventUseCase(eventRepository: EventRepository): AddEventUseCase{
        return AddEventUseCase(eventRepository)
    }
    @Provides
    fun provideGetAllRoomsUseCase(roomRepository: RoomRepository): GetAllRoomsUseCase{
        return GetAllRoomsUseCase(roomRepository)
    }
    @Provides
    fun provideAddRoomUseCase(roomRepository: RoomRepository): AddRoomUseCase{
        return AddRoomUseCase(roomRepository)
    }
    @Provides
    fun provideDeleteRoomUseCase(roomRepository: RoomRepository): DeleteRoomUseCase{
        return DeleteRoomUseCase(roomRepository)
    }
    @Provides
    fun provideChangeRoomUseCase(roomRepository: RoomRepository): ChangeRoomUseCase{
        return ChangeRoomUseCase(roomRepository)
    }
    @Provides
    fun provideGetEventsForRoomUseCase(roomRepository: EventRepository): GetEventsForRoomUseCase{
        return GetEventsForRoomUseCase(roomRepository)
    }
}