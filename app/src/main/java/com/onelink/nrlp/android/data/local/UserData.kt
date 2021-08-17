package com.onelink.nrlp.android.data.local

object UserData {
    private var loggedInUser : UserModel? = null
    var finalEncryptionIV : String? = null
    var identityKey : String? = null
    var appChecksum : String? = null

    fun setUser(userModel: UserModel){
        loggedInUser = userModel
    }

    fun getUser() = loggedInUser

    fun emptyUserData(){
        identityKey = null
        loggedInUser = null
        finalEncryptionIV = null
    }
}