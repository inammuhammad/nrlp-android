package com.onelink.nrlp.android.features.faqs.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.features.faqs.model.FAQAdapterModel
import kotlinx.android.synthetic.main.recyclerview_cell.view.*
import java.util.*


class RVAdapter(private val itemsCells: ArrayList<FAQAdapterModel>) :
    RecyclerView.Adapter<RVAdapter.ViewHolder>() {
    lateinit var context: Context
    private var lastExpandedIndex: Int? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_cell, parent, false)
        val vh = ViewHolder(v)
        context = parent.context
        return vh
    }

    override fun getItemCount(): Int {
        return itemsCells.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val faqAdapterModel = itemsCells[position]
        holder.itemView.question_textview.text = faqAdapterModel.faqsModel.question
        holder.itemView.answer_textview.text = faqAdapterModel.faqsModel.answer
        if (faqAdapterModel.isExpanded) {
            holder.itemView.answer_layout.visibility = View.VISIBLE
            holder.itemView.arrow.setImageResource(R.drawable.ic_up_arrow)
        } else {
            holder.itemView.answer_layout.visibility = View.GONE
            holder.itemView.arrow.setImageResource(R.drawable.ic_down)
        }

        holder.itemView.question_layout.setOnClickListener {
            if (holder.itemView.answer_layout.visibility == View.VISIBLE) {
                faqAdapterModel.isExpanded = false
            } else {
                faqAdapterModel.isExpanded = true
                if (lastExpandedIndex != position)
                    itemsCells[lastExpandedIndex ?: 0].isExpanded = false
                lastExpandedIndex = position
            }
            notifyDataSetChanged()
        }
    }
}