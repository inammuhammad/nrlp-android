package com.onelink.nrlp.android.features.viewStatement.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.LoyaltyStatementListItemBinding
import com.onelink.nrlp.android.features.viewStatement.models.StatementDetailModel
import com.onelink.nrlp.android.features.viewStatement.models.Type
import com.onelink.nrlp.android.utils.roundOff
import com.onelink.nrlp.android.utils.toFormattedAmount
import com.onelink.nrlp.android.utils.toFormattedDate
import kotlinx.android.synthetic.main.loyalty_statement_list_item.view.*

class StatementsAdapter(
    private val context: Context?, private val statementDetailModels: List<StatementDetailModel>
) : RecyclerView.Adapter<StatementsAdapter.BeneficiaryViewHolder>() {

    override fun getItemCount() = statementDetailModels.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BeneficiaryViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.loyalty_statement_list_item, parent, false
        )
    )

    class BeneficiaryViewHolder(val loyaltyStatementListItemBinding: LoyaltyStatementListItemBinding) :
        RecyclerView.ViewHolder(loyaltyStatementListItemBinding.root)

    override fun onBindViewHolder(holder: BeneficiaryViewHolder, position: Int) {
        val statementDetailModel = statementDetailModels[position]
        holder.loyaltyStatementListItemBinding.statement = statementDetailModel
        holder.loyaltyStatementListItemBinding.root.tvDate.text = statementDetailModel.date.toFormattedDate()
        val points = context?.getString(R.string.points)?.let {
            String.format(it, statementDetailModel.points.roundOff().toFormattedAmount())
        }
        if (statementDetailModel.name == null) {
            holder.loyaltyStatementListItemBinding.root.tvStatus.text = statementDetailModel.status
        } else {
            holder.loyaltyStatementListItemBinding.root.tvStatus.text = statementDetailModel.name
        }
        when (statementDetailModel.type) {
            Type.Credit -> {
                holder.loyaltyStatementListItemBinding.root.activeTextView.text = points
                holder.loyaltyStatementListItemBinding.root.activeTextView.visibility = View.VISIBLE
                holder.loyaltyStatementListItemBinding.root.pendingTextView.visibility = View.GONE
            }
            Type.Debit -> {
                holder.loyaltyStatementListItemBinding.root.pendingTextView.text = points
                holder.loyaltyStatementListItemBinding.root.activeTextView.visibility = View.GONE
                holder.loyaltyStatementListItemBinding.root.pendingTextView.visibility = View.VISIBLE
            }
        }
    }
}