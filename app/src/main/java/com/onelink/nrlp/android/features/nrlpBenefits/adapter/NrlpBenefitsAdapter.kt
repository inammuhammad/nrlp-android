package com.onelink.nrlp.android.features.nrlpBenefits.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.NrlpBenefitsCellBinding
import com.onelink.nrlp.android.features.nrlpBenefits.model.NrlpBenefitModel


class NrlpBenefitsAdapter( private val redemptionPartnerModels: List<NrlpBenefitModel>) : RecyclerView.Adapter<NrlpBenefitsAdapter.NrlpBenefitsViewHolder>() {
    override fun getItemCount() = redemptionPartnerModels.size
    var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NrlpBenefitsViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.nrlp_benefits_cell, parent, false
        )
    )

    class NrlpBenefitsViewHolder(val nrlpBenefitsCellBinding: NrlpBenefitsCellBinding) :
        RecyclerView.ViewHolder(nrlpBenefitsCellBinding.root)

    override fun onBindViewHolder(holder: NrlpBenefitsViewHolder, position: Int) {
        holder.nrlpBenefitsCellBinding.partner = redemptionPartnerModels[position]
    }
}