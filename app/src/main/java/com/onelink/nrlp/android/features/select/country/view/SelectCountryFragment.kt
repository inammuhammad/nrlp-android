package com.onelink.nrlp.android.features.select.country.view

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
import com.onelink.nrlp.android.features.select.country.adapter.CountryAdapter
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.viewmodel.SelectCountryFragmentViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SelectCountryFragment :
    BaseFragment<SelectCountryFragmentViewModel, SelectCountryCodeFragmentBinding>(
        SelectCountryFragmentViewModel::class.java
    ) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var listener: OnSelectCountryListener

    lateinit var countryAdapter: CountryAdapter

    fun setOnClickListener(listener: OnSelectCountryListener) {
        this.listener = listener
    }

    override fun getLayoutRes() = R.layout.select_country_code_fragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as OnSelectCountryListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$context must implement OnArticleSelectedListener"
            )
        }
    }

    override fun getTitle(): String = resources.getString(R.string.select_country_title)

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): SelectCountryFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(SelectCountryFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)

        viewModel.getCountryCodes()
        binding.countrySearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                countryAdapter.filter.filter(newText)
                return false
            }

        })

        viewModel.observerCountryCodes().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        val countriesList: List<CountryCodeModel> =
                            it.countryCodesList.sortedWith(compareBy { listItem -> listItem.country })
                        binding.rvCountries.setHasFixedSize(true)
                        countryAdapter = CountryAdapter(
                            countriesList,
                            listener::onSelectCountryListener
                        )
                        binding.rvCountries.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvCountries.adapter = countryAdapter
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

    interface OnSelectCountryListener {
        fun onSelectCountryListener(countryCodeModel: CountryCodeModel)
    }


    companion object {
        @JvmStatic
        fun newInstance() = SelectCountryFragment()
    }
}