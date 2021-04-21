package com.example.cryptopiggly

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.drawable.AnimationDrawable
import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.icu.util.ULocale
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptopiggly.databinding.ActivityMainBinding
import java.lang.Boolean.FALSE
import java.util.*
import android.media.MediaPlayer
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import java.io.IOException
import java.lang.Boolean.TRUE
import java.util.Locale.FRANCE
import java.util.Locale.FRENCH
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var img: ImageView
    private lateinit var imgset: ImageView
    private lateinit var cpAnimation: AnimationDrawable

    var mMediaPlayer: MediaPlayer? = null

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trenutnaVrednost.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                //Perform Code
                binding.imageView.callOnClick()
                closeKeyboard(binding.trenutnaVrednost)
                datumInUra()
                return@OnKeyListener true
            }
            false
        })

        binding.shareIcon.setOnClickListener {

            Toast.makeText(this, "Deli", Toast. LENGTH_SHORT).show()
        }

        binding.settingsIcon.setOnClickListener {

            Toast.makeText(this, "Nastavitve", Toast. LENGTH_SHORT).show()
        }



        val rocna_maxvred = intent.extras?.getString("key1")
        val rocni_maxprof = intent.extras?.getString("key2")

        binding.maxVrednost.isFocusable = FALSE
        binding.trenutniProfit.isFocusable = FALSE
        binding.maxProfit.isFocusable = FALSE


        img = binding.imageView
        img.setImageResource(R.drawable.cpanime)
        cpAnimation = img.drawable as AnimationDrawable


        loadData()



        if (rocna_maxvred!=null) {
            binding.maxVrednost.setText(rocna_maxvred)
        }


        if (rocni_maxprof!=null) {
            binding.maxProfit.setText(rocni_maxprof)
        }

        // Ikonca za nastavitve
        imgset = binding.manualEdit

        imgset.setOnClickListener {

            val settings = Intent(this, SettingsActivity::class.java)

            //playSound()
            startActivity(settings)

        }

        binding.imageView.setOnClickListener {

            var skvlozek: Int = (binding.skupniVlozek.text).toString().toInt()
            var trvred: Int = (binding.trenutnaVrednost.text).toString().toInt()
            var maxvred: Int = (binding.maxVrednost.text).toString().toInt()
            var profit: Int = trvred - skvlozek
            var maxprof: Int = (binding.maxProfit.text).toString().toInt()
            var razlikavred: Int = trvred - maxvred
            var razlikaprof: Int = profit - maxprof
            val vib = (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)


            vib.vibrate(VibrationEffect.createOneShot(100,3))


            binding.trenutniProfit.setText(profit.toString())
            datumInUra()
            rekord()



            if ((trvred >= maxvred) or (profit >= maxprof)){
                playSound2()
                binding.razlikaTxt.text = "="
                binding.razlikaTxt2.text = "="
            }
            else
            {
                playSound()
                binding.razlikaTxt.text = razlikavred.toString() + " €"
                binding.razlikaTxt2.text = razlikaprof.toString() + " €"
            }


            //binding.imageView.visibility = View.INVISIBLE

            cpAnimation.stop()
            cpAnimation.start()



            saveData()


        }


    }

    private fun saveData(){
        var skvlozek: Int = (binding.skupniVlozek.text).toString().toInt()
        val insertedText: String = skvlozek.toString()

        var trvred: Int = (binding.trenutnaVrednost.text).toString().toInt()
        val insertedText2: String = trvred.toString()

        var maxvred: Int = (binding.maxVrednost.text).toString().toInt()
        val insertedText3: String = maxvred.toString()

        var profit: Int = trvred - skvlozek
        val insertedText4: String = profit.toString()

        var maxprof: Int = (binding.maxProfit.text).toString().toInt()
        val insertedText5: String = maxprof.toString()
        //binding.textView7.text = insertedText

        var diu:String = (binding.Osvezeno.text).toString()
        val insertedText6:String = diu.toString()


        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPref",
                Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.apply {
            putString("STRING_KEY", insertedText)
            putString("STRING_KEY_2", insertedText2)
            putString("STRING_KEY_3", insertedText3)
            putString("STRING_KEY_4", insertedText4)
            putString("STRING_KEY_5", insertedText5)
            putString("STRING_KEY_6", insertedText6)

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
        val savedString6 = sharedPreferences.getString("STRING_KEY_6", null)



        binding.skupniVlozek.setText(savedString).toString()
        binding.trenutnaVrednost.setText(savedString2).toString()
        binding.maxVrednost.setText(savedString3).toString()
        binding.trenutniProfit.setText(savedString4).toString()
        binding.maxProfit.setText(savedString5).toString()
        binding.Osvezeno.setText(savedString6).toString()

        rekord()

    }

    private fun rekord(){

        var skvlozek: Int = (binding.skupniVlozek.text).toString().toInt()
        var trvred: Int = (binding.trenutnaVrednost.text).toString().toInt()
        var maxvred: Int = (binding.maxVrednost.text).toString().toInt()
        var profit: Int = trvred - skvlozek
        var maxprof: Int = (binding.maxProfit.text).toString().toInt()

        if (trvred >= maxvred){
            binding.maxVrednost.setText(trvred.toString())
         //   binding.trenutnaVrednost.setTextColor(resources.getColor(R.color.cifre_1))
            binding.trenutnaVrednost.setBackground(resources.getDrawable(R.drawable.custom_etxt))
            binding.imgStar.visibility = View.VISIBLE

        }
        else
        {
           // binding.trenutnaVrednost.setTextColor(resources.getColor(R.color.cifre_2))
            binding.trenutnaVrednost.setBackground(resources.getDrawable(R.drawable.custom_etxt_neg))
            binding.imgStar.visibility = View.INVISIBLE

        }

        if (profit >= maxprof){
            binding.maxProfit.setText(profit.toString())
            //binding.trenutniProfit.setTextColor(resources.getColor(R.color.cifre_1))
            binding.trenutniProfit.setBackground(resources.getDrawable(R.drawable.custom_etxt))
            binding.imgStar2.visibility = View.VISIBLE
        }
        else
        {
            //binding.trenutniProfit.setTextColor(resources.getColor(R.color.cifre_2))
            binding.trenutniProfit.setBackground(resources.getDrawable(R.drawable.custom_etxt_neg))
            binding.imgStar2.visibility = View.INVISIBLE
        }
    }

    fun playSound() {
       // if (mMediaPlayer == null) {
        mMediaPlayer?.stop()
        mMediaPlayer = MediaPlayer.create(this, R.raw.kovanci)
        mMediaPlayer!!.isLooping = false
        mMediaPlayer!!.start()
      //  } else mMediaPlayer!!.start()
    }

    fun playSound2() {
        //if (mMediaPlayer == null) {
        mMediaPlayer?.stop()
        mMediaPlayer = MediaPlayer.create(this, R.raw.kitaracp)
        mMediaPlayer!!.isLooping = false
        mMediaPlayer!!.start()
      //  } else mMediaPlayer!!.start()
    }

    private fun closeKeyboard(view: View) {
        val imm= getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun datumInUra() {

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val formatted = current.format(formatter)

        binding.Osvezeno.text = "Nazadnje osveženo: " + formatted.toString()
       // Toast.makeText(this, formatted, Toast.LENGTH_SHORT).show()
       // println("Current Date and Time is: $formatted")
    }


}