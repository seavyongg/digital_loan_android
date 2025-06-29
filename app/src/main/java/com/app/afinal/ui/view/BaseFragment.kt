package com.app.afinal.ui.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

open class BaseFragment: Fragment() {
    protected fun showDialog(title: String, message: String, buttonTitle: String = "OK", onDismiss: (() -> Unit)? = null) {
        val baseActivity = activity as? BaseActivity
        baseActivity?.showDialog(title, message, buttonTitle, onDismiss)
    }
    protected fun isInternetAvailable(): Boolean {
        val baseActivity = activity as? BaseActivity
        return baseActivity?.isInternetAvailable() ?: false
    }
}