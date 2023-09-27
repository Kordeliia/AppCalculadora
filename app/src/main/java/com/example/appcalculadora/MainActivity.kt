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
                val lenght = binding.tvOperation.text.length
                val newOperation : String
                if (lenght != 0){
                    newOperation = binding.tvOperation.text.toString().substring(0, lenght-1)
                } else {
                    newOperation = binding.tvOperation.text.toString()
                }
                binding.tvOperation.text= newOperation
            }
            R.id.btnClear -> {
                binding.tvOperation.text = ""
                binding.tvReultado.text = ""
            }
            R.id.btnResultado -> {

            }
            else -> {
                binding.tvOperation.append(valueStre)
            }
        }
    }
}