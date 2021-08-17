package com.onelink.nrlp.android.features.nrlpBenefits.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.features.nrlpBenefits.model.RedemptionPartnerModel
import com.onelink.nrlp.android.utils.base64ToBitmap


class NrlpPartnersTilesAdapter(
    private val context: Context?,
    private val partnersTileModels: List<RedemptionPartnerModel>,
    private val listener: (RedemptionPartnerModel) -> Unit
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.nrlp_partners_tiles_item, parent, false)
        view.tag =  ViewHolder(view).apply {
            val bitmap = getItem(position).imgSrc.base64ToBitmap()
            if(bitmap != null)
                tileIcon.setImageBitmap(bitmap)
            else
                tileIcon.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_benefits_placeholder) })
            tileTitle.text = getItem(position).name
            itemCard.setOnClickListener { listener(getItem(position)) }
        }
        if (position == 0) view?.layoutDirection = View.LAYOUT_DIRECTION_LOCALE
        return view
    }


    override fun getItem(position: Int): RedemptionPartnerModel {
        return partnersTileModels[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getCount(): Int {
        return partnersTileModels.size
    }

}

private class ViewHolder(view: View) {
    val itemCard: CardView = view.findViewById(R.id.homeItemCard)
    var tileIcon: ImageView = view.findViewById(R.id.ivTileIcon)
    var tileTitle: TextView = view.findViewById(R.id.tvTileText)
}

