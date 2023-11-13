package com.example.cryptomarket.bases

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.example.cryptomarket.databinding.ToolbarMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
open class BaseFragment : Fragment() {
    private var resultListener: FragmentResultListener? = null
    private var mCodeRequest = ""
    fun setFragmentForResult(
        codeRequest: String,
        fragmentResultListener: FragmentResultListener?
    ) {
        resultListener = fragmentResultListener
        mCodeRequest = codeRequest
    }

    protected fun callBackFragmentResult(bundle: Bundle) {
        resultListener?.onFragmentResult(mCodeRequest, bundle)
    }

    fun clearAllBackStack() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).clearAllBackStack()
        }
    }

    fun setVisibleBottomTab(bottomNavigationView: BottomNavigationView?, visible: Boolean) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).setVisibleBottomTab(bottomNavigationView!!, visible)
        }
    }

    fun popBackStack() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).popBackStack()
        }
    }

    fun replaceFragment(fragment: BaseFragment?, isAddToBackStack: Boolean, anim: Boolean) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).replaceFragment(fragment, isAddToBackStack, anim)
        }
    }

    fun addFragment(fragment: BaseFragment?, isAddToBackStack: Boolean, anim: Boolean) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).addFragment(fragment, isAddToBackStack, anim)
        }
    }

    fun showLoading() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showLoading()
        }
    }

    fun dismissLoading() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).dismissLoading()
        }
    }

    fun showToast(msg: String) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showToast(msg)
        }
    }

    fun setToolbarMain(
        binding: ToolbarMainBinding,
        title: String?,
        isShowBackButton: Boolean = false,
        isShowFavoriteButton: Boolean = false,
        isShowSettingButton: Boolean = false,
    ) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).setToolbarMain(
                binding,
                title,
                isShowBackButton,
                isShowFavoriteButton,
                isShowSettingButton,
            )
        }
    }

    fun visibleBottom(value: Boolean, bottomNavigationView: BottomNavigationView?) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).visibilityOfBottom(value, bottomNavigationView!!)
        }
    }

}