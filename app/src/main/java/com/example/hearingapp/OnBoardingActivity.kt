package com.example.hearingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.hearingapp.fragments.InstructFragment
import com.example.hearingapp.fragments.WelcomeFragment

class OnBoardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        val fragmentList= arrayListOf<Fragment>(
            WelcomeFragment(),
            InstructFragment()
        )
        val adapter = ViewPagerAdapter(fragmentList,supportFragmentManager,lifecycle)

        val viewPager : ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter=adapter
    }
}