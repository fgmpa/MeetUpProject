package com.eugene.meetupproject.di

import android.app.Application
import com.eugene.meetupproject.presentation.CalendarFragment
import com.eugene.meetupproject.presentation.EventListActivity
import com.eugene.meetupproject.presentation.EventValuesActivity
import com.eugene.meetupproject.presentation.LoginActivity
import com.eugene.meetupproject.presentation.MeetingListFragment
import com.eugene.meetupproject.presentation.RoomListActivity
import com.eugene.meetupproject.presentation.RoomValuesActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DataModule::class,
        DomainModule::class,
        ViewModelModule::class,
        AppModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun inject(activity: LoginActivity)
    fun inject(activity: EventListActivity)
    fun inject(fragment: CalendarFragment)
    fun inject(fragment: MeetingListFragment)
    fun inject(activity: EventValuesActivity)
    fun inject(activity: RoomListActivity)
    fun inject(activity: RoomValuesActivity)
}