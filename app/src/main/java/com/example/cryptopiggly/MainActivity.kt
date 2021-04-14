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
import android.view.View
import java.util.Locale.FRANCE
import java.util.Locale.FRENCH


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
        imgset = binding.settingsIcon

        imgset.setOnClickListener {

            val settings = Intent(this, SettingsActivity::class.java)

            //playSound()
            startActivity(settings)

        }

        binding.button.setOnClickListener {

            var skvlozek: Int = (binding.skupniVlozek.text).toString().toInt()
            var trvred: Int = (binding.trenutnaVrednost.text).toString().toInt()
            var maxvred: Int = (binding.maxVrednost.text).toString().toInt()
            var profit: Int = trvred - skvlozek
            var maxprof: Int = (binding.maxProfit.text).toString().toInt()
            val vib = (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)


            vib.vibrate(VibrationEffect.createOneShot(100,3))
           // mMediaPlayer!!.stop()

            //val num = 1000

            //binding.edittest.setText(NumberFormat.getNumberInstance(ULocale.ITALY).format(num).toString())

            loadColor()

            if (trvred >= maxvred) {
                playSound2()
            }
            else
            {
                playSound()
            }



            binding.trenutniProfit.setText(NumberFormat.getNumberInstance(ULocale.ITALY).format(profit).toString())

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


       // binding.textView.setText(NumberFormat.getNumberInstance(ULocale.ITALY).format(sv2).toString())

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
}