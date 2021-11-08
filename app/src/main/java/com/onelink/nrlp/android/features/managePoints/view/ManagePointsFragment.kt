package com.onelink.nrlp.android.features.managePoints.view

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.data.local.UserModel
import com.onelink.nrlp.android.databinding.ManagePointsFragmentBinding
import com.onelink.nrlp.android.features.managePoints.model.TransferPointsResponseModel
import com.onelink.nrlp.android.features.managePoints.viewmodel.ManagePointsFragmentViewModel
import com.onelink.nrlp.android.models.BeneficiariesResponseModel
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.ly_loyalty_points_balance.view.*
import java.math.BigInteger
import java.text.DecimalFormat
import javax.inject.Inject

const val TRANSFER_POINTS_DIALOG = 4001
const val TAG_TRANSFER_POINTS_DIALOG = "transfer_points_dialog"
const val NOT_ENOUGH_TRANSFER_POINTS_DIALOG = 4002
const val TAG_NOT_ENOUGH_TRANSFER_POINTS_DIALOG = "not_enough_transfer_points_dialog"


class ManagePointsFragment :
    BaseFragment<ManagePointsFragmentViewModel, ManagePointsFragmentBinding>(
        ManagePointsFragmentViewModel::class.java
    ), OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    val list = arrayListOf<BeneficiaryDetailsModel>()
    private val listSpinner: ArrayList<String> = ArrayList()

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var listenerInitialized: Boolean = false

    private var selectedBeneficiary = BeneficiaryDetailsModel(-1, BigInteger.ONE, "12312", 0, "", "")

    override fun getLayoutRes() = R.layout.manage_points_fragment

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getTitle(): String {
        return resources.getString(R.string.transfer_points)
    }

    override fun getViewM(): ManagePointsFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(ManagePointsFragmentViewModel::class.java)

    private var userPoints = BigInteger.ZERO

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        UserData.getUser()?.let(this::setUpUserPointsAndBalance)
        viewModel.getBeneficiary()
        initEditTextWatcher()
        binding.btnTransfer.setOnSingleClickListener(this::onTransferClick)
        viewModel.observeBeneficiary().observe(this, this::onBeneficiaryResponse)
        binding.spinnerLy.setOnClickListener {
            binding.spinnerSelectBene.performClick()
        }
        viewModel.observeTransferPoints().observe(this, this::onTransferResponse)
    }

    private fun setUpUserPointsAndBalance(userModel: UserModel) {
        val mContext = context ?: return
        binding.lyLoyaltyPointsBalanceMP.tvPoints.text =
            userModel.loyaltyPoints?.roundOff()?.toFormattedAmount()
        binding.lyLoyaltyPointsBalanceMP.tvName.text = userModel.fullName
        binding.lyLoyaltyPointsBalanceMP.tvMemberSince.text = userModel.memberSince
        binding.lyLoyaltyPointsBalanceMP.ivHomeBgLoyaltyCard.setLoyaltyCard(
            mContext,
            userModel.loyaltyLevel
        )
        userPoints = userModel.loyaltyPoints?.toBigInteger() ?: BigInteger.ZERO
    }

    private fun onBeneficiaryResponse(response: BaseResponse<BeneficiariesResponseModel>) {
        when (response.status) {
            Status.SUCCESS -> {
                binding.spinnerSelectBene.adapter = context?.let {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.data?.let { userData ->
                        for (i in userData) {
                            if (i.isActive) {
                                list.add(i)
                                listSpinner.add(i.alias)
                            }
                        }
                        if (list.size > 0) {
                            binding.lyNoBeneficiary.visibility = View.GONE
                            binding.lyManagePoints.visibility = View.VISIBLE

                        } else {
                            binding.lyNoBeneficiary.visibility = View.VISIBLE
                            binding.lyManagePoints.visibility = View.GONE
                        }

                    }
                    ArrayAdapter(
                        it,
                        R.layout.custom_spinner_item,
                        listSpinner
                    )
                } as SpinnerAdapter
                binding.spinnerSelectBene.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // not in use
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (listenerInitialized) {
                                list[position]
                                val data = list[position]
                                selectedBeneficiary = data
                                binding.tvSelectedBene.text = list[position].alias
                                binding.tvSelectedBene.colorToText(R.color.pure_black)
                                viewModel.selectedBene.postValue(data)


                            } else {
                                listenerInitialized = true
                                binding.spinnerSelectBene.setSelection(-1)
                            }
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
    }

    private fun onTransferResponse(response: BaseResponse<TransferPointsResponseModel>) {
        when (response.status) {
            Status.SUCCESS -> {
                oneLinkProgressDialog.hideProgressDialog()
                activity?.let {
                    it.startActivity(
                        TransferPointsSuccessFulActivity.newTransferPointsSuccessFulIntent(
                            it, binding.etEndPoint.text.toString().replace(",", "").toInt(),
                            selectedBeneficiary.alias
                        )
                    )
                    it.finish()
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
    }

    private fun onTransferClick() {
        if (viewModel.isValidTransaction(
                BigInteger(
                    viewModel.points.value?.replace(",", "") ?: "0"
                ),
                userPoints
            )
        ) {
            showTransferPointsDialog(selectedBeneficiary)
        } else {
            showNotEnoughPointsDialog()
        }
    }

    private fun initEditTextWatcher() {
        binding.etEndPoint.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                // on Text Change
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // before text change
            }

            override fun afterTextChanged(s: Editable) {
                binding.etEndPoint.removeTextChangedListener(this)
                try {
                    var givenStr = s.toString()
                    if (givenStr.contains(",")) {
                        givenStr = givenStr.replace(",".toRegex(), "")
                    }
                    val formatter = DecimalFormat("#,###,###")
                    val formattedString: String = formatter.format(givenStr.toLong())
                    binding.etEndPoint.setText(formattedString)
                    binding.etEndPoint.text?.length?.let { binding.etEndPoint.setSelection(it) }
                    // to place the cursor at the end of text
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                binding.etEndPoint.addTextChangedListener(this)
            }
        })
    }

    @Suppress("DEPRECATION")
    private fun showTransferPointsDialog(selectedBene: BeneficiaryDetailsModel) {
        val s = String.format(
            getString(R.string.sure_to_transfer),
            binding.etEndPoint.text.toString().replace(",", "").toInt(),
            selectedBene.alias
        )
        val points = binding.etEndPoint.text.toString().replace(",", "")
        viewModel.points.value = points
        val selectedBeneficiary = selectedBene.alias
        val str = SpannableStringBuilder(s)
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            s.indexOf(selectedBeneficiary),
            s.indexOf(selectedBeneficiary) + selectedBeneficiary.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            s.indexOf(points),
            s.indexOf(points) + points.length + resources.getString(R.string.points_simple).length + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            false,
            R.drawable.ic_transfermoney,
            getString(R.string.transfer_points),
            str.toHtmlString().parseHtml(),
            positiveButtonText = getString(R.string.confirm),
            negativeButtonText = getString(R.string.cancel)
        )
        oneLinkAlertDialogsFragment.setTargetFragment(this, TRANSFER_POINTS_DIALOG)
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_TRANSFER_POINTS_DIALOG)
    }

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        when (targetCode) {
            TRANSFER_POINTS_DIALOG -> {
                viewModel.transferPointsCall(
                    selectedBeneficiary.id,
                    viewModel.points.value?.replace(",", "") ?: ""
                )
            }
        }
    }

    private fun showNotEnoughPointsDialog() {
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            true,
            R.drawable.ic_error_dialog,
            getString(R.string.oh_snap),
            (getString(R.string.not_enough_points_transfer_msg)).toSpanned(),
            positiveButtonText = resources.getString(R.string.yes),
            negativeButtonText = resources.getString(R.string.no),
            neutralButtonText = getString(R.string.okay)
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            NOT_ENOUGH_TRANSFER_POINTS_DIALOG
        )
        oneLinkAlertDialogsFragment.show(
            parentFragmentManager,
            TAG_NOT_ENOUGH_TRANSFER_POINTS_DIALOG
        )
        oneLinkAlertDialogsFragment.isCancelable = false
    }

    companion object {
        @JvmStatic
        fun newInstance() = ManagePointsFragment()
    }
}