package com.example.appcalculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.appcalculadora.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClickButton(view: View) {
        val valueStre = (view as Button).text.toString()
        when (view.id) {
            R.id.btnAtras -> {
                val lenght = binding.tvOperation.text.length
                val newOperation: String
                if (lenght != 0) {
                    newOperation = binding.tvOperation.text.toString().substring(0, lenght - 1)
                } else {
                    newOperation = binding.tvOperation.text.toString()
                }
                binding.tvOperation.text = newOperation
            }

            R.id.btnClear -> {
                binding.tvOperation.text = ""
                binding.tvReultado.text = ""
            }

            R.id.btnResultado -> {
                tryResolve(binding.tvOperation.text.toString())
            }

            else -> {
                binding.tvOperation.append(valueStre)
            }
        }
    }

    private fun tryResolve(operationRef: String) {
        val operator = getOperator(operationRef)
        var values = arrayOfNulls<String>(0)
        if (operator != OPERATOR_NULL){
            if(operator == OPERATOR_RESTA)
            {
                val index = operationRef.lastIndexOf(OPERATOR_RESTA)
                if(index < operationRef.length-1){
                    values = arrayOfNulls(2)
                    values[0] = operationRef.substring(0, index)
                    values[1] = operationRef.substring(index+1)
                }
                else{
                    values = arrayOfNulls(1)
                    values[0] = operationRef.substring(0, index)
                }
            } else{
                values = operationRef.split(operator).toTypedArray()

            }
            if(values.size > 1){
                try {
                    val numberOne = values[0]!!.toDouble()
                    val numberTwo = values[1]!!.toDouble()
                    binding.tvReultado.text = getResult(numberOne, numberTwo, operator).toString()
                } catch (e: NumberFormatException) {
                    Snackbar.make(binding.root, "La expresion NO es correcta", Snackbar.LENGTH_SHORT).show()
                }
            } else{
                Snackbar.make(binding.root, "La expresion NO es correcta", Snackbar.LENGTH_SHORT).show()
            }

        }

    }

    private fun getOperator(operation: String): String {
        var operator = ""
        if (operation.contains(OPERATOR_SUMA)) {
            operator = OPERATOR_SUMA
        } else if (operation.contains(OPERATOR_RESTA)) {
            operator = OPERATOR_RESTA
        } else if (operation.contains(OPERATOR_MULTI)) {
            operator = OPERATOR_MULTI
        } else if (operation.contains(OPERATOR_DIV)) {
            operator = OPERATOR_DIV
        } else {
            operator = OPERATOR_NULL
        }
        if (operator == OPERATOR_NULL && operation.lastIndexOf(OPERATOR_RESTA) > 0) {
        operator = OPERATOR_RESTA
        }
        return operator
    }
    private fun getResult(numberOne: Double, numberTwo : Double, operator : String) : Double{
        var result = 0.0
        when(operator){
            OPERATOR_SUMA -> result = numberOne + numberTwo
            OPERATOR_RESTA -> result = numberOne - numberTwo
            OPERATOR_MULTI -> result = numberOne * numberTwo
            OPERATOR_DIV -> result = numberOne / numberTwo
        }
        return result
    }
    companion object{
        const val OPERATOR_MULTI = "*"
        const val OPERATOR_SUMA = "+"
        const val OPERATOR_RESTA = "-"
        const val OPERATOR_DIV = "/"
        const val OPERATOR_DECIMAL = "."
        const val OPERATOR_NULL = "null"
    }
}