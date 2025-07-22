package com.app.afinal.Application

import android.app.Application

class DigitalLoanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
    companion object{
        var isConnected = false
    }
}