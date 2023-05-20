package com.estudos.criminalintent.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.estudos.criminalintent.R
import com.estudos.criminalintent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}