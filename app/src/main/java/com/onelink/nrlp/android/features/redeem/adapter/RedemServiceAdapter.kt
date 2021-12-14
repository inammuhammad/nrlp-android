package com.onelink.nrlp.android.features.redeem.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.RedemServiceListItemBinding
import com.onelink.nrlp.android.features.redeem.model.RedeemCategoryModel
import com.onelink.nrlp.android.utils.toFormattedAmount

class RedemServiceAdapter(
    private val context: Context?,
    private val redeemModels: List<RedeemCategoryModel>,
    private val listener: OnItemClickListener,
    private val partner: String = ""
) : RecyclerView.Adapter<RedemServiceAdapter.BeneficiaryViewHolder>() {
    override fun getItemCount() = redeemModels.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BeneficiaryViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.redem_service_list_item, parent, false
        )
    )

    class BeneficiaryViewHolder(val redemPartnerCellBinding: RedemServiceListItemBinding) :
        RecyclerView.ViewHolder(redemPartnerCellBinding.root)

    override fun onBindViewHolder(holder: BeneficiaryViewHolder, position: Int) {
        val redeemModel = redeemModels[position]
        holder.redemPartnerCellBinding.redeemCategories = redeemModel
        val points = context?.getString(R.string.points)?.let {
            String.format(it, redeemModel.points.toFormattedAmount())
        }
        holder.redemPartnerCellBinding.pendingTextView.text = points
        holder.redemPartnerCellBinding.root.setOnClickListener {
            listener.onItemClicked(redeemModel)
        }
        if (partner.contains("SLIC", true))
            holder.redemPartnerCellBinding.pendingTextView.visibility = View.GONE
    }

    interface OnItemClickListener {
        fun onItemClicked(redeemCategoryModel: RedeemCategoryModel)
    }
}