package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.appnotification.fragments.NotificationComplaintFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NotificationsFragmentBuilder {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideNotificationComplaintFragment(): NotificationComplaintFragment
}