package com.onelink.nrlp.android.core

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.utils.ErrorCodesConstants
import com.onelink.nrlp.android.utils.ErrorDialogConstants
import com.onelink.nrlp.android.utils.LocaleManager
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.toSpanned
import dagger.android.support.DaggerAppCompatActivity

/**
 * Created by Umar Javed.
 */

abstract class BaseActivity<DB : ViewDataBinding, VM : BaseViewModel>(mViewModelClass: Class<VM>) :
    DaggerAppCompatActivity(), OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    @LayoutRes
    abstract fun getLayoutRes(): Int

    lateinit var binding: DB

    val viewModel by lazy {
        ViewModelProvider(this, getVMFactory()).get(mViewModelClass)
    }

    abstract fun getVMFactory(): ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleManager.updateBaseContext(this)
        binding = DataBindingUtil.setContentView(this, getLayoutRes())
        window.statusBarColor = Color.WHITE
        initViewModel(viewModel)
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
    }

    /**
     *  You need override this method.
     *  And you need to set viewModel to binding: binding.viewModel = viewModel
     */
    abstract fun initViewModel(viewModel: VM)

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base?.let { LocaleManager.updateBaseContext(it) })
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        LocaleManager.updateBaseContext(this)
    }

    fun hideKeyboard() {
        currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    protected fun showGeneralErrorDialog(error: BaseError?) {
        error?.let {
            it.errorCode?.let { errorCode ->
                when (errorCode) {
                    ErrorCodesConstants.NO_INTERNET_CONNECTION -> showNoInternetConnectionDialog()
                    ErrorCodesConstants.SESSION_EXPIRED -> showSessionExpiredDialog()
                    ErrorCodesConstants.UNSUCCESSFUL_TRANSACTION_FETCH -> showTransactionFetchUnsuccessfulDialog()
                    else -> showRemoteErrorDialog(it)
                }
            }
        }
    }

    private fun showNoInternetConnectionDialog() {
        OneLinkAlertDialogsFragment.Builder().setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_internet)
            .setTitle(getString(R.string.error_no_internet_title))
            .setMessage(getString(R.string.error_no_internet_text).toSpanned())
            .setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false)
            .show(supportFragmentManager, ErrorDialogConstants.TAG_NO_INTERNET_CONNECTION_DIALOG)
    }

    private fun showRemoteErrorDialog(error: BaseError) {
        OneLinkAlertDialogsFragment.Builder().setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap).setTitle(getString(R.string.oh_snap)).setMessage(
                (error.message
                    ?: resources.getString(R.string.something_went_wrong)).toSpanned()
            ).setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false)
            .show(supportFragmentManager, ErrorDialogConstants.TAG_GENERAL_SERVER_ERROR_DIALOG_CODE)
    }

    private fun showSessionExpiredDialog() {
        OneLinkAlertDialogsFragment.Builder().setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap).setTitle(getString(R.string.oh_snap))
            .setMessage(getString(R.string.error_session_expired).toSpanned())
            .setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false)
            .show(supportFragmentManager, ErrorDialogConstants.TAG_SESSION_EXPIRED)
    }

    private fun showTransactionFetchUnsuccessfulDialog() {
        OneLinkAlertDialogsFragment.Builder().setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap).setTitle(getString(R.string.oh_snap))
            .setMessage(getString(R.string.error_transaction_fetch).toSpanned())
            .setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false)
            .show(supportFragmentManager, ErrorDialogConstants.TAG_SESSION_EXPIRED)
    }

    override fun onNeutralButtonClicked(targetCode: Int) {
        when (targetCode) {
            ErrorDialogConstants.RC_SESSION_EXPIRED -> {
                UserData.emptyUserData()
                val intent = LoginActivity.newLoginIntent(this)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    override fun onPositiveButtonClicked(targetCode: Int) {

    }

    override fun onNegativeButtonClicked(targetCode: Int) {

    }
}
