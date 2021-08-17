package com.onelink.nrlp.android

import android.content.Intent
import android.content.res.Configuration
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.di.AppComponent
import com.onelink.nrlp.android.di.DaggerAppComponent
import com.onelink.nrlp.android.features.splash.view.SplashActivity
import com.onelink.nrlp.android.utils.LocaleManager.updateBaseContext
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class OneLinkApplication : DaggerApplication(), LifecycleObserver {
    private var countDownTimer: CountDownTimer? = null

    var timeInterval: Long = 1000
    var timeElapsed: Long = System.currentTimeMillis()

    private lateinit var appComponent: AppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val androidInjector = DaggerAppComponent
            .builder().create(this)
        appComponent = androidInjector as AppComponent
        return androidInjector
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun startCounter() {
        val countDownTime: Long = UserData.getUser()?.inActiveTime ?: 80 * 1000 * 60
        countDownTimer = object : CountDownTimer(countDownTime, timeInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeElapsed = millisUntilFinished
            }

            override fun onFinish() {
                timeElapsed = 0 // for older devices
            }
        }
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onAppBackgrounded() {
        timeElapsed = System.currentTimeMillis()
        countDownTimer?.start()
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForegrounded() {
        countDownTimer?.cancel()
        if (timeElapsed / timeInterval == 0L) {
            val splashIntent = Intent(this, SplashActivity::class.java)
            splashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(splashIntent)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateBaseContext(baseContext)
    }
}