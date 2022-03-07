package com.onelink.nrlp.android.features.home.sidemenu

import android.content.Context
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.data.local.UserData
import java.math.BigInteger

/**
 * Created by Umar Javed.
 */

const val SIDE_MENU_KEY_PROFILE = 1
const val SIDE_MENU_KEY_CHANGE_PASSWORD = 2
const val SIDE_MENU_KEY_FAQS = 3
const val SIDE_MENU_KEY_CONTACT_US = 4
const val SIDE_MENU_KEY_LOGOUT = 5
const val SIDE_MENU_KEY_CHANGE_LANGAUGE = 6
const val SIDE_MENU_KEY_GUIDE = 7
const val SIDE_MENU_KEY_COMPLAINTS = 8
const val SIDE_MENU_KEY_RECEIVER_MANAGEMENT = 9

object SideMenuItemUtils {

    fun getSideMenuUIModelsList(context: Context, onMenuItemClicked: ((sideMenuOptionsItemModel: SideMenuOptionsItemModel) -> Unit)) : List<SideMenuUIModel>{
        val sideMenuUIModelsList = mutableListOf<SideMenuUIModel>()

        //add side menu header
        sideMenuUIModelsList.add(getSideMenuUiModelHeaderItem())

        //add side menu items
        val sideMenuItemsList = getSideMenuOptionItemsList(context)
        sideMenuItemsList.forEach {
            sideMenuUIModelsList.add(SideMenuOptionsModel(it, onMenuItemClicked))
        }

        return sideMenuUIModelsList
    }

    private fun getSideMenuUiModelHeaderItem() = SideMenuHeaderModel(UserData.getUser()?.fullName ?: "", UserData.getUser()?.cnicNicop ?: BigInteger.ZERO)

    //For now we have same side menu items for Remitter and Beneficiary
    private fun getSideMenuOptionItemsList(context: Context) : List<SideMenuOptionsItemModel>{
        val sideMenuItemsList = mutableListOf<SideMenuOptionsItemModel>()
        sideMenuItemsList.add(SideMenuOptionsItemModel(SIDE_MENU_KEY_PROFILE, context.resources.getString(R.string.profile), R.drawable.ic_side_menu_profile))
        sideMenuItemsList.add(SideMenuOptionsItemModel(SIDE_MENU_KEY_CHANGE_PASSWORD, context.resources.getString(R.string.change_password), R.drawable.ic_side_menu_change_psw))
        sideMenuItemsList.add(SideMenuOptionsItemModel(SIDE_MENU_KEY_RECEIVER_MANAGEMENT, context.resources.getString(R.string.remittance_receiver_manager), R.drawable.ic_rrm))
        sideMenuItemsList.add(SideMenuOptionsItemModel(SIDE_MENU_KEY_FAQS, context.resources.getString(R.string.faqs), R.drawable.ic_side_menu_faqs))
        sideMenuItemsList.add(SideMenuOptionsItemModel(SIDE_MENU_KEY_GUIDE, context.resources.getString(R.string.guide), R.drawable.ic_youtube_guide))
        sideMenuItemsList.add(SideMenuOptionsItemModel(SIDE_MENU_KEY_CONTACT_US, context.resources.getString(R.string.contact_us), R.drawable.ic_side_menu_contact))
        sideMenuItemsList.add(SideMenuOptionsItemModel(SIDE_MENU_KEY_COMPLAINTS, context.resources.getString(R.string.complaint_management), R.drawable.ic_side_menu_complaints))
        sideMenuItemsList.add(SideMenuOptionsItemModel(SIDE_MENU_KEY_CHANGE_LANGAUGE, context.resources.getString(R.string.language), R.drawable.ic_language))
        sideMenuItemsList.add(SideMenuOptionsItemModel(SIDE_MENU_KEY_LOGOUT, context.resources.getString(R.string.logout), R.drawable.ic_side_menu_logout))
        return sideMenuItemsList
    }
}
