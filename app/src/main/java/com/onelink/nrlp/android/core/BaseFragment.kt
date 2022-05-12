package com.onelink.nrlp.android.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.ErrorCodesConstants
import com.onelink.nrlp.android.utils.ErrorDialogConstants
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.toSpanned
import dagger.android.support.DaggerFragment

/**
 * Created by Umar Javed.
 */

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>(private val viewModelClass: Class<VM>) :
    DaggerFragment(), OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    lateinit var viewModel: VM
    open lateinit var binding: DB

    private fun init(inflater: LayoutInflater, container: ViewGroup) {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
    }

    open fun init(savedInstanceState: Bundle?) {}

    @LayoutRes
    abstract fun getLayoutRes(): Int

    open fun getViewM(): VM = ViewModelProvider(this).get(viewModelClass)

    open fun onInject() {}

    lateinit var fragmentHelper: FragmentNavigationHelper

    override fun onAttach(context: Context) {

        try {
            fragmentHelper = context as FragmentNavigationHelper
        } catch (e: Exception) {
            throw e
        }

        onInject()
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewM()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init(inflater, container!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    open fun refresh() {}

    open fun getTitle(): String {
        return ""
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    protected fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    protected fun showKeyboard() {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    protected fun showGeneralErrorDialog(fragment: Fragment, error: BaseError?) {
        error?.let {
            it.errorCode?.let { errorCode ->
                when(errorCode){
                    ErrorCodesConstants.NO_INTERNET_CONNECTION -> showNoInternetConnectionDialog(fragment)
                    ErrorCodesConstants.SESSION_EXPIRED -> showSessionExpiredDialog(fragment)
                    ErrorCodesConstants.UNSUCCESSFUL_TRANSACTION_FETCH -> showTransactionFetchUnsuccessfulDialog(fragment)
                    ErrorCodesConstants.INCORRECT_INFORMATION -> showIncorrectInformationDialog(fragment)
                    ErrorCodesConstants.ATTEMPTS_EXCEEDED -> showAttemptsExceededDialog(fragment)
                    ErrorCodesConstants.PROFILE_VERIFICATION_FAILED -> showProfileUpdateVerificationFailedDialog(fragment)
                    ErrorCodesConstants.NEW_VERSION -> showNewVersionAvailableDialog(fragment)
                    ErrorCodesConstants.REMITTANCE_OLDER_DATE -> showRemittanceOlderDateDialog(fragment)
                    ErrorCodesConstants.REMITTANCE_SAME_DATE -> showRemittanceSameDateDialog(fragment)
                    ErrorCodesConstants.REMITTANCE_RDA_TRANSACTION -> showRDATransactionDialog(fragment)
                    else -> showRemoteErrorDialog(fragment, it)
                }
            }
        }
    }
    protected fun showGeneralAlertDialog(fragment: Fragment, alert: String, msg: String) {
        when(alert) {
            "Register" -> showRegisterHelpDialog(fragment,msg)
            "SelfAward" -> showSelfAwardHelpDialog(fragment,msg)
            "USD" -> showUSDHelpDialog(fragment,msg)
        }
    }

    private fun showNoInternetConnectionDialog(fragment: Fragment){
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(fragment, ErrorDialogConstants.RC_NO_INTERNET_CONNECTION_DIALOG)
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_internet)
            .setTitle(getString(R.string.error_no_internet_title))
            .setMessage(getString(R.string.error_no_internet_text).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_NO_INTERNET_CONNECTION_DIALOG)
    }

    private fun showRegisterHelpDialog(fragment: Fragment,msg: String){
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(fragment, ErrorDialogConstants.RC_NO_INTERNET_CONNECTION_DIALOG)
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_register)
            .setMessage(msg.toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_NO_INTERNET_CONNECTION_DIALOG)
    }

    private fun showSelfAwardHelpDialog(fragment: Fragment,msg: String){
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(fragment, ErrorDialogConstants.RC_NO_INTERNET_CONNECTION_DIALOG)
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_self_award_points_update)
            .setMessage(msg.toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_NO_INTERNET_CONNECTION_DIALOG)
    }

    private fun showUSDHelpDialog(fragment: Fragment,msg: String){
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(fragment, ErrorDialogConstants.RC_NO_INTERNET_CONNECTION_DIALOG)
            .setIsAlertOnly(true)
            .setTitle(msg)
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_NO_INTERNET_CONNECTION_DIALOG)
    }

    private fun showRemoteErrorDialog(fragment: Fragment, error: BaseError){
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(fragment, ErrorDialogConstants.RC_GENERAL_SERVER_ERROR_DIALOG_CODE)
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap)
            .setTitle(getString(R.string.oh_snap))
            .setMessage((error.message ?: resources.getString(R.string.something_went_wrong)).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_GENERAL_SERVER_ERROR_DIALOG_CODE)
    }

    private fun showSessionExpiredDialog(fragment: Fragment){
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(fragment, ErrorDialogConstants.RC_SESSION_EXPIRED)
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap)
            .setTitle(getString(R.string.oh_snap))
            .setMessage(getString(R.string.error_session_expired).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_SESSION_EXPIRED)
    }

    private fun showNewVersionAvailableDialog(fragment: Fragment){
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(fragment, ErrorDialogConstants.RC_NEW_VERSION)
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap)
            .setTitle(getString(R.string.oh_snap))
            .setMessage(getString(R.string.error_new_version).toSpanned())
            .setNeutralButtonText(getString(R.string.proceed_to_playstore))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_SESSION_EXPIRED)
    }

    private fun showProfileUpdateVerificationFailedDialog(fragment: Fragment){
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(fragment, ErrorDialogConstants.RC_SESSION_EXPIRED)
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap)
            .setTitle(getString(R.string.oh_snap))
            .setMessage(getString(R.string.error_profile_verification_failed).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_SESSION_EXPIRED)
    }

    private fun showTransactionFetchUnsuccessfulDialog(fragment: Fragment) {
        OneLinkAlertDialogsFragment.Builder()
            //.setTargetFragment(fragment, ErrorDialogConstants.RC_SESSION_EXPIRED)
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap)
            .setTitle(getString(R.string.oh_snap))
            .setMessage(getString(R.string.error_transaction_fetch).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_SESSION_EXPIRED)
    }

    private fun showIncorrectInformationDialog(fragment: Fragment){
        OneLinkAlertDialogsFragment.Builder()
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap)
            .setTitle(getString(R.string.oh_snap))
            .setMessage(getString(R.string.incorrect_information).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_GENERAL_SERVER_ERROR_DIALOG_CODE)
    }

    private fun showAttemptsExceededDialog(fragment: Fragment){
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(fragment, ErrorDialogConstants.RC_SESSION_EXPIRED)
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap)
            .setTitle(getString(R.string.oh_snap))
            .setMessage(getString(R.string.attempts_exceeded).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_SESSION_EXPIRED)
    }

    private fun showRemittanceOlderDateDialog(fragment: Fragment){
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(fragment, ErrorDialogConstants.REMITTANCE_DIALOG)
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap)
            .setTitle(getString(R.string.oh_snap))
            .setMessage(getString(R.string.error_remittance_sent_prior).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_NO_INTERNET_CONNECTION_DIALOG)
    }

    private fun showRemittanceSameDateDialog(fragment: Fragment){
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(fragment, ErrorDialogConstants.REMITTANCE_DIALOG)
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap)
            .setTitle(getString(R.string.oh_snap))
            .setMessage(getString(R.string.error_remittance_same_date).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_NO_INTERNET_CONNECTION_DIALOG)
    }

    private fun showRDATransactionDialog(fragment: Fragment){
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(fragment, ErrorDialogConstants.REMITTANCE_DIALOG)
            .setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_oh_snap)
            .setTitle(getString(R.string.oh_snap))
            .setMessage(getString(R.string.error_remittance_rda_transaction).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setNegativeButtonText("")
            .setPositiveButtonText("")
            .setCancelable(false)
            .show(parentFragmentManager, ErrorDialogConstants.TAG_NO_INTERNET_CONNECTION_DIALOG)
    }

    override fun onNeutralButtonClicked(targetCode: Int) {
        when(targetCode){
            ErrorDialogConstants.RC_SESSION_EXPIRED -> {
                activity?.let {
                    UserData.emptyUserData()
                    val intent = LoginActivity.newLoginIntent(it)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    it.startActivity(intent)
                }
            }
            ErrorDialogConstants.RC_NEW_VERSION -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PLAY_STORE_NRLP_URL))
                startActivity(intent)
            }
        }
    }

    override fun onPositiveButtonClicked(targetCode: Int) {

    }

    override fun onNegativeButtonClicked(targetCode: Int) {

    }

    interface FragmentNavigationHelper {

        fun addFragment(fragment: Fragment, clearBackStack: Boolean, addToBackStack: Boolean)

        fun addFragment(fragment: Fragment, layoutId: Int, clearBackStack: Boolean, addToBackStack: Boolean)

        fun replaceFragment(fragment: Fragment, clearBackStack: Boolean, addToBackStack: Boolean)

        fun replaceFragment(fragment: Fragment, layoutId: Int, clearBackStack: Boolean, addToBackStack: Boolean)

        fun onBack()

        fun getCurrentFragment(): Fragment?
    }
}
