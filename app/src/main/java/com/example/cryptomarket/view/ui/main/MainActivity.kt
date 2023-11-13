package com.example.cryptomarket.view.ui.main

import android.os.Bundle
import android.view.MenuItem
import com.example.cryptomarket.R
import com.example.cryptomarket.bases.BaseActivity
import com.example.cryptomarket.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : BaseActivity(), NavigationBarView.OnItemSelectedListener,
    NavigationBarView.OnItemReselectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNav = binding.bnvMain;
        bottomNav.setOnItemSelectedListener(this)
        replaceFragment(MarketFragment.newInstance(), isAddToBackStack = false, anim = false)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.market -> replaceFragment(
                    MarketFragment.newInstance(),
                    isAddToBackStack = false,
                    anim = false
                )
            R.id.search -> replaceFragment(
                SearchFragment.newInstance(),
                isAddToBackStack = false,
                anim = false
            )
            R.id.watch_list -> replaceFragment(
                WatchListFragment.newInstance(),
                isAddToBackStack = false,
                anim = false
            )
        }
        return true
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        // empty action for this function
    }
}