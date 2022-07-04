package com.onelink.nrlp.android.features.home.fragments.popup

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentGeneralInfoBinding
import com.onelink.nrlp.android.features.home.fragments.HomeFragmentViewModel
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class GeneralInfoFragment(message: String?) :
    BaseFragment<HomeFragmentViewModel, FragmentGeneralInfoBinding>
        (HomeFragmentViewModel::class.java) {

    private val displayText = message

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    override fun getLayoutRes(): Int = R.layout.fragment_general_info

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): HomeFragmentViewModel = ViewModelProvider(
        this, viewModelFactory
    ).get(HomeFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        (activity as HomeActivity).hideHomeScreenTools()
        (activity as HomeActivity).showBackArrow()
        binding.tvInfo.text = displayText
        setUserTypeImage()
        initListeners()
    }

    private fun initListeners() {

        binding.btnNext.setOnSingleClickListener {
            (activity as HomeActivity).showHomeScreenTools() //activity?.let { HomeActivity.start(it) }
            (activity as HomeActivity).keyEventSender()
        }
    }

    private fun setUserTypeImage() {
        if (UserData.getUser()?.accountType.equals(Constants.REMITTER, ignoreCase = true))
            binding.ivPopupImage.setImageResource(R.drawable.ic_register)
        else
            binding.ivPopupImage.setImageResource(R.drawable.ic_loyalty_points_tile)
    }

    companion object {
        @JvmStatic
        fun newInstance(message: String?) = GeneralInfoFragment(message)
    }
}