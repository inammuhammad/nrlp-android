package com.onelink.nrlp.android.features.home.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseActivity
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.ActivityRegisterContainerBinding
import com.onelink.nrlp.android.features.home.fragments.NadraVerificationRequiredFragment
import com.onelink.nrlp.android.features.register.view.RegisterSuccessActivity
import com.onelink.nrlp.android.features.register.viewmodel.RegisterViewModel
import com.onelink.nrlp.android.utils.Constants
import kotlinx.android.synthetic.main.fragment_register_success.*
import java.util.*
import javax.inject.Inject

class NadraVerificationsSuccessActivity: BaseActivity<ActivityRegisterContainerBinding, RegisterViewModel>(
    RegisterViewModel::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.fragment_register_success

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: RegisterViewModel) {
        textViewRegistrationSuccessful.text = getString(R.string.update_successful)
        textViewRegisterSuccessMsg.text = getString(R.string.nadra_details_provided)
        buttonDone.setOnClickListener {
            launchHomeActivity()
        }
    }

    private fun launchHomeActivity(){
        HomeActivity.start(this)
    }

    companion object {
        fun newNadraVerificationSuccessIntent(context: Context): Intent {
            return Intent(context, NadraVerificationsSuccessActivity::class.java)
        }

        fun start(activity: Activity) {
            val intent = Intent(activity, NadraVerificationsSuccessActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}