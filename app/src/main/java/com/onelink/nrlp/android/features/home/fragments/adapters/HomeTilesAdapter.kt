package com.onelink.nrlp.android.features.home.fragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import com.onelink.nrlp.android.utils.view.hometiles.HomeTileModel

class HomeTilesAdapter(
    private val context: Context?,
    private val homeTilesList: MutableList<HomeTileModel>,
    private val listener: (HomeTileModel) -> Unit
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.home_tiles_item, parent, false)
        view.tag =  ViewHolder(view).apply {
            tileIcon.setImageResource(getItem(position).drawableId)
            tileTitle.text = context?.getString(getItem(position).stringResId) ?: ""
            itemCard.setOnSingleClickListener { listener(getItem(position)) }
        }
        if (position == 0) view?.layoutDirection = View.LAYOUT_DIRECTION_LOCALE
        return view
    }


    override fun getItem(position: Int): HomeTileModel {
        return homeTilesList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getCount(): Int {
        return homeTilesList.size
    }

}

private class ViewHolder(view: View) {
    val itemCard: CardView = view.findViewById(R.id.homeItemCard)
    var tileIcon: ImageView = view.findViewById(R.id.ivTileIcon)
    var tileTitle: TextView = view.findViewById(R.id.tvTileText)
}

