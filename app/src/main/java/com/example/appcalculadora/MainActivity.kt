package com.example.appcalculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.appcalculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun onClickButton(view : View){
        val valueStre = (view as Button).text.toString()
        when(view.id){
            R.id.btnAtras -> {

            }
            R.id.btnClear -> {

            }
            R.id.btnResultado -> {

            }
            else -> {
                binding.tvOperation.append(valueStre)
            }
        }
    }
}