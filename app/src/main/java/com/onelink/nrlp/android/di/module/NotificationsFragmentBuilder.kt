package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.appnotification.fragments.NotificationActivityFragment
import com.onelink.nrlp.android.features.appnotification.fragments.NotificationAnnouncementFragment
import com.onelink.nrlp.android.features.appnotification.fragments.NotificationComplaintFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NotificationsFragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideNotificationComplaintFragment(): NotificationComplaintFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideNotificationActivityFragment(): NotificationActivityFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideNotificationAnnouncementFragment(): NotificationAnnouncementFragment
}