package com.estudos.criminalintent.views.fragments.crimelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.estudos.criminalintent.data.Crime
import com.estudos.criminalintent.infrastructure.CrimeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()

    val crimes = mutableListOf<Crime>()

    init {
        viewModelScope.launch {
            crimes.addAll(loadCrimes())
        }
    }

    suspend fun loadCrimes(): List<Crime> {
        return crimeRepository.getCrimes()
    }

}