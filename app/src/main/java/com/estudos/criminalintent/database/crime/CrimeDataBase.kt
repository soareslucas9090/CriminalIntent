package com.estudos.criminalintent.database.crime

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.estudos.criminalintent.data.Crime

@Database(entities = [ Crime::class ], version=1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDataBase:RoomDatabase() {

    abstract fun crimeDao():CrimeDao

}