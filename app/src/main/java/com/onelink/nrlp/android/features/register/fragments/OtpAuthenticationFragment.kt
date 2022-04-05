package com.onelink.nrlp.android.features.register.fragments

import android.annotation.SuppressLint
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
import com.onelink.nrlp.android.databinding.FragmentOtpAuthenticationBinding
import com.onelink.nrlp.android.features.register.models.RegisterFlowDataModel
import com.onelink.nrlp.android.features.register.models.ResendOTPRequest
import com.onelink.nrlp.android.features.register.models.VerifyOTPRequest
import com.onelink.nrlp.android.features.register.viewmodel.OtpAuthenticationFragmentViewModel
import com.onelink.nrlp.android.features.register.viewmodel.SharedViewModel
import com.onelink.nrlp.android.utils.CountDownTimerCanBePause
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.formattedCountDownTimer
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject


const val FIVE_MINUTE_TIMER_MILLIS = 5 * 60 * 1000L
const val TIMER_MILLIS = 60000L
const val TIMER_INTERVAL = 1000L
const val RESEND_OTP_CARD_VISIBILITY_TIMER = 10 * 1000L

class OtpAuthenticationFragment :
    BaseFragment<OtpAuthenticationFragmentViewModel, FragmentOtpAuthenticationBinding>(
        OtpAuthenticationFragmentViewModel::class.java
    ) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var sharedViewModel: SharedViewModel? = null

    private lateinit var registerFlowDataModel: RegisterFlowDataModel

    var otpAttempts: Int = 0

    private val timer = object : CountDownTimer(TIMER_MILLIS, TIMER_INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            binding.textViewResendOTP.isEnabled = false
        }

        override fun onFinish() {
            //if (otpAttempts < RETRIES_COUNT) {
            binding.textViewResendOTP.isEnabled = true
            otpAttempts++
            //}
        }
    }

    private val fiveMinuteTimer =
        object : CountDownTimerCanBePause(FIVE_MINUTE_TIMER_MILLIS, TIMER_INTERVAL) {
            @SuppressLint("DefaultLocale")
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

    override fun getLayoutRes() = R.layout.fragment_otp_authentication

    override fun getViewM(): OtpAuthenticationFragmentViewModel = ViewModelProvider(
        this, viewModelFactory
    ).get(OtpAuthenticationFragmentViewModel::class.java)

    override fun getTitle(): String = resources.getString(R.string.otp_verification_title)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            sharedViewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
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
            sharedViewModel?.setRegisterFlowDataModel(
                RegisterFlowDataModel(
                    fullName = registerFlowDataModel.fullName,
                    cnicNicop = registerFlowDataModel.cnicNicop,
                    phoneNumber = registerFlowDataModel.phoneNumber,
                    email = registerFlowDataModel.email,
                    country = registerFlowDataModel.country,
                    residentId = registerFlowDataModel.residentId,
                    passportType = registerFlowDataModel.passportType,
                    passportId = registerFlowDataModel.passportId,
                    password = registerFlowDataModel.password,
                    rePassword = registerFlowDataModel.rePassword,
                    accountType = registerFlowDataModel.accountType,
                    referenceNumber = registerFlowDataModel.referenceNumber,
                    transactionAmount = registerFlowDataModel.transactionAmount,
                    registrationCode = "",
                    otpCode = viewModel.getOTPCode(),
                    motherMaidenName = registerFlowDataModel.motherMaidenName,
                    placeOfBirth =  registerFlowDataModel.placeOfBirth,
                    cnicNicopIssueDate = registerFlowDataModel.cnicNicopIssueDate,
                    fatherName = registerFlowDataModel.fatherName
                )
            )

            viewModel.verifyOTP(
                VerifyOTPRequest(
                    nicNicop = registerFlowDataModel.cnicNicop,
                    userType = registerFlowDataModel.accountType,
                    otp = registerFlowDataModel.otpCode
                )
            )
        }

        binding.textViewResendOTP.setOnClickListener {
            viewModel.resendOTP(
                ResendOTPRequest(
                    nicNicop = registerFlowDataModel.cnicNicop,
                    userType = registerFlowDataModel.accountType,
                    retryCount = otpAttempts
                )
            )
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
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && etCurrent.hasFocus()) {
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
        viewModel.observeVerifyOTP().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        fragmentHelper.addFragment(
                            TermsAndConditionsFragment(),
                            clearBackStack = false,
                            addToBackStack = true
                        )
                        hideKeyboard()
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

        viewModel.observeResendOTP().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        fiveMinuteTimer.reset()
                        timer.start()
                        showNewOTPSentCard()
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

        sharedViewModel?.registerFlowDataModel?.observe(this, Observer {
            registerFlowDataModel = it
            val str = String.format(
                getString(R.string.otp_sent_phone_number_msg), registerFlowDataModel.phoneNumber.replace("+", "")
            )
            binding.tvOtpSentMsg.text = str
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
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvSentNewOtpMsg.visibility = View.GONE
        }, RESEND_OTP_CARD_VISIBILITY_TIMER)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        fiveMinuteTimer.cancel()
    }

    companion object {
        @JvmStatic
        fun newInstance() = OtpAuthenticationFragment()
    }
}