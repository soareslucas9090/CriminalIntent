package com.estudos.criminalintent.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.estudos.criminalintent.data.Crime
import com.estudos.criminalintent.databinding.ViewListItemCrimeBinding
import com.estudos.criminalintent.databinding.ViewListItemCrimePoliceBinding

open class Holder(val binding: ViewBinding): RecyclerView.ViewHolder(binding.root){
    open fun bind(crime: Crime) {}
}
class CrimeHolder (val bindingCrime: ViewListItemCrimeBinding): Holder(bindingCrime) {

    override fun bind(crime: Crime){
        bindingCrime.textViewCrimeTitle.text = crime.title
        bindingCrime.textViewCrimeDate.text = crime.date.toString()
        bindingCrime.imageSolved.isVisible = crime.isSolved

        bindingCrime.root.setOnClickListener{
            Toast.makeText(bindingCrime.root.context, "${crime.title} clicado", Toast.LENGTH_SHORT).show()
        }
    }

}

class CrimePoliceHolder (val bindingCrimePolice: ViewListItemCrimePoliceBinding): Holder(bindingCrimePolice) {

    override fun bind(crime: Crime){
        bindingCrimePolice.textViewCrimeTitle.text = crime.title
        bindingCrimePolice.textViewCrimeDate.text = crime.date.toString()
        bindingCrimePolice.imageSolved.isVisible = crime.isSolved

        bindingCrimePolice.imagePolice.setOnClickListener{
            Toast.makeText(bindingCrimePolice.root.context, "Ligue para policia", Toast.LENGTH_SHORT).show()
        }

        bindingCrimePolice.root.setOnClickListener{
            Toast.makeText(bindingCrimePolice.root.context, "${crime.title} clicado", Toast.LENGTH_SHORT).show()
        }
    }

}

class CrimeListAdapter (private val crimes: List<Crime>): RecyclerView.Adapter<Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) {
            val binding = ViewListItemCrimeBinding.inflate(inflater, parent, false)
            CrimeHolder(binding)
        } else{
            val binding = ViewListItemCrimePoliceBinding.inflate(inflater, parent, false)
            CrimePoliceHolder(binding)
        }
    }

    override fun getItemViewType(position: Int) = if (crimes[position].requiresPolice) 1 else 0
    override fun getItemCount() = crimes.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime)
    }


}