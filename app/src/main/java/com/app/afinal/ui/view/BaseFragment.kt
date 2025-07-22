package com.app.afinal.ui.view

import android.renderscript.ScriptGroup.Binding
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding>:  Fragment() {
    val baseActivity: BaseActivity<*>
        get() = activity as BaseActivity<*>
    var binding : T? = null
    abstract fun provideBinding(): T
    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): android.view.View? {
        binding = provideBinding()
        return binding?.root
    }

    protected fun showDialog(title: String, message: String, buttonTitle: String = "OK",buttonNav: String? = "Cancel", onDismiss: (() -> Unit)? = null) {
        baseActivity.showDialog(title, message, buttonTitle, buttonNav, onDismiss)
    }
    protected fun isInternetAvailable(): Boolean {
        return baseActivity.isInternetAvailable() ?: false
    }
    fun replaceFragment(fragment: Fragment, fragmentId: Int) {
       baseActivity.replaceFragment(fragment, fragmentId)
    }

}