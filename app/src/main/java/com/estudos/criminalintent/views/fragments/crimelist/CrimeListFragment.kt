package com.estudos.criminalintent.views.fragments.crimelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.estudos.criminalintent.adapters.CrimeListAdapter
import com.estudos.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.launch

class CrimeListFragment: Fragment() {

    private val crimeListViewModel: CrimeListViewModel by viewModels()

    /**
     * É criado um _binding e um binding para possibilitar que nenhuma view esteja salvo na memória
     * no momento em que este fragment for destruido
     *
     * _binding é usado para receber o inflate do fragment e ser excluido da memoria por onDestroyView
     *
     * binding é usado para manipular os elementos de visualização, e só é usado quando _binding não
     * é nulo, pois só é usado depois que _binding é inflado em onCreateView
     */
    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding é atualmente nulo, provavelmente a view não está visível"
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentCrimeListBinding.inflate(layoutInflater, container, false)

        binding.recyclerViewCrimeList.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                val crimes = crimeListViewModel.loadCrimes()
                binding.recyclerViewCrimeList.adapter = CrimeListAdapter(crimes)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}