package com.onelink.nrlp.android.features.redeem.view

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseActivity
import com.onelink.nrlp.android.databinding.ActivityRedeemSuccessBinding
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemSuccessViewModel
import com.onelink.nrlp.android.utils.IntentConstants
import com.onelink.nrlp.android.utils.getCurrentDate
import java.math.BigInteger
import javax.inject.Inject

class RedeemSuccessActivity : BaseActivity<ActivityRedeemSuccessBinding, RedeemSuccessViewModel>(
    RedeemSuccessViewModel::class.java
) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.activity_redeem_success

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    private lateinit var s: String

    private lateinit var str: SpannableStringBuilder

    private var psid: String ?= null

    override fun initViewModel(viewModel: RedeemSuccessViewModel) {
        val partnerName = intent.extras?.get(IntentConstants.PARTNER_NAME) as String
        val transactionId = intent.extras?.get(IntentConstants.TRANSACTION_ID) as String
        val redeemPoints = intent.extras?.get(IntentConstants.REDEEM_POINTS) as BigInteger

        val date = getCurrentDate()
        if(partnerName == "FBR") {
             psid = intent.extras?.get(IntentConstants.PSID) as String
            s = String.format(
                getString(R.string.redem_points_fbr_detail), redeemPoints, psid, partnerName,date
            )
            str = SpannableStringBuilder(s)
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(redeemPoints.toString()),
                s.indexOf(redeemPoints.toString()) +
                        redeemPoints.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(psid.toString()),
                s.indexOf(psid.toString()) + psid.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(partnerName),
                s.indexOf(partnerName) + partnerName.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.textViewRedeemMessage.text = str
        }
        else if(partnerName == "Passport"){
             s = String.format(
                getString(R.string.redem_points_detail), redeemPoints, partnerName, date
            )
            val str = SpannableStringBuilder(s)
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(redeemPoints.toString()),
                s.indexOf(redeemPoints.toString()) +
                        redeemPoints.toString().length + resources.getString(R.string.points_simple).length + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(partnerName),
                s.indexOf(partnerName) + partnerName.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.textViewRedeemMessage.text = str
        } else if(partnerName == "PIA") {
            psid = intent.extras?.get(IntentConstants.PSID) as String
            s = String.format(
                getString(R.string.redem_points_fbr_detail), redeemPoints, psid, partnerName,date
            )
            str = SpannableStringBuilder(s)
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(redeemPoints.toString()),
                s.indexOf(redeemPoints.toString()) +
                        redeemPoints.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(psid.toString()),
                s.indexOf(psid.toString()) + psid.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(partnerName),
                s.indexOf(partnerName) + partnerName.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.textViewRedeemMessage.text = str
        } else if(partnerName == "NADRA") {
            psid = intent.extras?.get(IntentConstants.PSID) as String
            s = String.format(
                getString(R.string.redem_points_nadra_detail), redeemPoints, psid, partnerName,date
            )
            str = SpannableStringBuilder(s)
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(redeemPoints.toString()),
                s.indexOf(redeemPoints.toString()) +
                        redeemPoints.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(psid.toString()),
                s.indexOf(psid.toString()) + psid.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(partnerName),
                s.indexOf(partnerName) + partnerName.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.textViewRedeemMessage.text = str
        } else if(partnerName == "USC") {
            psid = intent.extras?.get(IntentConstants.PSID) as String
            s = String.format(
                getString(R.string.redem_points_fbr_detail), redeemPoints, psid, partnerName,date
            )
            str = SpannableStringBuilder(s)
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(redeemPoints.toString()),
                s.indexOf(redeemPoints.toString()) +
                        redeemPoints.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(psid.toString()),
                s.indexOf(psid.toString()) + psid.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(partnerName),
                s.indexOf(partnerName) + partnerName.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.textViewRedeemMessage.text = str
        } else if(partnerName == "OPF") {
            psid = intent.extras?.get(IntentConstants.PSID) as String
            s = String.format(
                getString(R.string.redem_points_opf_detail), psid, redeemPoints, partnerName,date
            )
            str = SpannableStringBuilder(s)
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(redeemPoints.toString()),
                s.indexOf(redeemPoints.toString()) +
                        redeemPoints.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(psid.toString()),
                s.indexOf(psid.toString()) + psid.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(partnerName),
                s.indexOf(partnerName) + partnerName.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.textViewRedeemMessage.text = str

        }
        else if(partnerName == "SLIC") {
            psid = intent.extras?.get(IntentConstants.PSID) as String
            s = String.format(
                getString(R.string.redem_points_slic_detail), redeemPoints, psid, partnerName,date
            )
            str = SpannableStringBuilder(s)
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(redeemPoints.toString()),
                s.indexOf(redeemPoints.toString()) +
                        redeemPoints.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(psid.toString()),
                s.indexOf(psid.toString()) + psid.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(partnerName),
                s.indexOf(partnerName) + partnerName.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.textViewRedeemMessage.text = str
        }
        else if(partnerName == "BEOE") {
            psid = intent.extras?.get(IntentConstants.PSID) as String
            s = String.format(
                getString(R.string.redem_points_beoe_detail), redeemPoints, psid, partnerName,date
            )
            str = SpannableStringBuilder(s)
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(redeemPoints.toString()),
                s.indexOf(redeemPoints.toString()) +
                        redeemPoints.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(psid.toString()),
                s.indexOf(psid.toString()) + psid.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(partnerName),
                s.indexOf(partnerName) + partnerName.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.textViewRedeemMessage.text = str
        }
       /* val str = SpannableStringBuilder(s)
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            s.indexOf(redeemPoints.toString()),
            s.indexOf(redeemPoints.toString()) +
                    redeemPoints.toString().length + resources.getString(R.string.points_simple).length + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            s.indexOf(partnerName),
            s.indexOf(partnerName) + partnerName.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            s.indexOf(psid.toString()),
            s.indexOf(partnerName) + psid.toString().length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textViewRedeemMessage.text = str
*/
        val receiptNumberString = String.format(getString(R.string.receipt_number), transactionId)
        binding.textViewReceiptNumber.text = receiptNumberString

        binding.buttonDone.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun newRedeemSuccessIntent(context: Context): Intent {
            return Intent(context, RedeemSuccessActivity::class.java)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
