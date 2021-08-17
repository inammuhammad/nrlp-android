package com.onelink.nrlp.android.features.faqs.view

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FaqsFragmentBinding
import com.onelink.nrlp.android.features.faqs.adapter.RVAdapter
import com.onelink.nrlp.android.features.faqs.model.FAQAdapterModel
import com.onelink.nrlp.android.features.faqs.model.FAQsModel
import com.onelink.nrlp.android.features.faqs.viewmodel.FAQsFragmentViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class FAQsFragment :
    BaseFragment<FAQsFragmentViewModel, FaqsFragmentBinding>(FAQsFragmentViewModel::class.java) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var adapter: RVAdapter

    private lateinit var faqAdapterModels: ArrayList<FAQAdapterModel>

    companion object {
        @JvmStatic
        fun newInstance() = FAQsFragment()
    }

    override fun getLayoutRes() = R.layout.faqs_fragment

    override fun getTitle(): String = resources.getString(R.string.faqs_title)

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): FAQsFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(FAQsFragmentViewModel::class.java)


    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)

        viewModel.getFaqs()

        viewModel.observerFaqs().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        val faqsList: ArrayList<FAQsModel> = it.questions.faqsList
                        faqAdapterModels = arrayListOf()
                        for (faqModel in faqsList) {
                            faqAdapterModels.add(FAQAdapterModel(faqModel, false))
                        }
                        binding.rcFaqs.setHasFixedSize(true)
                        binding.rcFaqs.layoutManager = LinearLayoutManager(context)
                        val a = binding.root.context.obtainStyledAttributes(
                            intArrayOf(android.R.attr.listDivider)
                        )
                        val divider = a.getDrawable(0)
                        val insetLeft =
                            resources.getDimensionPixelSize(R.dimen._15sdp)
                        val insetRight =
                            resources.getDimensionPixelSize(R.dimen._18sdp)
                        val insetTop =
                            resources.getDimensionPixelSize(R.dimen._12sdp)
                        val insetDivider =
                            InsetDrawable(divider, insetLeft, insetTop, insetRight, 0)
                        a.recycle()

                        val itemDecoration =
                            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                        itemDecoration.setDrawable(insetDivider)
                        binding.rcFaqs.addItemDecoration(itemDecoration)
                        adapter = RVAdapter(faqAdapterModels)
                        binding.rcFaqs.adapter = adapter
                        adapter.notifyDataSetChanged()
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
    }
}
