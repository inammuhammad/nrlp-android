package com.onelink.nrlp.android.features.select.country.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.CountryItemBinding
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import kotlinx.android.synthetic.main.country_item.view.*

class CountryAdapter(
    private val countries: List<CountryCodeModel>,
    private val listener: (CountryCodeModel) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    override fun getItemCount() = countries.size

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
        holder.countryItemBinding.country = countries[position]
        holder.itemView.setOnClickListener {
            listener(countries[position])
        }
        holder.countryItemBinding.root.textViewCountry.setOnClickListener {
            listener(countries[position])
        }
    }

}