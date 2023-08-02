package com.estudos.criminalintent.views.fragments.crimedetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.estudos.criminalintent.infrastructure.CrimeRepository
import com.estudos.criminalintent.data.Crime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class CrimeDetailViewModel(crimeId: UUID) : ViewModel() {
    private val crimeRepository = CrimeRepository.get()

    private val _crime: MutableStateFlow<Crime?> = MutableStateFlow(null)
    val crime: StateFlow<Crime?> = _crime.asStateFlow()
    private var crimeAlreadyExists = false

    init {

        viewModelScope.launch {
            if (crimeId == UUID.fromString("00000000-0000-0000-0000-000000000000")) {
                val newCrime = Crime(
                    id = UUID.randomUUID(),
                    title = "",
                    date = Date(),
                    time = Date(),
                    isSolved = false,
                    requiresPolice = false,
                )
                _crime.value = newCrime
            } else {
                _crime.value = crimeRepository.getCrime(crimeId)
                crimeAlreadyExists = true
            }
        }
    }

    fun updateCrime(onUpdate: (Crime) -> Crime) {
        _crime.update { oldCrime ->
            oldCrime?.let { onUpdate(it) }
        }
    }

    suspend fun deleteCrime() {
        crime.value?.let { crimeRepository.deleteCrime(it) }
    }

    override fun onCleared() {
        super.onCleared()
        crime.value?.let {
            if (crimeAlreadyExists) {
                crimeRepository.updateCrime(it)
            } else {
                crimeRepository.addCrime(it)
            }
        }
    }
}

class CrimeDetailViewModelFactory(
    private val crimeId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeId) as T
    }
}
