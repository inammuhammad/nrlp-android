package com.onelink.nrlp.android.features.home.sidemenu

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onelink.nrlp.android.databinding.ItemSideMenuItemsBinding

/**
 * Created by Umar Javed.
 */

class SideMenuOptionsModel(private val sideMenuOptionsItemModel: SideMenuOptionsItemModel,
                           private val onMenuItemClicked: ((sideMenuOptionsItemModel: SideMenuOptionsItemModel) -> Unit)) : SideMenuUIModel() {

    private lateinit var binding: ItemSideMenuItemsBinding

    override fun createViewHolder(parent: ViewGroup): BaseViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemSideMenuItemsBinding.inflate(inflater, parent, false)
        return BaseViewHolder(binding)
    }

    override fun bindViewHolder(holder: BaseViewHolder, position: Int) {
        binding = holder.binding as ItemSideMenuItemsBinding

        binding.tvMenuItemTitle.text = sideMenuOptionsItemModel.title
        binding.ivMenuItemImage.setImageResource(sideMenuOptionsItemModel.drawableId)

        binding.tvMenuItemTitle.setOnClickListener{
            onMenuItemClicked(sideMenuOptionsItemModel)
        }

        binding.root.setOnClickListener{
            onMenuItemClicked(sideMenuOptionsItemModel)
        }
    }

    override fun getViewType() = TYPE_MENU_OPTIONS
}
