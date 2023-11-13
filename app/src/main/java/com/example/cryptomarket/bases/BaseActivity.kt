package com.example.cryptomarket.bases

import com.example.cryptomarket.R
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.cryptomarket.databinding.ToolbarMainBinding
import com.example.cryptomarket.view.customs.LoadingDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    private var mDialogView: LoadingDialog? = null
    fun showLoading() {
        if (mDialogView != null) {
            mDialogView!!.show()
        } else {
            mDialogView = LoadingDialog(this)
            mDialogView!!.setCanceledOnTouchOutside(false)
            mDialogView!!.show()
        }
    }

    fun dismissLoading() {
        mDialogView?.dismiss()
    }

    private fun addReplaceFragment(
        fragment: BaseFragment?,
        isReplace: Boolean,
        isAddToBackStack: Boolean,
        anim: Boolean
    ) {
        val fragmentManager: FragmentManager = supportFragmentManager
        if (fragment == null) return;
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        if (anim) {
            fragmentTransaction.setCustomAnimations(
                R.anim.anim_enter_from_up,
                R.anim.anim_enter_from_top,
                R.anim.anim_exit_to_top,
                R.anim.anim_exit_to_up
            )
        } else {
            fragmentTransaction.setCustomAnimations(
                R.anim.anim_enter_from_right,
                R.anim.anim_exit_to_left,
                R.anim.anim_enter_from_left,
                R.anim.anim_exit_to_right
            )
        }
        if (isReplace) fragmentTransaction.replace(
            R.id.frm_container,
            fragment,
            fragment.javaClass.simpleName
        ) else {
            val currentFragment: Fragment? =
                supportFragmentManager.findFragmentById(R.id.frm_container)
            if (currentFragment != null) {
                fragmentTransaction.hide(currentFragment)
            }
            fragmentTransaction.add(
                R.id.frm_container,
                fragment,
                fragment.javaClass.simpleName
            )
        }
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        }
        fragmentTransaction.commit()

    }

    fun replaceFragment(fragment: BaseFragment?, isAddToBackStack: Boolean, anim: Boolean) {
        addReplaceFragment(fragment, true, isAddToBackStack, anim)
    }

    fun addFragment(fragment: BaseFragment?, isAddToBackStack: Boolean, anim: Boolean) {
        addReplaceFragment(fragment, false, isAddToBackStack, anim)
    }

    fun clearAllBackStack() {
        val fm: FragmentManager = supportFragmentManager
        val count: Int = fm.backStackEntryCount
        for (i in 0 until count) {
            fm.popBackStack()
        }
    }

    fun showFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.anim_enter_from_up,
            R.anim.anim_enter_from_top,
            R.anim.anim_exit_to_top,
            R.anim.anim_exit_to_up
        )
        fragmentTransaction.show(fragment)
        fragmentTransaction.commit()
    }

    fun hideFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.anim_enter_from_up,
            R.anim.anim_enter_from_top,
            R.anim.anim_exit_to_top,
            R.anim.anim_exit_to_up
        )
        fragmentTransaction.hide(fragment)
        fragmentTransaction.commit()
    }

    fun popBackStack() {
        supportFragmentManager.popBackStack()
    }

    fun setVisibleBottomTab(bottomNavigationView: BottomNavigationView, visible: Boolean) {
        bottomNavigationView.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setToolbarMain(
        binding: ToolbarMainBinding,
        title: String?,
        isShowBackButton: Boolean = false,
        isShowFavoriteButton: Boolean = false,
        isShowSettingButton: Boolean = false,
    ) {
        if (title != null) {
            binding.toolbarMainTvTitle.text = title
        }
        if (isShowBackButton) {
            binding.toolbarMainIvBack.visibility = View.VISIBLE
            binding.toolbarMainIvBack.setOnClickListener { _ -> onBackPressedDispatcher.onBackPressed() }
        }
        if (isShowFavoriteButton) {
            binding.toolbarMainIvFavorite.visibility = View.VISIBLE
        }
        if (isShowSettingButton) {
            binding.toolbarMainIvSetting.visibility = View.VISIBLE
        }
    }


    fun visibilityOfBottom(value: Boolean, bottomNavigationView: BottomNavigationView) {
        val visibility = if (value) View.GONE else View.VISIBLE
        bottomNavigationView.visibility = visibility
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}