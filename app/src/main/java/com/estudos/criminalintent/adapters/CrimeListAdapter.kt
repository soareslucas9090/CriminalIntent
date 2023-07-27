package com.estudos.criminalintent.adapters

import android.icu.text.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.estudos.criminalintent.data.Crime
import com.estudos.criminalintent.databinding.ViewListItemCrimeBinding
import com.estudos.criminalintent.databinding.ViewListItemCrimePoliceBinding

open class Holder(open val binding: ViewBinding): RecyclerView.ViewHolder(binding.root){
    val df = DateFormat.getPatternInstance("E dd 'de' MMM 'de' YYYY")
    open fun bind(crime: Crime) {}
}
class CrimeHolder (override val binding: ViewListItemCrimeBinding): Holder(binding) {

    override fun bind(crime: Crime){
        binding.textViewCrimeTitle.text = crime.title
        binding.textViewCrimeDate.text = df.format(crime.date)
        binding.imageSolved.isVisible = crime.isSolved

        binding.root.setOnClickListener{
            Toast.makeText(binding.root.context, "${crime.title} clicado", Toast.LENGTH_SHORT).show()
        }
    }

}

class CrimePoliceHolder (override val binding: ViewListItemCrimePoliceBinding): Holder(binding) {

    override fun bind(crime: Crime){
        binding.textViewCrimeTitle.text = crime.title
        binding.textViewCrimeDate.text = df.format(crime.date)
        binding.imageSolved.isVisible = crime.isSolved

        binding.imagePolice.setOnClickListener{
            Toast.makeText(binding.root.context, "Ligue para policia", Toast.LENGTH_SHORT).show()
        }

        binding.root.setOnClickListener{
            Toast.makeText(binding.root.context, "${crime.title} clicado", Toast.LENGTH_SHORT).show()
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