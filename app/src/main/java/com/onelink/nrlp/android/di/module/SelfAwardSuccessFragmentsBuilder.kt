package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.rate.fragments.TransactionRatingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class SelfAwardSuccessFragmentsBuilder {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideTransactionRatingFragment(): TransactionRatingFragment
}