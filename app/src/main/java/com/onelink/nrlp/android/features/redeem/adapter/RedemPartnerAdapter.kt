package com.onelink.nrlp.android.features.redeem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.RedemPartnerCellBinding
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerModel

class RedemPartnerAdapter(
    private val redemPartner: List<RedeemPartnerModel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RedemPartnerAdapter.RedeemPartnerViewHolder>() {
    override fun getItemCount() = redemPartner.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RedeemPartnerViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.redem_partner_cell,
            parent, false
        )
    )

    class RedeemPartnerViewHolder(val redeemPartnerCellBinding: RedemPartnerCellBinding) :
        RecyclerView.ViewHolder(redeemPartnerCellBinding.root)

    interface OnItemClickListener {
        fun onItemClicked(redeemPartnerModel: RedeemPartnerModel)
    }

    override fun onBindViewHolder(holder: RedeemPartnerViewHolder, position: Int) {
        holder.redeemPartnerCellBinding.redeem = redemPartner[position]
        holder.redeemPartnerCellBinding.root.setOnClickListener {
            listener.onItemClicked(redemPartner[position])
        }
        holder.redeemPartnerCellBinding.titlePartner.setOnClickListener {
            listener.onItemClicked(redemPartner[position])

        }
    }
}