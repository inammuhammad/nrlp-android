package com.onelink.nrlp.android.features.select.city.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.SelectCountryCodeFragmentBinding
import com.onelink.nrlp.android.features.select.city.adapter.CitiesAdapter
import com.onelink.nrlp.android.features.select.city.model.CitiesModel
import com.onelink.nrlp.android.features.select.city.viewmodel.SelectCityFragmentViewModel
import com.onelink.nrlp.android.features.select.country.adapter.CountryAdapter
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.features.select.country.viewmodel.SelectCountryFragmentViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SelectCityFragment(type: String = "beneficiary") :
    BaseFragment<SelectCityFragmentViewModel, SelectCountryCodeFragmentBinding>(
        SelectCityFragmentViewModel::class.java
    ) {

    var userType = type
    var pageNum = 0

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var listener: OnSelectCityListener

    lateinit var cityAdapter: CitiesAdapter

    fun setOnClickListener(listener: OnSelectCityListener) {
        this.listener = listener
    }

    override fun getLayoutRes() = R.layout.select_country_code_fragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as OnSelectCityListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$context must implement OnArticleSelectedListener"
            )
        }
    }

    override fun getTitle(): String = resources.getString(R.string.select_place_of_birth_title)

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): SelectCityFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(SelectCityFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)

        viewModel.getCountryCodes(userType)
        viewModel.getCities()

        binding.btnLoadMore.visibility = View.VISIBLE
        binding.countrySearch.queryHint = resources.getString(R.string.search_city)
        binding.btnLoadMore.setOnClickListener {
            pageNum += 1
            viewModel.getCities(binding.countrySearch.query.toString(), pageNum)
        }
        binding.countrySearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                pageNum = 0
                viewModel.getCities(query.toString(), pageNum)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //cityAdapter.filter.filter(newText)
                if(newText == "") {
                    pageNum = 0
                    viewModel.getCities(newText.toString(), pageNum)
                }
                return false
            }

        })

        /*viewModel.observerCountryCodes().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        val countriesList: List<CountryCodeModel> =
                            it.countryCodesList.sortedWith(compareBy { listItem -> listItem.country })
                        binding.rvCountries.setHasFixedSize(true)
                        cityAdapter = CountryAdapter(
                            countriesList,
                            listener::onSelectCityListener
                        )
                        binding.rvCountries.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvCountries.adapter = cityAdapter
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
        })*/

        viewModel.observeCities().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        val citiesList: ArrayList<CitiesModel> = it.citiesList
                        if(citiesList.size < 10)
                            binding.btnLoadMore.visibility = View.GONE
                        else
                            binding.btnLoadMore.visibility = View.VISIBLE
                            //it.citiesList.sortedWith(compareBy { listItem -> listItem.city })
                        binding.rvCountries.setHasFixedSize(true)
                        if(pageNum<1) {
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
                        }
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

    interface OnSelectCityListener {
        fun onSelectCityListener(citiesModel: CitiesModel)
    }


    companion object {
        @JvmStatic
        fun newInstance(type: String = "beneficiary") = SelectCityFragment(type)
    }

}