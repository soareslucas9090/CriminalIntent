package com.estudos.criminalintent.infrastructure

import android.icu.text.DateFormat

class Constants private constructor() {

    object FORMATS{
        val DATEFORMAT: DateFormat = DateFormat.getPatternInstance("E dd MMM YYYY")
        val TIMEFORMAT: DateFormat = DateFormat.getPatternInstance(DateFormat.HOUR_MINUTE)
        val DATEFORMATREPORT: DateFormat = DateFormat.getPatternInstance("EEE, MMM, dd")
    }

}