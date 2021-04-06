package com.example.cryptopiggly

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptopiggly.databinding.ActivityMainBinding
import java.lang.Boolean.FALSE


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var img: ImageView
    private lateinit var cpAnimation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.maxVrednost.isFocusable = FALSE
        binding.trenutniProfit.isFocusable = FALSE
        binding.maxProfit.isFocusable = FALSE


        img = binding.imageView
        img.setImageResource(R.drawable.cpanime)
        cpAnimation = img.drawable as AnimationDrawable

        loadData()

        binding.button.setOnClickListener {

            var skvlozek: Int = (binding.skupniVlozek.text).toString().toInt()
            var trvred: Int = (binding.trenutnaVrednost.text).toString().toInt()
            var maxvred: Int = (binding.maxVrednost.text).toString().toInt()
            var profit: Int = trvred - skvlozek
            var maxprof: Int = (binding.maxProfit.text).toString().toInt()


            loadColor()

            binding.trenutniProfit.setText(profit.toString())
            //binding.imageView.visibility = View.INVISIBLE

            cpAnimation.stop()
            cpAnimation.start()



            saveData()

        }


    }

    private fun saveData(){
        val insertedText: String = binding.skupniVlozek.text.toString()
        val insertedText2: String = binding.trenutnaVrednost.text.toString()
        val insertedText3: String = binding.maxVrednost.text.toString()
        val insertedText4: String = binding.trenutniProfit.text.toString()
        val insertedText5: String = binding.maxProfit.text.toString()
        //binding.textView7.text = insertedText


        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPref",
            Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.apply {
            putString("STRING_KEY", insertedText)
            putString("STRING_KEY_2", insertedText2)
            putString("STRING_KEY_3", insertedText3)
            putString("STRING_KEY_4", insertedText4)
            putString("STRING_KEY_5", insertedText5)

        }.apply()

       // Toast.makeText(this,"Data saved",Toast.LENGTH_SHORT).show()
    }

    private fun loadData(){

        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPref",
            Context.MODE_PRIVATE)

        val savedString = sharedPreferences.getString("STRING_KEY", null)
        val savedString2 = sharedPreferences.getString("STRING_KEY_2", null)
        val savedString3 = sharedPreferences.getString("STRING_KEY_3", null)
        val savedString4 = sharedPreferences.getString("STRING_KEY_4", null)
        val savedString5 = sharedPreferences.getString("STRING_KEY_5", null)

        binding.skupniVlozek.setText(savedString)
        binding.trenutnaVrednost.setText(savedString2)
        binding.maxVrednost.setText(savedString3)
        binding.trenutniProfit.setText(savedString4)
        binding.maxProfit.setText(savedString5)

        loadColor()

    }

    private fun loadColor(){

        var skvlozek: Int = (binding.skupniVlozek.text).toString().toInt()
        var trvred: Int = (binding.trenutnaVrednost.text).toString().toInt()
        var maxvred: Int = (binding.maxVrednost.text).toString().toInt()
        var profit: Int = trvred - skvlozek
        var maxprof: Int = (binding.maxProfit.text).toString().toInt()

        if (trvred >= maxvred){
            binding.maxVrednost.setText(trvred.toString())
            binding.trenutnaVrednost.setTextColor(resources.getColor(R.color.cifre_1))
            binding.trenutnaVrednost.setBackground(resources.getDrawable(R.drawable.custom_etxt))
        }
        else
        {
            binding.trenutnaVrednost.setTextColor(resources.getColor(R.color.cifre_2))
            binding.trenutnaVrednost.setBackground(resources.getDrawable(R.drawable.custom_etxt_neg))

        }

        if (profit >= maxprof){
            binding.maxProfit.setText(profit.toString())
            binding.trenutniProfit.setTextColor(resources.getColor(R.color.cifre_1))
            binding.trenutniProfit.setBackground(resources.getDrawable(R.drawable.custom_etxt))
        }
        else
        {
            binding.trenutniProfit.setTextColor(resources.getColor(R.color.cifre_2))
            binding.trenutniProfit.setBackground(resources.getDrawable(R.drawable.custom_etxt_neg))
        }
    }
}