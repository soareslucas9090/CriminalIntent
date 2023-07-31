package com.estudos.criminalintent.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.estudos.criminalintent.data.Crime
import com.estudos.criminalintent.databinding.ListItemCrimeBinding
import com.estudos.criminalintent.databinding.ListItemCrimePoliceBinding
import com.estudos.criminalintent.infrastructure.Constants
import java.util.UUID

/**
 * Esta classe serve como uma classe mãe para criar diferentes tipos de viewholders baseados
 * em diferentes layouts
 *
 * @property binding
 */
open class Holder(open val binding: ViewBinding): RecyclerView.ViewHolder(binding.root){

    val dateFormat = Constants.FORMATS.dateFormat
    open fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit) {}
}

class CrimeHolder (override val binding: ListItemCrimeBinding): Holder(binding) {

    override fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit){
        binding.textViewCrimeTitle.text = crime.title
        binding.textViewCrimeDate.text = dateFormat.format(crime.date)
        binding.imageSolved.isVisible = crime.isSolved

        binding.root.setOnClickListener{
            onCrimeClicked(crime.id)
        }
    }

}

class CrimePoliceHolder (override val binding: ListItemCrimePoliceBinding): Holder(binding) {

    override fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit){
        binding.textViewCrimeTitle.text = crime.title
        binding.textViewCrimeDate.text = dateFormat.format(crime.date)
        binding.imageSolved.isVisible = crime.isSolved

        binding.imagePolice.setOnClickListener{
            Toast.makeText(binding.root.context, "Ligue para policia", Toast.LENGTH_SHORT).show()
        }

        binding.root.setOnClickListener{
            onCrimeClicked(crime.id)
        }
    }

}


class CrimeListAdapter (private val crimes: List<Crime>, private val onCrimeClicked: (crimeId: UUID) -> Unit): RecyclerView.Adapter<Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) {
            val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
            CrimeHolder(binding)
        } else{
            val binding = ListItemCrimePoliceBinding.inflate(inflater, parent, false)
            CrimePoliceHolder(binding)
        }
    }

    override fun getItemViewType(position: Int) = if (crimes[position].requiresPolice) 1 else 0
    override fun getItemCount() = crimes.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime, onCrimeClicked)
    }


}