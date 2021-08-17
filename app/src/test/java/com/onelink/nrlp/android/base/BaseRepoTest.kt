package com.onelink.nrlp.android.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.onelink.nrlp.android.data.NetworkHelper
import com.onelink.nrlp.android.data.ServiceGateway
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
abstract class BaseRepoTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    protected lateinit var serviceGateway: ServiceGateway
    protected lateinit var networkHelper: NetworkHelper
    protected lateinit var disposable: Disposable

    @Before
    open fun setUp() {
        MockitoAnnotations.openMocks(this)
        fixSchedulers()
        serviceGateway = Mockito.mock(ServiceGateway::class.java)
        networkHelper = Mockito.mock(NetworkHelper::class.java)
        disposable = Mockito.mock(Disposable::class.java)
    }

    @After
    open fun tearDown() {
        networkHelper.disposable?.dispose()
        networkHelper.disposable = null
        if (this::disposable.isInitialized) disposable.dispose()
    }

    /**
     * AndroidSchedulers.mainThread() cannot be used in test environment.
     * If observeOn is set to {@link AndroidSchedulers.mainThread() Main Thread Scheduler} then callbacks will not be executed in test.
     * So need to replace it.
     * https://stackoverflow.com/questions/43356314/android-rxjava-2-junit-test-getmainlooper-in-android-os-looper-not-mocked-runt
     */
    private fun fixSchedulers() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }


}