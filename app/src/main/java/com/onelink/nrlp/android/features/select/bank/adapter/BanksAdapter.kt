package com.onelink.nrlp.android.features.select.bank.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.BankItemBinding
import com.onelink.nrlp.android.features.select.bank.model.BankDetailsModel
import kotlinx.android.synthetic.main.city_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class BanksAdapter(
    private val cities: ArrayList<BankDetailsModel>,
    private val listener: (BankDetailsModel) -> Unit
): RecyclerView.Adapter<BanksAdapter.CitiesViewHolder>() , Filterable {
    private var searchCities = ArrayList<BankDetailsModel>(cities)
    override fun getItemCount(): Int = searchCities.size

    fun addItems(newList: ArrayList<BankDetailsModel>){
        cities.addAll(newList)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CitiesViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.bank_item,
            parent, false
        )
    )

    class CitiesViewHolder(val bankItemBinding: BankItemBinding) :
        RecyclerView.ViewHolder(bankItemBinding.root)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filteredList = ArrayList<BankDetailsModel>()
                if(constraint.isBlank() or constraint.isEmpty()) {
                    filteredList.addAll(cities)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    cities.forEach {
                        if(it.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            filteredList.add(it)
                        }
                    }
                }
                val result = FilterResults()
                result.values = filteredList
                return result
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                searchCities.clear()
                searchCities.addAll(results?.values as ArrayList<BankDetailsModel>)
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.bankItemBinding.bank = searchCities[position]
        holder.itemView.setOnClickListener {
            listener(searchCities[position])
        }
        holder.bankItemBinding.root.textViewCity.setOnClickListener {
            listener(searchCities[position])
        }
    }
}