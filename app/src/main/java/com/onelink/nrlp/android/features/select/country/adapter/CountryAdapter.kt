package com.onelink.nrlp.android.features.select.country.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.CountryItemBinding
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import kotlinx.android.synthetic.main.country_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class CountryAdapter(
    private val countries: List<CountryCodeModel>,
    private val listener: (CountryCodeModel) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() , Filterable {

    private var searchCountires = ArrayList<CountryCodeModel>(countries)
    override fun getItemCount(): Int = searchCountires.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CountryViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.country_item,
            parent, false
        )
    )

    class CountryViewHolder(val countryItemBinding: CountryItemBinding) :
        RecyclerView.ViewHolder(countryItemBinding.root)

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.countryItemBinding.country = searchCountires[position]
        holder.itemView.setOnClickListener {
            listener(searchCountires[position])
        }
        holder.countryItemBinding.root.textViewCountry.setOnClickListener {
            listener(searchCountires[position])
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filteredList = ArrayList<CountryCodeModel>()
                    if(constraint.isBlank() or constraint.isEmpty()) {
                        filteredList.addAll(countries)
                    } else {
                        val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                        countries.forEach {
                            if(it.country.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                                filteredList.add(it)
                            }
                        }
                }
                val result = FilterResults()
                result.values = filteredList
                return result
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                searchCountires.clear()
                searchCountires.addAll(results?.values as ArrayList<CountryCodeModel>)
                notifyDataSetChanged()
            }
        }
    }

}