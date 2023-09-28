package com.example.appcalculadora

import java.lang.NumberFormatException

class Operations {
    companion object {
        fun getOperator(operation: String): String {
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

        fun canReplaceOperator(charSquence: CharSequence): Boolean {
            if (charSquence.length < 2) return false
            val lastElement = charSquence[charSquence.length - 1].toString()
            val penultElement = charSquence[charSquence.length - 2].toString()
            return (lastElement == Constantes.OPERATOR_MULTI ||
                    lastElement == Constantes.OPERATOR_DIV ||
                    lastElement == Constantes.OPERATOR_SUMA) &&
                    (penultElement == Constantes.OPERATOR_MULTI ||
                            penultElement == Constantes.OPERATOR_DIV ||
                            penultElement == Constantes.OPERATOR_SUMA ||
                            penultElement == Constantes.OPERATOR_RESTA)
        }

        fun tryResolve(operationRef: String, isFromResolve: Boolean, listener: OnResolveListener) {
            if (operationRef.isEmpty()) return
            var operation = operationRef
            if (operation.contains(Constantes.OPERATOR_DECIMAL) && operation.lastIndexOf(Constantes.OPERATOR_DECIMAL) == operation.length - 1) {
                operation = operation.substring(0, operation.length - 1)
            }
            val operator = getOperator(operation)
            val values = divideOperation(operator, operation)
            if (values.size > 1) {
                try {
                    val numberOne = values[0]!!.toDouble()
                    val numberTwo = values[1]!!.toDouble()
                    listener.onShowResult(getResult(numberOne, numberTwo, operator))
                } catch (e: NumberFormatException) {
                    if (isFromResolve) listener.onShowMssg(R.string.mssg_error)
                }
            } else {
                if (isFromResolve && operator != Constantes.OPERATOR_NULL) listener.onShowMssg(R.string.exception_n1)
            }
        }

        fun getResult(numberOne: Double, numberTwo: Double, operator: String): Double {
            var result = 0.0
            when (operator) {
                Constantes.OPERATOR_SUMA -> result = numberOne + numberTwo
                Constantes.OPERATOR_RESTA -> result = numberOne - numberTwo
                Constantes.OPERATOR_MULTI -> result = numberOne * numberTwo
                Constantes.OPERATOR_DIV -> result = numberOne / numberTwo
            }
            return result
        }

        fun divideOperation(operator: String, operation: String): Array<String?> {
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
                    values = operation.split(operator).dropLastWhile { it == "" }.toTypedArray()

                }
            }
            return values
        }
    }
}