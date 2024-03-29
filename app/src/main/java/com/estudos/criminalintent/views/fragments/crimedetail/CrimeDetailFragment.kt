package com.estudos.criminalintent.views.fragments.crimedetail

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.estudos.criminalintent.R
import com.estudos.criminalintent.data.Crime
import com.estudos.criminalintent.databinding.FragmentCrimeDetailBinding
import com.estudos.criminalintent.infrastructure.Constants
import com.estudos.criminalintent.views.fragments.crimedetail.datepicker.DatePickerFragment
import com.estudos.criminalintent.views.fragments.crimedetail.timepicker.TimePickerFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date

class CrimeDetailFragment : Fragment() {

    private val dateFormatReport = Constants.FORMATS.DATEFORMATREPORT

    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: CrimeDetailFragmentArgs by navArgs()

    private lateinit var callback: OnBackPressedCallback

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }

    private val selectSuspect = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri ->
        uri?.let { parseContactSelection(it) }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                selectSuspect.launch(null)
            } else {
                Toast.makeText(requireContext(), R.string.contact_permission_denied, Toast.LENGTH_LONG).show()
                binding.buttonCrimeSuspect.isEnabled = false
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val titleText = binding.editTextCrimeTitle.text.toString()
            if (titleText.isNullOrEmpty()) {
                Toast.makeText(requireContext(), R.string.toast_title_required, Toast.LENGTH_SHORT)
                    .show()
            } else {
                callback.isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentCrimeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            editTextCrimeTitle.doOnTextChanged { text, _, _, _ ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
            }

            checkBoxCrimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }

            checkBoxCrimeRequiresPolice.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(requiresPolice = isChecked)
                }
            }

            buttonCrimeSuspect.setOnClickListener {
                if (
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_CONTACTS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                } else {
                    selectSuspect.launch(null)
                }
            }

            buttonCallCrimeSuspect.setOnClickListener {
                val suspectNumber = crimeDetailViewModel.crime.value?.numberSuspect

                if (suspectNumber.isNullOrEmpty()){
                    Toast.makeText(requireContext(),R.string.contact_dont_have_number,Toast.LENGTH_SHORT).show()
                } else {
                    val uri = Uri.parse("tel:$suspectNumber")
                    val dialIntent = Intent(Intent.ACTION_DIAL, uri)
                    startActivity(dialIntent)
                }
            }

            val selectSuspectIntent = selectSuspect.contract.createIntent(
                requireContext(),
                null
            )
            buttonCrimeSuspect.isEnabled = canResolveIntent(selectSuspectIntent)

        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.crime.collect { crime ->
                    crime?.let { updateUi(it) }
                }
            }
        }

        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->
            val newDate =
                bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            crimeDetailViewModel.updateCrime { it.copy(date = newDate) }
        }

        setFragmentResultListener(
            TimePickerFragment.REQUEST_KEY_TIME
        ) { _, bundle ->
            val newTime =
                bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as Date
            crimeDetailViewModel.updateCrime { it.copy(time = newTime) }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_crime -> {
                showDeleteConfirm()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteConfirm() {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(getString(R.string.delete_crime_or_not, binding.editTextCrimeTitle.text))
        builder.setMessage(getString(R.string.delete_this_crime))

        builder.setPositiveButton(getString(R.string.delete_crime_yes)) { _, _ ->
            deleteCrime()
        }

        builder.setNegativeButton(getString(R.string.delete_crime_no)) { _, _ -> }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun deleteCrime() {
        viewLifecycleOwner.lifecycleScope.launch {
            crimeDetailViewModel.deleteCrime()
            requireActivity().onBackPressed()
        }
    }

    private fun updateUi(crime: Crime) {
        val dateFormat = Constants.FORMATS.DATEFORMAT
        val timeFormat = Constants.FORMATS.TIMEFORMAT

        binding.apply {
            if (editTextCrimeTitle.text.toString() != crime.title) {
                editTextCrimeTitle.setText(crime.title)
            }

            buttonCrimeDate.text = dateFormat.format(crime.date)
            buttonCrimeDate.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectDate(crime.date)
                )
            }

            buttonCrimeTime.text = timeFormat.format(crime.time)
            buttonCrimeTime.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectTime(crime.time)
                )
            }

            buttonCrimeReport.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
                }

                val chooserIntent =
                    Intent.createChooser(reportIntent, getString(R.string.send_report))

                startActivity(chooserIntent)
            }

            buttonCrimeSuspect.text = crime.suspect.ifEmpty {
                getString(R.string.crime_suspect_text)
            }


            checkBoxCrimeSolved.isChecked = crime.isSolved
            checkBoxCrimeRequiresPolice.isChecked = crime.requiresPolice
        }
    }

    private fun getCrimeReport(crime: Crime): String {
        val solvedString =
            if (crime.isSolved) {
                getString(R.string.crime_report_solved)
            } else {
                getString(R.string.crime_report_unsolved)
            }

        val dateString = dateFormatReport.format(crime.date)

        val suspectText =
            if (crime.suspect.isBlank()) {
                getString(R.string.crime_report_no_suspect)
            } else {
                getString(R.string.crime_report_suspect, crime.suspect)
            }

        return getString(
            R.string.crime_report,
            crime.title, dateString, solvedString, suspectText
        )
    }

    private fun parseContactSelection(contactUri: Uri) {
        val queryFields = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID
        )

        val queryCursor = requireContext().contentResolver.query(
            contactUri,
            queryFields,
            null,
            null,
            null
        )

        queryCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val suspect = cursor.getString(0)
                val numberSuspect = getNumberFromContact(cursor.getString(1))
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(suspect = suspect, numberSuspect = numberSuspect)
                }
            }
        }
    }

    private fun getNumberFromContact(contactId: String): String {
        val queryCursor = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(contactId),
            null
        )

        var phoneNumber = ""

        queryCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                if (cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) >= 0) {
                    val columnIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    phoneNumber = cursor.getString(columnIndex)
                }
            }
        }

        queryCursor?.close()
        return phoneNumber.ifEmpty { "" }
    }


    private fun canResolveIntent(intent: Intent): Boolean {
        val packageManager = requireActivity().packageManager
        val resolvedActivity =
            packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolvedActivity != null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

