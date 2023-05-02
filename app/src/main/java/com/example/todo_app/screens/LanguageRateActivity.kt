package com.example.todo_app.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todo_app.R
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import  android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.util.Locale

class LanguageRateActivity : AppCompatActivity() {
    lateinit var nbr_1: EditText
    lateinit var nbr_2: EditText
    lateinit var nbr_3: EditText
    lateinit var btn_calc: Button
    private lateinit var txt_lng:Button
    private lateinit var  editor:SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_rate)

        LoadLocal();
        txt_lng= findViewById(R.id.txt_lng)


        nbr_1 = findViewById(R.id.nbr1)
        nbr_2 = findViewById(R.id.nbr2)
        nbr_3 = findViewById(R.id.nbr3)
        btn_calc = findViewById(R.id.btncalc)

        txt_lng.setOnClickListener({
            openDialogforLanguageChange();
        })


        btn_calc.setOnClickListener {


            val nb1 = nbr_1.text.toString().trim()
            val nb2 = nbr_2.text.toString().trim()
            val nb3 = nbr_3.text.toString().trim()

            if (nb1.isEmpty() &&  nb2.isEmpty() && nb3.isEmpty()) {

                Toast.makeText(this, getString(R.string.txt_lng), Toast.LENGTH_SHORT)
                    .show()

            }
            else {
                val nbr1 = nbr_1.text.toString().toDouble()
                val nbr2 = nbr_2.text.toString().toDouble()
                val nbr3 = nbr_3.text.toString().toDouble()
                val moy = ((nbr1 + nbr2 + nbr3) / 3)
                moy.toString()
                val intent = Intent(this@LanguageRateActivity, FirstResultActivity::class.java)
                intent.putExtra("result", moy)

                moy.toString()

                startActivity(intent)
            }
        }
    }

    private fun openDialogforLanguageChange(){
        val list = arrayOf("English","fr-rCA","ar-rDZ")
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("select Language")
        alertDialog.setSingleChoiceItems(list,-1,DialogInterface.OnClickListener{dialog, i ->
            if (i==0){
                setLocale("En")
                recreate()
            }
            else if (i==1){
                setLocale("Fr")
                recreate()
            }
            else if (i==2){
                setLocale("Ar")
                recreate()
            }
        })
        alertDialog.setNeutralButton("cancel",DialogInterface.OnClickListener{dialog,which ->
            dialog.cancel()
        })
        val mDialog = alertDialog.create()
        mDialog.show()
    }

    private fun setLocale(language:String){
        val local = Locale(language)
        Locale.setDefault(local)
        val config= Configuration()
        config.locale = local;
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)
        editor = getSharedPreferences("",Context.MODE_PRIVATE).edit()
        editor.putString("selected_languge",language)
        editor.apply()
    }
    private fun LoadLocal(){
        sharedPreferences = getSharedPreferences("Settings",Activity.MODE_PRIVATE)
//        val language = sharedPreferences.getString ( "selected_languge", " ")
//        setLocale(language)
    }
}