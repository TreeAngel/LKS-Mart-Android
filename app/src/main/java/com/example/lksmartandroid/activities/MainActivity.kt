package com.example.lksmartandroid.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.lksmartandroid.R
import com.example.lksmartandroid.databinding.ActivityMainBinding
import com.example.lksmartandroid.fragments.ProductFragment
import com.example.lksmartandroid.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val productFragment: ProductFragment = ProductFragment()
    private val profileFragment: ProfileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        with(binding) {
            bottomNav.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.menu_profile -> {
                        changeFragment(profileFragment, "profile")
                    }
                    R.id.menu_products -> {
                        changeFragment(productFragment, "product")
                    }
                }
                true
            }
        }
        changeFragment(productFragment, "product")
    }

    private fun changeFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        val currentFragment = supportFragmentManager.primaryNavigationFragment
        val existFragment = supportFragmentManager.findFragmentByTag(tag)

        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (existFragment == null) {
            transaction.add(binding.navView.id, fragment, tag)
        } else {
            transaction.show(existFragment)
        }

        transaction.setPrimaryNavigationFragment(existFragment ?: fragment)
        transaction.setReorderingAllowed(true)
        transaction.commit()
    }
}