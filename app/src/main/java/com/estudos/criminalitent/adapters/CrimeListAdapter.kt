package com.estudos.criminalitent.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.estudos.criminalitent.data.Crime
import com.estudos.criminalitent.databinding.ViewListItemCrimeBinding

class CrimeHolder (val binding: ViewListItemCrimeBinding): RecyclerView.ViewHolder(binding.root) {


}

class CrimeListAdapter (private val crimes: List<Crime>): RecyclerView.Adapter<CrimeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)
    }

    override fun getItemCount() = crimes.size

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.apply {
            binding.textViewCrimeTitle.text = crime.title
            binding.textViewCrimeDate.text = crime.date.toString()
        }
    }


}