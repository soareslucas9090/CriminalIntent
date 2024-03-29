package com.estudos.criminalintent.views.fragments.crimelist

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.estudos.criminalintent.R
import com.estudos.criminalintent.adapters.CrimeListAdapter
import com.estudos.criminalintent.data.Crime
import com.estudos.criminalintent.databinding.FragmentCrimeListBinding
import com.estudos.criminalintent.infrastructure.Constants
import kotlinx.coroutines.launch
import java.util.UUID

class CrimeListFragment : Fragment() {

    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val crimeListViewModel: CrimeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)

        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.buttonAddCrime.setOnClickListener {
            showNewCrime()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeListViewModel.crimes.collect { crimes ->
                    Log.d("TESTE", "$crimes")
                    binding.crimeRecyclerView.adapter =
                        CrimeListAdapter(crimes) { crimeId ->
                            findNavController().navigate(
                                CrimeListFragmentDirections.showCrimeDetail(crimeId)
                            )
                        }
                }
            }
        }
    }

    override fun onResume() {

        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
            crimeListViewModel.crimes.collect { crimes ->
                if (crimes.isEmpty()) {
                    binding.textEmptyCrimeList.visibility = View.VISIBLE
                    binding.buttonAddCrime.visibility = View.VISIBLE
                    binding.crimeRecyclerView.visibility = View.GONE
                } else {
                    binding.textEmptyCrimeList.visibility = View.GONE
                    binding.buttonAddCrime.visibility = View.GONE
                    binding.crimeRecyclerView.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                showNewCrime()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showNewCrime() {
        findNavController().navigate(
            CrimeListFragmentDirections.showCrimeDetail(UUID.fromString("00000000-0000-0000-0000-000000000000"))
        )
    }

}