package com.onelink.nrlp.android.features.select.banksandexchange.view

import android.content.Context
import android.os.Bundle
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.SelectCountryCodeFragmentBinding
import com.onelink.nrlp.android.features.select.banksandexchange.adapter.BanksAndExchangeAdapter
import com.onelink.nrlp.android.features.select.banksandexchange.viewmodel.SelectBanksAndExchangeViewModel
import com.onelink.nrlp.android.features.select.generic.model.BranchCenterModel
import com.onelink.nrlp.android.features.select.generic.viewmodel.SelectItemViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SelectBanksAndExchangeFragment() :
    BaseFragment<SelectBanksAndExchangeViewModel, SelectCountryCodeFragmentBinding>(
        SelectBanksAndExchangeViewModel::class.java
    ) {

    var pageNum = 0

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var listener: OnSelectBanksExchangeListener

    lateinit var cityAdapter: BanksAndExchangeAdapter

    fun setOnClickListener(listener: OnSelectBanksExchangeListener) {
        this.listener = listener
    }

    override fun getLayoutRes() = R.layout.select_country_code_fragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as OnSelectBanksExchangeListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$context must implement OnArticleSelectedListener"
            )
        }
    }

    override fun getTitle(): String = resources.getString(R.string.title_place_of_birth)

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): SelectBanksAndExchangeViewModel =
        ViewModelProvider(this, viewModelFactory).get(SelectBanksAndExchangeViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)


        //binding.btnLoadMore.visibility = View.VISIBLE
        binding.countrySearch.queryHint = resources.getString(R.string.search_bank_exchange)
        binding.btnLoadMore.setOnClickListener {
            pageNum += 1
            //viewModel.getCities(binding.countrySearch.query.toString(), pageNum)
        }
        binding.countrySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                /*pageNum = 0
                viewModel.getCities(query.toString(), pageNum)*/
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                cityAdapter.filter.filter(newText)
                /*if(newText == "") {
                    pageNum = 0
                    viewModel.getCities(newText.toString(), pageNum)
                }*/
                return false
            }

        })

        var banksAndExchangeList: ArrayList<String> = viewModel.bcList
        binding.rvCountries.setHasFixedSize(true)
        cityAdapter = BanksAndExchangeAdapter(
            banksAndExchangeList,
            listener::onSelectBanksExchangeListener
        )
        binding.rvCountries.layoutManager =
            LinearLayoutManager(requireContext())
        binding.rvCountries.adapter = cityAdapter

        viewModel.observeBranchCenter().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    /*response.data?.let {
                        //var banksAndExchangeList: ArrayList<BranchCenterModel> = it.branchCenterList
                        var banksAndExchangeList: ArrayList<String> = viewModel.bcList
                        binding.rvCountries.setHasFixedSize(true)
                        cityAdapter = BanksAndExchangeAdapter(
                            banksAndExchangeList,
                            listener::onSelectBanksExchangeListener
                        )
                        binding.rvCountries.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.rvCountries.adapter = cityAdapter
                    }*/
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

    interface OnSelectBanksExchangeListener {
        fun onSelectBanksExchangeListener(banksAndExchangeModel: String)
    }


    companion object {
        @JvmStatic
        fun newInstance() = SelectBanksAndExchangeFragment()
    }
}