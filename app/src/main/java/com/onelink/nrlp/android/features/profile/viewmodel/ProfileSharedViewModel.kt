package com.onelink.nrlp.android.features.profile.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.onelink.nrlp.android.core.BaseViewModel
import com.onelink.nrlp.android.features.profile.models.UpdateProfileRequestModel

/**
 * Created by Qazi Abubakar
 */
class ProfileSharedViewModel : BaseViewModel() {
    val updateProfileRequestModel = MutableLiveData<JsonObject>()
}