package com.example.appcalculadora

interface OnResolveListener {
    fun onShowResult(result: Double)
    fun onShowMssg(errorRest: Int)
}