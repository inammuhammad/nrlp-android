package com.onelink.nrlp.android.features.beneficiary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.BeneficiaryListItemBinding
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import com.onelink.nrlp.android.utils.formattedCnicNumber
import kotlinx.android.synthetic.main.beneficiary_list_item.view.*

class BeneficiariesAdapter(
    context: Context?,
    private val beneficiaryDetailsModels: List<BeneficiaryDetailsModel>
) :
    RecyclerView.Adapter<BeneficiariesAdapter.BeneficiaryViewHolder>() {
    private val clickHandler: ClickEventHandler = context as ClickEventHandler

    override fun getItemCount() = beneficiaryDetailsModels.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BeneficiaryViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.beneficiary_list_item,
            parent, false
        )
    )

    class BeneficiaryViewHolder(val beneficiaryListItemBinding: BeneficiaryListItemBinding) :
        RecyclerView.ViewHolder(beneficiaryListItemBinding.root)

    override fun onBindViewHolder(holder: BeneficiaryViewHolder, position: Int) {
        holder.beneficiaryListItemBinding.beneficiary =
            beneficiaryDetailsModels[position]
        holder.beneficiaryListItemBinding.root.tvCnic.text =
            String.format(beneficiaryDetailsModels[position].nicNicop.toString().formattedCnicNumber())
        if (beneficiaryDetailsModels[position].nadraStatusCode == "A") {
            holder.beneficiaryListItemBinding.root.pendingTextView.visibility = View.GONE
            holder.beneficiaryListItemBinding.root.activeTextView.visibility = View.VISIBLE
        } else {
            holder.beneficiaryListItemBinding.root.pendingTextView.visibility = View.VISIBLE
            holder.beneficiaryListItemBinding.root.activeTextView.visibility = View.GONE
        }
        holder.beneficiaryListItemBinding.root.item_ly.setOnClickListener {
            clickHandler.onBeneficiarySelected(beneficiaryDetailsModels[position])
        }
    }

    interface ClickEventHandler {
        fun onBeneficiarySelected(beneficiaryDetailsModel: BeneficiaryDetailsModel)
    }
}