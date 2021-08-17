package com.onelink.nrlp.android.utils

import android.os.CountDownTimer
import android.os.SystemClock

abstract class CountDownTimerCanBePause(
    millisInFuture: Long,
    countDownInterval: Long
) {
    private var millisInFuture: Long = 0
    var countDownInterval: Long = 0
    var millisRemaining: Long = 0
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var countDownTimer: CountDownTimer? = null
    private var isPaused = true
    var initializingTimer = true
    var isFinished = false
    private fun createCountDownTimer(millis: Long) {
        countDownTimer = object : CountDownTimer(millis, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                millisRemaining = millisUntilFinished
                this@CountDownTimerCanBePause.onTick(millisUntilFinished)
            }

            override fun onFinish() {
                if (initializingTimer) {
                    initializingTimer = false
                } else {
                    isFinished = true
                    this@CountDownTimerCanBePause.onFinish()
                }
            }
        }
    }

    abstract fun onTick(millisUntilFinished: Long)
    abstract fun onFinish()
    fun cancel() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
        millisRemaining = 0
    }

    /**
     * Start or Resume the countdown.
     *
     */
    @Synchronized
    fun start(): CountDownTimerCanBePause {
        if (!isFinished) {
            if (isPaused) {
                endTime = SystemClock.elapsedRealtime()
                val elapsedMilliSeconds = endTime - startTime
                createCountDownTimer(millisRemaining - elapsedMilliSeconds)
                isPaused = false
            } else {
                reset()
            }
            countDownTimer!!.start()
        }
        return this
    }

    @Synchronized
    fun reset(): CountDownTimerCanBePause {
        isPaused = false
        countDownTimer?.cancel()
        millisRemaining = millisInFuture
        createCountDownTimer(millisRemaining)
        countDownTimer?.start()
        return this
    }

    /**
     * Pauses the CountDownTimerPausable, so it could be resumed(start)
     * later from the same point where it was paused.
     */
    @Throws(IllegalStateException::class)
    fun pause() {
        if (!isFinished) {
            startTime = SystemClock.elapsedRealtime()
            if (!isPaused) {
                countDownTimer!!.cancel()
            } else {
                throw IllegalStateException("CountDownTimerPausable is already in pause state, start counter before pausing it.")
            }
            isPaused = true
        }
    }

    init {
        this.millisInFuture = millisInFuture
        this.countDownInterval = countDownInterval
        millisRemaining = this.millisInFuture
    }
}