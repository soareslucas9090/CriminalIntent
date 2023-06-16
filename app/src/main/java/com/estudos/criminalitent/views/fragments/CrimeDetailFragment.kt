package com.estudos.criminalitent.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.estudos.criminalitent.data.Crime
import com.estudos.criminalitent.databinding.FragmentCrimeDetailBinding
import java.util.Date
import java.util.UUID

    class CrimeDetailFragment: Fragment() {

        /**
         * É criado um _binding e um binding para possibilitar que nenhumA view esteja salvo na memória
         * no momento em que este fragment for destruido
         * _binding é usado par receber o inflate do fragment e ser excluido da memoria por onDestroyView
         *
         * binding é usado para manipular os elementos de visualização, e só é usado quando _binding não
         * é nulo, pois só é usado, depois que _binding é inflado em onCreateView
         */
        private var _binding: FragmentCrimeDetailBinding? = null
        private val binding
            get() = checkNotNull(_binding) {
                "Binding é atualmente nulo, provavelmente a view não está visível"
            }
        private lateinit var crime: Crime

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            crime = Crime(
                id = UUID.randomUUID(),
                title = "",
                date = Date(),
                isSolved = false
            )
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            _binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            /** *Criação de um listener para alteração do objeto Crime de acordo
             * com as alterações feitas
             * */
            binding.apply {
                editTextCrimeTitle.doOnTextChanged { text, _, _, _ ->
                    crime = crime.copy(title = text.toString())
                }

                buttonCrimeDate.apply {
                    text = crime.date.toString()
                    isEnabled = false
                }

                checkBoxCrimeSolved.setOnCheckedChangeListener { _, isChecked ->
                    crime = crime.copy(isSolved = isChecked)
                }


            }

        }

        override fun onDestroyView() {

            super.onDestroyView()
            _binding = null
        }

        /**
         * Função usada para teste com Espresso
         * Atribuir modificador privado quando não estiver sendo usada
         */
        fun getCrimeForTest(): Crime{
            return crime
        }

    }