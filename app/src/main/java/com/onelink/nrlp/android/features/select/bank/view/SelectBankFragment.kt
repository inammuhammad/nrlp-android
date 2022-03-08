package com.onelink.nrlp.android.features.select.bank.view

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
import com.onelink.nrlp.android.features.select.bank.model.BankDetailsModel
import com.onelink.nrlp.android.features.select.bank.viewmodel.SelectBankFragmentViewModel
import com.onelink.nrlp.android.features.select.bank.adapter.BanksAdapter
import com.onelink.nrlp.android.features.select.city.adapter.CitiesAdapter
import com.onelink.nrlp.android.features.select.city.model.CitiesModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SelectBankFragment(type: String = "beneficiary") :
    BaseFragment<SelectBankFragmentViewModel, SelectCountryCodeFragmentBinding>(
        SelectBankFragmentViewModel::class.java
    ) {

    var userType = type
    var pageNum = 0

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var listener: OnSelectBankListener

    lateinit var cityAdapter: BanksAdapter

    fun setOnClickListener(listener: OnSelectBankListener) {
        this.listener = listener
    }

    override fun getLayoutRes() = R.layout.select_country_code_fragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as OnSelectBankListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$context must implement OnArticleSelectedListener"
            )
        }
    }

    override fun getTitle(): String = resources.getString(R.string.bank_name)

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): SelectBankFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(SelectBankFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)

        viewModel.getBanksList()

        //binding.btnLoadMore.visibility = View.VISIBLE
        binding.countrySearch.queryHint = resources.getString(R.string.search_bank)
        binding.btnLoadMore.setOnClickListener {
            pageNum += 1
            //viewModel.getCities(binding.countrySearch.query.toString(), pageNum)
        }
        binding.countrySearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
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

        viewModel.observeBankList().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        var citiesList: ArrayList<BankDetailsModel> = it.data
                        //citiesList.add(CitiesModel("0", "Other"))
                        //it.citiesList.sortedWith(compareBy { listItem -> listItem.city })
                        binding.rvCountries.setHasFixedSize(true)
                        cityAdapter = BanksAdapter(
                            citiesList,
                            listener::onSelectBankListener
                        )
                        binding.rvCountries.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.rvCountries.adapter = cityAdapter
                        /*if(pageNum<1) {
                            cityAdapter = CitiesAdapter(
                                citiesList,
                                listener::onSelectCityListener
                            )
                            binding.rvCountries.layoutManager =
                                LinearLayoutManager(requireContext())
                            binding.rvCountries.adapter = cityAdapter
                        }
                        else {
                            cityAdapter.addItems(citiesList)
                            //cityAdapter.notifyDataSetChanged()
                        }*/
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

    interface OnSelectBankListener {
        fun onSelectBankListener(bankDetailsModel: BankDetailsModel)
    }


    companion object {
        @JvmStatic
        fun newInstance() = SelectBankFragment()
    }

}