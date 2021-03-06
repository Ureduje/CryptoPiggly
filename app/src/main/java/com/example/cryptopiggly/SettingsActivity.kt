package com.example.cryptopiggly

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptopiggly.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val maxvred = binding.maxvredset.text.toString()

            intent.putExtra("key1", maxvred)

            startActivity(intent)

            }

        binding.button3.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val maxprof = binding.maxprofset.text.toString()

            intent.putExtra("key2", maxprof)

            startActivity(intent)

        }


    }
}