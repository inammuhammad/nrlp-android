package com.onelink.nrlp.android.features.home.sidemenu

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Umar Javed.
 */
@Suppress("unused")
fun <T : View> RecyclerView.ViewHolder.bind(@IdRes res: Int): T {
    @Suppress("UNCHECKED_CAST")
    return itemView.findViewById(res) as T
}

abstract class SideMenuUIModel {
    abstract fun createViewHolder(parent: ViewGroup): BaseViewHolder
    abstract fun bindViewHolder(holder: BaseViewHolder, position: Int)
    abstract fun getViewType(): Int

    class BaseViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        const val TYPE_HEADER = 1
        const val TYPE_MENU_OPTIONS = 2
    }
}
