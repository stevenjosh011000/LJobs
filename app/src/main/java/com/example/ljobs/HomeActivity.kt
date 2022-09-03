package com.example.ljobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.example.ljobs.databinding.ActivityHomeBinding
import com.example.ljobs.fragments.HomeFragment
import com.example.ljobs.fragments.MoreFragment
import com.example.ljobs.fragments.ProfileFragment

class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeBinding

    private val homeFragment = HomeFragment()
    private val moreFragment = MoreFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        replaceFragment(homeFragment)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_profile -> replaceFragment(profileFragment)
                R.id.ic_more -> replaceFragment(moreFragment)
            }
            true
        }




    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer,fragment)
            transaction.commit()
        }
    }
}