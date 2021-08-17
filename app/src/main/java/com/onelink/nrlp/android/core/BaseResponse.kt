package com.onelink.nrlp.android.core

import java.io.Serializable

/**
 * Created by Umar Javed.
 */

open class BaseResponse<T>(
    var status: Status,
    var data: T? = null,
    var error: BaseError? = null,
    var message: String? = null,
    var code: Int? = null
) {

    companion object {

        fun <T> success(data: T, code: Int? = null): BaseResponse<T> {
            return BaseResponse(Status.SUCCESS, data, null, null, code)
        }

        fun <T> error(
            msg: String,
            data: T?,
            error: BaseError? = null,
            code: Int? = null
        ): BaseResponse<T> {
            return BaseResponse(Status.ERROR, data, error, msg, code)
        }

        fun <T> loading(data: T?): BaseResponse<T> {
            return BaseResponse(Status.LOADING, data, null)
        }

    }

}

data class BaseError(
    var errorCode: String? = null,
    var message: String? = null
)

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}