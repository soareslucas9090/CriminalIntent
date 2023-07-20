package com.estudos.criminalitent.views.fragments.crimelist

import androidx.lifecycle.ViewModel
import com.estudos.criminalitent.data.Crime
import java.util.Date
import java.util.UUID

class CrimeListViewModel: ViewModel() {

    val crimes = mutableListOf<Crime>()

    init{
        for (i in 0 until 100 ) {
            val crime = Crime(
                id = UUID.randomUUID(),
                title = "Crime #$i",
                date = Date(),
                isSolved = i % 2 == 0,
                requiresPolice = i % 3 == 1
            )
            crimes.add(crime)
        }
    }

}