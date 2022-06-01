package com.onelink.nrlp.android.features.managePoints.view

import android.content.Context
import android.content.Intent
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.StyleSpan
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.TransferPointsSuccessfulBinding
import com.onelink.nrlp.android.features.managePoints.viewmodel.TransferPointsSuccessFulViewModel
import com.onelink.nrlp.android.features.rate.view.RateActivity
import com.onelink.nrlp.android.utils.IntentConstants
import com.onelink.nrlp.android.utils.TransactionTypeConstants
import kotlinx.android.synthetic.main.transfer_points_successful.*
import javax.inject.Inject

class TransferPointsSuccessFulActivity : BaseFragmentActivity<TransferPointsSuccessfulBinding,
        TransferPointsSuccessFulViewModel>(
    TransferPointsSuccessFulViewModel::class.java
) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.transfer_points_successful

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: TransferPointsSuccessFulViewModel) {
        val points = intent.getIntExtra("points", 0)
        val bene = intent.getStringExtra("beneName")
        val giveRating = intent.getBooleanExtra(IntentConstants.GIVE_RATING, false)
        val transactionType = TransactionTypeConstants.TRANSFER_POINTS
        val s = String.format(
            getString(R.string.you_have_successfully_transfered),
            points,
            bene
        )
        val str = SpannableStringBuilder(s)
        str.setSpan(
            StyleSpan(android.graphics.Typeface.BOLD), s.indexOf(points.toString()),
            s.indexOf(points.toString()) + points.toString().length +
                    resources.getString(R.string.points_simple).length + 1, SPAN_EXCLUSIVE_EXCLUSIVE
        )
        str.setSpan(
            StyleSpan(android.graphics.Typeface.BOLD), s.indexOf(bene.toString()),
            s.indexOf(bene.toString()) + bene.toString().length, SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textViewRegisterSuccessMsg.text = str

        buttonDone.setOnClickListener {
            if(giveRating)
                startActivity(
                    RateActivity.newRateIntent(this)
                        .putExtra(IntentConstants.TRANSACTION_TYPE, transactionType)
                )
            finish()
        }
    }

    companion object {
        fun newTransferPointsSuccessFulIntent(context: Context, points: Int, name: String, giveRating: Boolean): Intent {
            return Intent(context, TransferPointsSuccessFulActivity::class.java)
                .putExtra("points", points)
                .putExtra("beneName", name)
                .putExtra(IntentConstants.GIVE_RATING, giveRating)

        }
    }

}