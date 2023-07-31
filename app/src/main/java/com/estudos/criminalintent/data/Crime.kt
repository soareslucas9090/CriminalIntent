package com.estudos.criminalintent.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import java.util.Date

@Entity
data class Crime(@PrimaryKey val id: UUID, val title: String, val date: Date, val isSolved: Boolean, val requiresPolice: Boolean) {
}
