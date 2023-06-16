package com.estudos.criminalitent.views.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.estudos.criminalitent.R
import com.estudos.criminalitent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    
}