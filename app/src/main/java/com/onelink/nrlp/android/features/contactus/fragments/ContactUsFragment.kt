package com.onelink.nrlp.android.features.contactus.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentContactUsBinding
import com.onelink.nrlp.android.features.contactus.adapter.ContactUsAdapter
import com.onelink.nrlp.android.features.contactus.models.ContactDetailModel
import com.onelink.nrlp.android.features.contactus.view.ContactUsUtils
import com.onelink.nrlp.android.features.contactus.viewmodel.ContactUsActivityViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_contact_us.*
import javax.inject.Inject

class ContactUsFragment :
    BaseFragment<ContactUsActivityViewModel, FragmentContactUsBinding>(ContactUsActivityViewModel::class.java) {
    override fun getLayoutRes() = R.layout.fragment_contact_us
    override fun getTitle(): String = resources.getString(R.string.contact_us)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        @JvmStatic
        fun newInstance() = ContactUsFragment()
    }

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): ContactUsActivityViewModel =
        ViewModelProvider(this, viewModelFactory).get(ContactUsActivityViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)

        val contactList: List<ContactDetailModel> = ContactUsUtils.contactUsList()
        rv_contact_us.setHasFixedSize(true)
        context?.run { rv_contact_us.adapter = ContactUsAdapter(contactList, this) }
    }
}