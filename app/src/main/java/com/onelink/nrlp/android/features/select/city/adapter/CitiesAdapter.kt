package com.onelink.nrlp.android.features.select.city.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.CityItemBinding
import com.onelink.nrlp.android.features.select.city.model.CitiesModel
import kotlinx.android.synthetic.main.city_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class CitiesAdapter(
    private val cities: ArrayList<CitiesModel>,
    private val listener: (CitiesModel) -> Unit
): RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>() , Filterable {
    private var searchCities = ArrayList<CitiesModel>(cities)
    override fun getItemCount(): Int = searchCities.size

    fun addItems(newList: ArrayList<CitiesModel>){
        cities.addAll(newList)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CitiesViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.city_item,
            parent, false
        )
    )

    class CitiesViewHolder(val cityItemBinding: CityItemBinding) :
        RecyclerView.ViewHolder(cityItemBinding.root)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filteredList = ArrayList<CitiesModel>()
                if(constraint.isBlank() or constraint.isEmpty()) {
                    filteredList.addAll(cities)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    cities.forEach {
                        if(it.city.toLowerCase(Locale.ROOT).contains(filterPattern)) {
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
                searchCities.addAll(results?.values as ArrayList<CitiesModel>)
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: CitiesAdapter.CitiesViewHolder, position: Int) {
        holder.cityItemBinding.city = searchCities[position]
        holder.itemView.setOnClickListener {
            listener(searchCities[position])
        }
        holder.cityItemBinding.root.textViewCity.setOnClickListener {
            listener(searchCities[position])
        }
    }
}