package com.onelink.nrlp.android.features.selfAwardPoints.view

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
import com.google.gson.JsonObject
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentSelfAwardPointsOtpBinding
import com.onelink.nrlp.android.features.redeem.view.RedeemSuccessActivity
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsOTPRequestModel
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsRequest
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsOTPFragmentViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsSharedViewModel
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


const val TIMER_MILLIS = 60000L
const val FIVE_MINUTE_TIMER_MILLIS = 5 * 60 * 1000L
const val TIMER_INTERVAL = 1000L
const val RESEND_OTP_CARD_VISIBILITY_TIMER = 10 * 1000L

@Suppress("unused")
const val RETRIES_COUNT = 2


class SelfAwardPointsOTPFragment :
    BaseFragment<SelfAwardPointsOTPFragmentViewModel, FragmentSelfAwardPointsOtpBinding>(
        SelfAwardPointsOTPFragmentViewModel::class.java
    ) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var selfAwardPointsSharedViewModel: SelfAwardPointsSharedViewModel? = null

    private lateinit var selfAwardPointsRequest: JsonObject

    var resentAttempts: Int = 0

    private val timer = object : CountDownTimer(TIMER_MILLIS, TIMER_INTERVAL) {
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

    override fun getLayoutRes() = R.layout.fragment_self_award_points_otp

    override fun getViewM(): SelfAwardPointsOTPFragmentViewModel = ViewModelProvider(this, viewModelFactory)
        .get(SelfAwardPointsOTPFragmentViewModel::class.java)

    override fun getTitle(): String = resources.getString(R.string.otp_verification_title)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            selfAwardPointsSharedViewModel =
                ViewModelProvider(it).get(SelfAwardPointsSharedViewModel::class.java)
        }

        timer.start()
        fiveMinuteTimer.start()
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
            viewModel.selfAwardPointsOTP(
                SelfAwardPointsOTPRequestModel(
//                    nicNicop = forgotPasswordFlowDataModel.cnicNicop,
//                    userType = forgotPasswordFlowDataModel.accountType,
                    otp = viewModel.getOTPCode(),
                    response_row_id = selfAwardPointsSharedViewModel?.selfAwardRowId?.value,
                    )
            )
        }

        binding.textViewResendOTP.setOnSingleClickListener {
            viewModel.getSelfAwardPointsResendOTP(selfAwardPointsRequest)
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

        selfAwardPointsSharedViewModel?.selfAwardPointsRequestModel?.observe(this, Observer{
            selfAwardPointsRequest = it
        })

        viewModel.observeSelfAwardPointsOTPResponse().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        oneLinkProgressDialog.hideProgressDialog()
                        response.data?.let { data ->
                            activity?.let {
                                val intent =
                                    SelfAwardPointsSuccessActivity.newSelfAwardSuccessIntent(it)
                                intent.putExtra(
                                    IntentConstants.SELF_AWARD_VERIFY_OTP_Message,
                                    data.message
                                )
                                    .putExtra(IntentConstants.GIVE_RATING, data.customerRating)
                                startActivity(intent)
                                it.finish()
                            }
                            hideKeyboard()
                        }
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


        viewModel.observeSelfAwardPointsResendOTP().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        oneLinkProgressDialog.hideProgressDialog()
                        showNewOTPSentCard()
                        fiveMinuteTimer.reset()
                        timer.start()
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
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvSentNewOtpMsg.visibility = View.GONE
        }, RESEND_OTP_CARD_VISIBILITY_TIMER)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SelfAwardPointsOTPFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        fiveMinuteTimer.cancel()
    }
}