package com.estudos.criminalintent

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.estudos.criminalintent.views.fragments.crimedetail.CrimeDetailFragment
import org.junit.Before
import org.junit.Test

class CrimeDetailFragmentTest {

    private lateinit var scenario: FragmentScenario<CrimeDetailFragment>

    @Before
    fun setUp() {
        // Inicializando o FragmentScenario para o CrimeDetailFragment
        scenario = launchFragmentInContainer()
    }

    @Test
    fun AlterObjectCrimeOnCrimeDetailFragmentTest() {
        val newTitle = "Titulo Teste"
        onView(withId(R.id.checkBox_crime_solved)).perform(click())
        onView(withId(R.id.editText_crime_title)).perform(typeText(newTitle))

        //Futuramente, esta função será privada para evitar erros de segurança
        //Somente será pública enquanto estiver sendo testada
        //Descomente esta linha quando não for testar mais /*
        scenario.onFragment{
            val crime = it.getCrimeForTest()
            assert(crime.title == newTitle)
            assert(crime.isSolved)
        }
        //Descomente esta linha quando não for testar mais */
    }

}