package com.onelink.nrlp.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.core.Status
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Suppress("unused")
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            if (o is BaseResponse<*>){
                if (o.status==Status.LOADING){
                    /*do nothing on loading because data is not arrived yet.
                    Status loading is called when someone subscribing to task, to see it Open NetworkHelper
                     */
                    return
                }
            }
            data = o
            latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}