package com.estudos.criminalintent.infrastructure

import android.icu.text.DateFormat

class Constants private constructor() {

    object FORMATS{
        val dateFormat: DateFormat = DateFormat.getPatternInstance("E dd 'de' MMM 'de' YYYY")
        val timeFormat: DateFormat = DateFormat.getPatternInstance(DateFormat.HOUR_MINUTE)
    }

}