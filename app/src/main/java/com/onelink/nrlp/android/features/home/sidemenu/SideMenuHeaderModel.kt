package com.onelink.nrlp.android.features.home.sidemenu

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onelink.nrlp.android.databinding.ItemSideMenuHeaderBinding
import com.onelink.nrlp.android.utils.formattedCnicNumber
import java.math.BigInteger

/**
 * Created by Umar Javed.
 */

class SideMenuHeaderModel(private val userName: String, private val nicNicop: BigInteger) : SideMenuUIModel() {

    private lateinit var binding: ItemSideMenuHeaderBinding

    override fun createViewHolder(parent: ViewGroup): BaseViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemSideMenuHeaderBinding.inflate(inflater, parent, false)
        return BaseViewHolder(binding)
    }

    override fun bindViewHolder(holder: BaseViewHolder, position: Int) {
        binding = holder.binding as ItemSideMenuHeaderBinding

        binding.tvUserName.text = userName
        binding.tvCNIC.text = String.format(nicNicop.toString().formattedCnicNumber())
    }

    override fun getViewType() = TYPE_HEADER
}
