package com.onelink.nrlp.android.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.onelink.nrlp.android.core.BaseError
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.data.interceptors.NoConnectivityException
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.ErrorCodesConstants
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Umar Javed.
 */

open class NetworkHelper @Inject constructor() {

    var disposable: Disposable? = null

    fun <T> serviceCall(call: Single<Response<T>>): LiveData<BaseResponse<T>> {

        val responseData = MutableLiveData<BaseResponse<T>>()

        call.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                responseData.value = BaseResponse.loading(null)
            }
            .subscribe(object : SingleObserver<Response<T>> {
                override fun onSuccess(response: Response<T>) {

                    if (response.isSuccessful) {
                        response.body()?.let {
                            responseData.value = BaseResponse.success(it, code = response.code())
                        } ?: run {
                            responseData.value = BaseResponse.error(
                                Constants.SOMETHING_WENT_WRONG,
                                null,
                                BaseError(
                                    message = Constants.SOMETHING_WENT_WRONG,
                                    errorCode = ErrorCodesConstants.SERVER_ERROR
                                ),
                                code = response.code()
                            )
                        }
                    } else {
                        response.errorBody()?.let {
                            try {
                                val error = Gson().fromJson(it.string(), BaseError::class.java)
                                if (error.errorCode == null)
                                    error.errorCode = ErrorCodesConstants.SERVER_ERROR
                                responseData.value = BaseResponse.error(
                                    response.message(),
                                    null,
                                    error = error,
                                    code = response.code()
                                )
                            } catch (e: Exception) {
                                responseData.value = BaseResponse.error(
                                    Constants.SOMETHING_WENT_WRONG,
                                    null,
                                    BaseError(
                                        message = Constants.SOMETHING_WENT_WRONG,
                                        errorCode = ErrorCodesConstants.SERVER_ERROR
                                    ),
                                    code = response.code()
                                )
                            }
                        } ?: run {
                            responseData.value = BaseResponse.error(
                                Constants.SOMETHING_WENT_WRONG,
                                null,
                                BaseError(
                                    message = Constants.SOMETHING_WENT_WRONG,
                                    errorCode = ErrorCodesConstants.SERVER_ERROR
                                ),
                                code = response.code()
                            )
                        }
                    }
                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onError(t: Throwable) {
                    if (t is NoConnectivityException) {
                        val error = "No Internet Connection"
                        responseData.value = BaseResponse.error(
                            error,
                            null,
                            BaseError(
                                message = error,
                                errorCode = ErrorCodesConstants.NO_INTERNET_CONNECTION
                            )
                        )
                        return
                    }
                    t.message?.let {
                        responseData.value = BaseResponse.error(
                            it,
                            null,
                            BaseError(
                                message = Constants.SOMETHING_WENT_WRONG,
                                errorCode = ErrorCodesConstants.SERVER_ERROR
                            )
                        )
                    }
                }
            })

        return responseData
    }

    fun dispose() {
        disposable?.let {
            if (!it.isDisposed)
                it.dispose()
        }
    }
}
