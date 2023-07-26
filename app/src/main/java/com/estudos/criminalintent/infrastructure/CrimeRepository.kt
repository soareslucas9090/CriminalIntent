package com.estudos.criminalintent.infrastructure

import android.content.Context
import androidx.room.Room
import com.estudos.criminalintent.data.Crime
import com.estudos.criminalintent.database.crime.CrimeDataBase
import java.util.UUID

private const val DATABASE_NAME = "BDCrimeIntent"

class CrimeRepository private constructor (context: Context){

    private val database: CrimeDataBase = Room.databaseBuilder(context.applicationContext,
            CrimeDataBase::class.java,
            DATABASE_NAME).build()

    suspend fun getCrimes(): List<Crime> = database.crimeDao().getCrimes()

    suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository =
            INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")

    }

}