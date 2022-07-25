package com.onelink.nrlp.android.features.splash.view

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.SplashActivityBinding
import com.onelink.nrlp.android.features.language.view.LanguageActivity
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.splash.viewmodel.SplashViewModel
import com.onelink.nrlp.android.utils.*
import javax.inject.Inject

/**
 * Created by Umar Javed.
 */

const val SPLASH_TIME_OUT = 3000L

class SplashActivity : BaseFragmentActivity<SplashActivityBinding, SplashViewModel>(SplashViewModel::class.java) {

    companion object{
        fun start(activity:Activity){
            activity.startActivity(Intent(activity,SplashActivity::class.java))
            activity.finishAffinity()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.splash_activity

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: SplashViewModel) {
        viewModel.clearUserData()
        viewModel.updateCheckSum(Constants.checkSum)    //(SgTils.getSingInfo(this))
        //viewModel.updateCheckSum("446fd81a1ac6cccb861025481b448c345d0084d1")
        Handler(Looper.getMainLooper()).postDelayed({
            if (LocaleManager.getLanguageBoolPref(this)!!) {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            } else LanguageActivity.start(this, true)


        }, SPLASH_TIME_OUT)
    }
}