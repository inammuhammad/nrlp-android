package com.onelink.nrlp.android.features.viewStatement.view

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseActivity
import com.onelink.nrlp.android.databinding.ActivityStatementGeneratedBinding
import com.onelink.nrlp.android.features.viewStatement.viewmodel.ViewStatementViewModel
import javax.inject.Inject

class StatementGeneratedActivity :
    BaseActivity<ActivityStatementGeneratedBinding, ViewStatementViewModel>(ViewStatementViewModel::class.java) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.activity_statement_generated

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: ViewStatementViewModel) {
        binding.buttonDone.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, StatementGeneratedActivity::class.java)
        }
    }
}