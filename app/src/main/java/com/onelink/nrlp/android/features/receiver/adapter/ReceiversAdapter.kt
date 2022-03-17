package com.onelink.nrlp.android.features.receiver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.BeneficiaryListItemBinding
import com.onelink.nrlp.android.databinding.ReceiverListItemBinding
import com.onelink.nrlp.android.features.receiver.models.ReceiverDetailsModel
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import com.onelink.nrlp.android.utils.formattedCnicNumber
import kotlinx.android.synthetic.main.beneficiary_list_item.view.*

class ReceiversAdapter(
    context: Context?,
    private val receiverDetailsModels: List<ReceiverDetailsModel>
) :
    RecyclerView.Adapter<ReceiversAdapter.ReceiverViewHolder>() {
    private val clickHandler: ClickEventHandler = context as ClickEventHandler

    override fun getItemCount() = receiverDetailsModels.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReceiverViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.receiver_list_item,
            parent, false
        )
    )

    class ReceiverViewHolder(val beneficiaryListItemBinding: ReceiverListItemBinding) :
        RecyclerView.ViewHolder(beneficiaryListItemBinding.root)

    override fun onBindViewHolder(holder: ReceiverViewHolder, position: Int) {
        holder.beneficiaryListItemBinding.receiver =
            receiverDetailsModels[position]
        holder.beneficiaryListItemBinding.root.tvCnic.text =
            String.format(receiverDetailsModels[position].receiverCnic.toString().formattedCnicNumber())
        if (receiverDetailsModels[position].linkStatus == "LINKED") {
            holder.beneficiaryListItemBinding.root.pendingTextView.visibility = View.GONE
            //holder.beneficiaryListItemBinding.root.activeTextView.visibility = View.VISIBLE
        } else {
            holder.beneficiaryListItemBinding.root.pendingTextView.visibility = View.GONE
            holder.beneficiaryListItemBinding.root.activeTextView.visibility = View.GONE
        }
        holder.beneficiaryListItemBinding.root.item_ly.setOnClickListener {
            clickHandler.onReceiverSelected(receiverDetailsModels[position])
        }
    }

    interface ClickEventHandler {
        fun onReceiverSelected(receiverDetailsModel: ReceiverDetailsModel)
    }
}