package com.onelink.nrlp.android.features.language.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.LanguageItemCellBinding
import com.onelink.nrlp.android.features.language.model.LanguageTypeModel
import com.onelink.nrlp.android.utils.LocaleManager


class LanguageAdapter(
    private val languageObj: List<LanguageTypeModel>,
    private val listener: OnItemClickListener,
    private var lastSelectedPosition: Int
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {
    override fun getItemCount() = languageObj.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LanguageViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.language_item_cell, parent, false
        )
    )

    class LanguageViewHolder(val languageItemCellBinding: LanguageItemCellBinding) :
        RecyclerView.ViewHolder(languageItemCellBinding.root)

    interface OnItemClickListener {
        fun onItemClicked(bool: Boolean, lang: String)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.languageItemCellBinding.language = languageObj[position]
        holder.languageItemCellBinding.radiobtn.isChecked = lastSelectedPosition == position
        if (holder.languageItemCellBinding.radiobtn.isChecked) {
            holder.languageItemCellBinding.itemLayout.isClickable = false
        } else {
            holder.languageItemCellBinding.itemLayout.isClickable = true
            holder.languageItemCellBinding.itemLayout.setOnClickListener {
                lastSelectedPosition = position
                notifyDataSetChanged()
                if (lastSelectedPosition == 0) {
                    listener.onItemClicked(true, LocaleManager.ENGLISH)
                } else {
                    listener.onItemClicked(true, LocaleManager.URDU)
                }

            }
        }
    }
}