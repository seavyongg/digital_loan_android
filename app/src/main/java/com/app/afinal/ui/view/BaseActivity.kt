package com.app.afinal.ui.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.app.afinal.databinding.ActivityProductListBinding
import com.app.afinal.model.Product

open class BaseActivity: AppCompatActivity() {
    fun showDialog(title: String, message: String, buttonTitile: String = "OK", onDissmiss: (() -> Unit)? = null) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonTitile){
                    dialog, which -> dialog.dismiss()
                onDissmiss?.invoke()
            }
        dialog.show()
    }
    fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


}