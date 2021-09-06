package com.onelink.nrlp.android.features.selfAwardPoints.view

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseActivity
import com.onelink.nrlp.android.databinding.SelfAwardPointsSuccessBinding
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsSuccessViewModel
import com.onelink.nrlp.android.utils.IntentConstants
import kotlinx.android.synthetic.main.fragment_register_success.buttonDone
import kotlinx.android.synthetic.main.self_award_points_success.*
import javax.inject.Inject

class SelfAwardPointsSuccessActivity :
    BaseActivity<SelfAwardPointsSuccessBinding, SelfAwardPointsSuccessViewModel>(
            SelfAwardPointsSuccessViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.self_award_points_success


    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: SelfAwardPointsSuccessViewModel) {

        val message = intent.extras?.get(IntentConstants.SELF_AWARD_VERIFY_OTP_Message) as String?
        textViewSelfAwardSuccessMsg.text = message

//        toolbar.setTitle(resources.getString(R.string.self_award_points))
        buttonDone.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun newSelfAwardSuccessIntent(context: Context): Intent {
            return Intent(context, SelfAwardPointsSuccessActivity::class.java)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
