package com.example.appcalculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import com.example.appcalculadora.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvOperation.run{
           addTextChangedListener {charSequence ->
                if(canReplaceOperator(charSequence.toString())){
                    val length = text.length
                    val newOperation = text.toString().substring(0, length-2) +
                            text.toString().substring( length-1)
                    text = newOperation
                }
        }
    }
    }
    private fun canReplaceOperator(charSquence: CharSequence): Boolean {
        if (charSquence.length < 2) return false
        val lastElement = charSquence[charSquence.length -1].toString()
        val penultElement = charSquence[charSquence.length -2].toString()
        return (lastElement == Constantes.OPERATOR_MULTI ||
                lastElement == Constantes.OPERATOR_DIV ||
                lastElement == Constantes.OPERATOR_SUMA) &&
                (penultElement == Constantes.OPERATOR_MULTI ||
                        penultElement == Constantes.OPERATOR_DIV ||
                        penultElement == Constantes.OPERATOR_SUMA ||
                        penultElement == Constantes.OPERATOR_RESTA)
    }

    fun onClickButton(view: View) {
        val valueStr = (view as Button).text.toString()
        val operation = binding.tvOperation.text.toString()
        when (view.id) {
            R.id.btnAtras -> {
                binding.tvOperation.run{
                    if(text.length > 0) text = operation.substring(0, text.length-1)
                }
            }
            R.id.btnClear -> {
                binding.tvOperation.text = ""
                binding.tvResultado.text = ""
            }
            R.id.btnResultado -> tryResolve(operation, true)
            R.id.btnSuma, R.id.btnResta, R.id.btnMultiplicacion, R.id.btnDivision -> {
                tryResolve(operation, false)
                val operator = valueStr
                addOperator(operator, operation)
            }
            R.id.btnDecimal -> addPoint(valueStr, operation)
            else -> binding.tvOperation.append(valueStr)
        }
    }
    private fun addPoint(pointStr: String, operation: String) {
        if (!operation.contains(Constantes.OPERATOR_DECIMAL)) {
            binding.tvOperation.append(pointStr)
        } else {
            val operator = getOperator(operation)
            var values = arrayOfNulls<String>(0)
            if (operator != Constantes.OPERATOR_NULL) {
                if (operator == Constantes.OPERATOR_RESTA) {
                    val index = operation.lastIndexOf(Constantes.OPERATOR_RESTA)
                    if (index < operation.length - 1) {
                        values = arrayOfNulls(2)
                        values[0] = operation.substring(0, index)
                        values[1] = operation.substring(index + 1)
                    } else {
                        values = arrayOfNulls(1)
                        values[0] = operation.substring(0, index)
                    }
                } else {
                    values = operation.split(operator).toTypedArray()

                }
            }
            if(values.size > 0){
                val numberOne = values[0]!!
                if (values.size > 1) {
                    val numberTwo = values[1]!!
                    if(numberOne.contains(Constantes.OPERATOR_DECIMAL) && !numberTwo.contains(Constantes.OPERATOR_DECIMAL)){
                        binding.tvOperation.append(pointStr)
                    }
                } else{
                    if(numberOne.contains(Constantes.OPERATOR_DECIMAL)){
                        binding.tvOperation.append(pointStr)
                    }
                }
            }
        }
    }

    private fun addOperator(operator: String, operation: String) {
        val lastElement = if (operation.isEmpty()) ""
        else operation.substring(operation.length-1)
        if(operator == Constantes.OPERATOR_RESTA){
            if(operation.isEmpty() || lastElement != Constantes.OPERATOR_RESTA && lastElement != Constantes.OPERATOR_DECIMAL){
                binding.tvOperation.append(operator)
            }
        } else{
            if(!operation.isEmpty() && lastElement != Constantes.OPERATOR_DECIMAL){
                binding.tvOperation.append(operator)
            }
        }
    }

    private fun tryResolve(operationRef: String, isFromResolve: Boolean) {
        if(operationRef.isEmpty()) return
        var operation = operationRef
        if(operation.contains(Constantes.OPERATOR_DECIMAL) && operation.lastIndexOf(Constantes.OPERATOR_DECIMAL) == operation.length-1){
            operation = operation.substring(0, operation.length-1)
        }
        val operator = getOperator(operation)
        var values = arrayOfNulls<String>(0)
        if (operator != Constantes.OPERATOR_NULL){
            if(operator == Constantes.OPERATOR_RESTA)
            {
                val index = operation.lastIndexOf(Constantes.OPERATOR_RESTA)
                if(index < operation.length-1){
                    values = arrayOfNulls(2)
                    values[0] = operation.substring(0, index)
                    values[1] = operation.substring(index+1)
                }
                else{
                    values = arrayOfNulls(1)
                    values[0] = operationRef.substring(0, index)
                }
            } else{
                values = operation.split(operator).toTypedArray()

            }
            if(values.size > 1){
                try {
                    val numberOne = values[0]!!.toDouble()
                    val numberTwo = values[1]!!.toDouble()
                    binding.tvResultado.text = getResult(numberOne, numberTwo, operator).toString()
                    if(binding.tvResultado.text.isNotEmpty() && !isFromResolve)
                    {
                        binding.tvOperation.text = binding.tvResultado.text
                    }
                } catch (e: NumberFormatException) {
                    if (isFromResolve) showMessg()
                }
            } else{
                if(isFromResolve && operator != Constantes.OPERATOR_NULL){
                    showMessg()
                }
            }
        }
    }

    private fun showMessg() {
        Snackbar.make(binding.root, getString(R.string.exception_n1), Snackbar.LENGTH_SHORT)
            .setAnchorView(binding.llBotones1)
            .show()
    }

    private fun getOperator(operation: String): String {
        var operator = ""
        if (operation.contains(Constantes.OPERATOR_SUMA)) {
            operator = Constantes.OPERATOR_SUMA
        } else if (operation.contains(Constantes.OPERATOR_MULTI)) {
            operator = Constantes.OPERATOR_MULTI
        } else if (operation.contains(Constantes.OPERATOR_DIV)) {
            operator = Constantes.OPERATOR_DIV
        } else {
            operator = Constantes.OPERATOR_NULL
        }
        if (operator == Constantes.OPERATOR_NULL && operation.lastIndexOf(Constantes.OPERATOR_RESTA) > 0) {
        operator = Constantes.OPERATOR_RESTA
        }
        return operator
    }
    private fun getResult(numberOne: Double, numberTwo : Double, operator : String) : Double{
        var result = 0.0
        when(operator){
            Constantes.OPERATOR_SUMA -> result = numberOne + numberTwo
            Constantes.OPERATOR_RESTA -> result = numberOne - numberTwo
            Constantes.OPERATOR_MULTI -> result = numberOne * numberTwo
            Constantes.OPERATOR_DIV -> result = numberOne / numberTwo
        }
        return result
    }
}