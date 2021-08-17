package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.viewStatement.fragments.AdvancedLoyaltyStatementFragment
import com.onelink.nrlp.android.features.viewStatement.fragments.LoyaltyStatementFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Qazi Abubakar on 14/07/2020.
 */
@Suppress("unused")
@Module
abstract class ViewStatementFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideLoyaltyStatementFragment(): LoyaltyStatementFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideAdvancedLoyaltyStatementFragment(): AdvancedLoyaltyStatementFragment
}
