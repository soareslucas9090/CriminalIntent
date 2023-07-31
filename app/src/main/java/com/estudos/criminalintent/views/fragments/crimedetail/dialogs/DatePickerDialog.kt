package com.estudos.criminalintent.views.fragments.crimedetail.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavArgs
import java.util.Calendar

class DataPickerDialog: DialogFragment() {

    private val args: DataPickerDialogArgs by NavArgs

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog( requireContext(), null, initialYear, initialMonth, initialDay)
    }


}