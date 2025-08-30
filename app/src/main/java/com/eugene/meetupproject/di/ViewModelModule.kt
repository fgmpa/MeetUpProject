package com.eugene.meetupproject.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eugene.meetupproject.presentation.viewmodel.AuthViewModel
import com.eugene.meetupproject.presentation.DaggerViewModelFactory
import com.eugene.meetupproject.di.ViewModelKey
import com.eugene.meetupproject.presentation.MeetingListFragment
import com.eugene.meetupproject.presentation.viewmodel.EventViewModel
import com.eugene.meetupproject.presentation.viewmodel.RoomViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(vm: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    abstract fun bindEventViewModel(vm: EventViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(RoomViewModel::class)
    abstract fun bindRoomViewModel(vm: RoomViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}