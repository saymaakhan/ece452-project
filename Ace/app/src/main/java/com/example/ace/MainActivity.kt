package com.example.ace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.R.attr.value
import android.content.Intent
import android.widget.Button
import androidx.core.view.WindowCompat
import com.example.ace.databinding.ActivityMainBinding
import com.example.ace.ui.login.LoginActivity
import com.example.ace.MainActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var sploginbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sploginbutton.setOnClickListener {
            val myIntent = Intent(this, LoginActivity::class.java)
            this.startActivity(myIntent)
        }
    }
}