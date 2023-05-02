package com.example.todo_app.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.text.DecimalFormat
import com.example.todo_app.R

class FirstResultActivity : AppCompatActivity() {
    lateinit var tex_res: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_result)

        var bn =DecimalFormat("#.##")
        val moy = bn.format(intent.getDoubleExtra("result", 0.0))

        tex_res = findViewById(R.id.texres)
        tex_res.text = moy

    }
}