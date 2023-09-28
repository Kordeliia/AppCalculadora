package com.example.appcalculadora
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import com.example.appcalculadora.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.NumberFormatException
import java.util.Locale
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvOperation.run{
           addTextChangedListener {charSequence ->
                if(Operations.canReplaceOperator(charSequence.toString())){
                    val length = text.length
                    val newOperation = text.toString().substring(0, length-2) +
                            text.toString().substring( length-1)
                    text = newOperation
                }
            }
        }
    }
    fun onClickButton(view: View) {
        val valueStr = (view as Button).text.toString()
        val operation = binding.tvOperation.text.toString()
        when (view.id) {
            R.id.btnAtras -> {
                binding.tvOperation.run{
                    if(text.isNotEmpty()) text = operation.substring(0, text.length-1)
                }
            }
            R.id.btnClear -> {
                binding.tvOperation.text = ""
                binding.tvResultado.text = ""
            }
            R.id.btnResultado -> checkOrResolve(operation, true)
            R.id.btnSuma, R.id.btnResta, R.id.btnMultiplicacion, R.id.btnDivision -> {
                checkOrResolve(operation, false)
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
            val operator = Operations.getOperator(operation)
            val values = Operations.divideOperation(operator, operation)
            if(values.isNotEmpty()){
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
            if(operation.isNotEmpty() && lastElement != Constantes.OPERATOR_DECIMAL){
                binding.tvOperation.append(operator)
            }
        }
    }
    private fun showMessg(errorRes: Int) {
        Snackbar.make(binding.root,errorRes, Snackbar.LENGTH_SHORT)
            .setAnchorView(binding.llBotones1)
            .show()
    }
    private fun checkOrResolve(operation: String, isFromResolve: Boolean){
        Operations.tryResolve(operation, isFromResolve, object : OnResolveListener{
            override fun onShowResult(result: Double) {
                binding.tvResultado.text = String.format(Locale.ROOT, "%.2f", result)
                if(binding.tvResultado.text.isNotEmpty() && !isFromResolve)
                {
                    binding.tvOperation.text = binding.tvResultado.text
                }
            }
            override fun onShowMssg(errorRes: Int) {
                showMessg(errorRes)
            }
        })
    }
}