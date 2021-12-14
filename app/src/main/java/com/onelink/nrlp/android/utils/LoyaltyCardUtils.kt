package com.onelink.nrlp.android.utils

import android.content.Context
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.onelink.nrlp.android.R
import java.util.*

/**
 * Created by Qazi Abubakar
 */

fun ImageView.setLoyaltyCardBackground(context: Context, loyaltyLevel: String?) {
    this.background = ContextCompat.getDrawable(context, getLoyaltyCardBackground(loyaltyLevel))
}

fun ConstraintLayout.setLoyaltyCard(context: Context, loyaltyLevel: String?) {
    this.background = ContextCompat.getDrawable(context, getLoyaltyCardBackground(loyaltyLevel))
}

fun ImageView.setLoyaltyTitleBackground(context: Context, loyaltyLevel: String?) {
    this.background = ContextCompat.getDrawable(context, getLoyaltyTitleBackground(loyaltyLevel))
}

fun getLoyaltyCardBackground(loyaltyLevel: String?): Int{
    return when (loyaltyLevel?.toLowerCase(Locale.getDefault())){
       /* LoyaltyCardConstants.BRONZE -> R.drawable.ic_loyalty_points_bronze
        LoyaltyCardConstants.GOLD -> R.drawable.ic_loyalty_points_gold
        LoyaltyCardConstants.PLATINUM -> R.drawable.ic_loyalty_points_platinum
        LoyaltyCardConstants.SILVER -> R.drawable.ic_loyalty_points_silver
        else -> R.drawable.ic_loyalty_points_bronze*/
        LoyaltyCardConstants.BRONZE -> R.drawable.green_card
        LoyaltyCardConstants.GOLD -> R.drawable.gold_card
        LoyaltyCardConstants.PLATINUM -> R.drawable.platinum_card
        LoyaltyCardConstants.SILVER -> R.drawable.green_card
        else -> R.drawable.green_card
    }
}

fun getLoyaltyTitleBackground(loyaltyLevel: String?): Int{
    return when(loyaltyLevel?.toLowerCase(Locale.getDefault())) {
        LoyaltyCardConstants.BRONZE -> R.drawable.ic_title_bronze
        LoyaltyCardConstants.GOLD -> R.drawable.ic_title_gold
        LoyaltyCardConstants.PLATINUM -> R.drawable.ic_title_platinum
        LoyaltyCardConstants.SILVER -> R.drawable.ic_title_silver
        else -> R.drawable.ic_title_bronze
    }
}