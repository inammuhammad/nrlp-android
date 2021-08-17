package com.onelink.nrlp.android.features.uuid.view

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentLoginOtpAuthenticationBinding
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.uuid.viewmodel.LoginOtpFragmentViewModel
import com.onelink.nrlp.android.features.uuid.viewmodel.UUIDSharedViewModel
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


const val TIMER_MILLIS = 60000L
const val FIVE_MINUTE_TIMER_MILLIS = 5 * 60 * 1000L
const val TIMER_INTERVAL = 1000L
const val RESEND_OTP_CARD_VISIBILITY_TIMER = 10 * 1000L
//const val RETRIES_COUNT = 2


class UUIDOtpAuthentication :
    BaseFragment<LoginOtpFragmentViewModel, FragmentLoginOtpAuthenticationBinding>(
        LoginOtpFragmentViewModel::class.java
    ) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var resentAttempts: Int = 0

    private lateinit var uuidSharedViewModel: UUIDSharedViewModel

    private val timer = object : CountDownTimer(
        TIMER_MILLIS, TIMER_INTERVAL
    ) {
        override fun onTick(millisUntilFinished: Long) {
            binding.textViewResendOTP.isEnabled = false
        }

        override fun onFinish() {
            //if (resentAttempts < RETRIES_COUNT) {
            binding.textViewResendOTP.isEnabled = true
            resentAttempts++
            //}
        }
    }

    private val fiveMinuteTimer = object : CountDownTimerCanBePause(
        FIVE_MINUTE_TIMER_MILLIS, TIMER_INTERVAL
    ) {
        override fun onTick(millisUntilFinished: Long) {
            binding.textViewTimer.text = formattedCountDownTimer(millisUntilFinished)
        }

        override fun onFinish() {
            binding.textViewTimer.text = getString(R.string.timer_default)
        }
    }


    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_login_otp_authentication

    override fun getViewM(): LoginOtpFragmentViewModel = ViewModelProvider(
        this, viewModelFactory
    ).get(LoginOtpFragmentViewModel::class.java)

    override fun getTitle(): String = getString(R.string.device_change)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            uuidSharedViewModel = ViewModelProvider(it).get(UUIDSharedViewModel::class.java)
        }

        fiveMinuteTimer.start()
        timer.start()
        initObservers()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        fiveMinuteTimer.start()
    }

    override fun onPause() {
        super.onPause()
        fiveMinuteTimer.pause()
    }

    private fun initListeners() {
        binding.etOTP1.requestFocus()
        showKeyboard()
        initEditTextListeners()

        binding.btnNext.setOnSingleClickListener {
            viewModel.updateUUIDCall()
        }

        binding.textViewResendOTP.setOnSingleClickListener {
            viewModel.resendOTP()
        }
    }

    private fun initEditTextListeners() {
        binding.etOTP1.setOnKeyListener(onKeyListenerOTP(binding.etOTP1, binding.etOTP1))
        binding.etOTP2.setOnKeyListener(onKeyListenerOTP(binding.etOTP2, binding.etOTP1))
        binding.etOTP3.setOnKeyListener(onKeyListenerOTP(binding.etOTP3, binding.etOTP2))
        binding.etOTP4.setOnKeyListener(onKeyListenerOTP(binding.etOTP4, binding.etOTP3))
    }

    private fun onKeyListenerOTP(etCurrent: EditText, etPrevious: EditText): View.OnKeyListener {
        return View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL
                && event.action == KeyEvent.ACTION_DOWN
                && etCurrent.hasFocus()
            ) {
                if (etCurrent.text.toString().length == 1) {
                    etCurrent.setText("")
                    etCurrent.requestFocus()
                } else if (etCurrent.text.toString().isEmpty()) {
                    etPrevious.setText("")
                    etPrevious.requestFocus()
                }
                return@OnKeyListener true
            }
            false
        }
    }

    private fun initObservers() {
        uuidSharedViewModel.loginCredentials.observe(this, Observer {
            viewModel.loginCredentials = it
        })
        viewModel.observeUpdateUUIDResponse().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    showDeviceVerifiedSuccessDialog()
                }
                Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.error?.let {
                        showGeneralErrorDialog(this, it)
                    }
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(activity)
                }
            }
        })

        viewModel.observeResendOTP().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    activity?.let {
                        showNewOTPSentCard()
                        timer.start()
                        fiveMinuteTimer.reset()
                    }
                }
                Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.error?.let {
                        showGeneralErrorDialog(this, it)
                    }
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(activity)
                }
            }
        })

        viewModel.validEditText1.observe(this, Observer {
            if (it) binding.etOTP2.requestFocus()
        })
        viewModel.validEditText2.observe(this, Observer {
            if (it) binding.etOTP3.requestFocus()
        })
        viewModel.validEditText3.observe(this, Observer {
            if (it) binding.etOTP4.requestFocus()
        })
        viewModel.validEditText4.observe(this, Observer {
            if (it) hideKeyboard()
        })
    }

    private fun showNewOTPSentCard() {
        binding.tvSentNewOtpMsg.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.tvSentNewOtpMsg.visibility = View.GONE
            }, RESEND_OTP_CARD_VISIBILITY_TIMER
        )
    }

    companion object {
        private const val TAG = "uuidotp.fragment"
        private const val RC_DEVICE_VERIFIED = 0x321

        @JvmStatic
        fun newInstance() = UUIDOtpAuthentication()
    }

    private fun showDeviceVerifiedSuccessDialog() {
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(this, RC_DEVICE_VERIFIED)
            .setIsAlertOnly(true).setDrawable(R.drawable.ic_uuid_success_dialog)
            .setTitle(getString(R.string.uuid_change_success_title))
            .setMessage(getString(R.string.uuid_change_success_message).toSpanned())
            .setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false).show(parentFragmentManager, TAG)
    }

    override fun onNeutralButtonClicked(targetCode: Int) {
        super.onNeutralButtonClicked(targetCode)
        when (targetCode) {
            RC_DEVICE_VERIFIED -> {
                activity?.let {
                    val intent = LoginActivity.newLoginIntent(it)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        fiveMinuteTimer.cancel()
    }
}
