package com.estudos.criminalintent.views.fragments.crimedetail.timepicker

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar

class TimePickerFragment : DialogFragment() {

    private val args: TimePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timeListener = TimePickerDialog.OnTimeSetListener {_, hour: Int, minute:Int ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            val resultTime = calendar.time

            setFragmentResult(REQUEST_KEY_TIME, bundleOf(BUNDLE_KEY_TIME to resultTime))
        }


        val calendar = Calendar.getInstance()
        calendar.time = args.crimeTime
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            timeListener,
            hour,
            minute,
            true
        )
    }

    companion object {
        const val REQUEST_KEY_TIME = "REQUEST_KEY_TIME"
        const val BUNDLE_KEY_TIME = "BUNDLE_KEY_TIME"
    }

}
