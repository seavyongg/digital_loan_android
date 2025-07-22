package com.app.afinal.ui.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.viewbinding.ViewBinding



abstract class BaseActivity<T : ViewBinding>: AppCompatActivity() {
    protected lateinit var binding: T
    abstract fun provideBinding(): T
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = provideBinding()
        setContentView(binding.root)
    }

    fun toolbar() {
        supportActionBar?.hide()
    }

    fun showDialog(
        title: String,
        message: String,
        buttonTitile: String = "OK",
        buttonCancel: String ?= "Cancel",
        onDissmiss: (() -> Unit)? = null
    ) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonTitile) { dialog, which ->
                dialog.dismiss()
                onDissmiss?.invoke()
            }
            .setNegativeButton(buttonCancel) { dialog, which ->
                dialog.dismiss()
            }
        dialog.show()
    }

    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    // Function to replace a fragment in the activity
    fun replaceFragment(fragment: Fragment, fragmentId: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        Log.d("ReplaceFragment", "Replacing fragment to RequestLoanFragment")
        transaction.replace(fragmentId, fragment)
            .addToBackStack(null) // Add to back stack to allow navigation back
            .commit()
    }

}