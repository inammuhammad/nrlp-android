package com.onelink.nrlp.android.features.viewStatement.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentLoyaltyStatementBinding
import com.onelink.nrlp.android.features.viewStatement.adpater.StatementsAdapter
import com.onelink.nrlp.android.features.viewStatement.models.StatementDetailModel
import com.onelink.nrlp.android.features.viewStatement.viewmodel.LoyaltyStatementFragmentViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.roundOff
import com.onelink.nrlp.android.utils.setLoyaltyCardBackground
import com.onelink.nrlp.android.utils.toFormattedAmount
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.ly_loyalty_points_balance.view.*
import javax.inject.Inject


class LoyaltyStatementFragment :
    BaseFragment<LoyaltyStatementFragmentViewModel, FragmentLoyaltyStatementBinding>(
        LoyaltyStatementFragmentViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    private val layoutManager: LinearLayoutManager = object : LinearLayoutManager(activity) {
        override fun canScrollVertically(): Boolean {
            return false
        }
    }

    override fun getLayoutRes() = R.layout.fragment_loyalty_statement

    override fun getTitle(): String = resources.getString(R.string.view_stmnt)

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): LoyaltyStatementFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(LoyaltyStatementFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getStatements()
        initListeners()
        initObservers()
        binding.rvStatements.layoutManager = layoutManager

        UserData.getUser()?.let {
            binding.lyLoyaltyPointsBalance.tvPoints.text =
                it.loyaltyPoints?.roundOff()?.toFormattedAmount()
            context?.let { context ->
                binding.lyLoyaltyPointsBalance.ivHomeBgLoyaltyCard.setLoyaltyCardBackground(
                    context, it.loyaltyLevel
                )
            }
        }
    }

    private fun initListeners() {
        binding.btnAdvancedStatement.setOnClickListener {
            fragmentHelper.addFragment(
                fragment = AdvancedLoyaltyStatementFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
    }

    private fun initObservers() {
        viewModel.observeStatements().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        if (it.data.statements.size > 0) {
                            val statements: List<StatementDetailModel> = it.data.statements
                            binding.rvStatements.setHasFixedSize(true)
                            binding.rvStatements.adapter = StatementsAdapter(context, statements)
                            binding.lyStatements.visibility = View.VISIBLE
                            binding.lyNoStatementsFound.visibility = View.GONE
                            binding.btnAdvancedStatement.visibility = View.VISIBLE
                        } else {
                            binding.lyStatements.visibility = View.GONE
                            binding.lyNoStatementsFound.visibility = View.VISIBLE
                            binding.btnAdvancedStatement.visibility = View.GONE
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

    companion object {
        @JvmStatic
        fun newInstance() = LoyaltyStatementFragment()
    }
}
