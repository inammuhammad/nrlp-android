package com.onelink.nrlp.android.features.select.generic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.BranchCenterItemBinding
import com.onelink.nrlp.android.databinding.CityItemBinding
import com.onelink.nrlp.android.features.select.city.model.CitiesModel
import com.onelink.nrlp.android.features.select.generic.model.BranchCenterModel
import com.onelink.nrlp.android.features.select.generic.model.BranchCenterRequestModel
import kotlinx.android.synthetic.main.city_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class BranchCenterAdapter(
    private val cities: ArrayList<BranchCenterModel>,
    private val listener: (BranchCenterModel) -> Unit
) : RecyclerView.Adapter<BranchCenterAdapter.CitiesViewHolder>(), Filterable {
    private var searchCities = ArrayList<BranchCenterModel>(cities)
    override fun getItemCount(): Int = searchCities.size

    fun addItems(newList: ArrayList<BranchCenterModel>) {
        cities.addAll(newList)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CitiesViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.branch_center_item,
            parent, false
        )
    )

    class CitiesViewHolder(val cityItemBinding: BranchCenterItemBinding) :
        RecyclerView.ViewHolder(cityItemBinding.root)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filteredList = ArrayList<BranchCenterModel>()
                if (constraint.isBlank() or constraint.isEmpty()) {
                    filteredList.addAll(cities)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    cities.forEach {
                        if (it.branchCenterName.toLowerCase(Locale.ROOT).contains(filterPattern)) {
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
                searchCities.addAll(results?.values as ArrayList<BranchCenterModel>)
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: BranchCenterAdapter.CitiesViewHolder, position: Int) {
        holder.cityItemBinding.branchCenter = searchCities[position]
        holder.itemView.setOnClickListener {
            listener(searchCities[position])
        }
        holder.cityItemBinding.root.textViewCity.setOnClickListener {
            listener(searchCities[position])
        }
    }
}