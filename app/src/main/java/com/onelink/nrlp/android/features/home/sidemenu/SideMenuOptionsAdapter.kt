package com.onelink.nrlp.android.features.home.sidemenu

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Umar Javed.
 */

class SideMenuOptionsAdapter(private val models: List<SideMenuUIModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = models.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sideMenuUIModel = models[position]
        sideMenuUIModel.bindViewHolder(holder as SideMenuUIModel.BaseViewHolder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return models.find {
            it.getViewType() == viewType
        }!!.createViewHolder(parent)
    }

    override fun getItemViewType(position: Int) = models[position].getViewType()
}