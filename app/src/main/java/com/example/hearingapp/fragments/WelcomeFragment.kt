package com.example.hearingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.hearingapp.R


class WelcomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        val btnNext: Button = view.findViewById(R.id.btnNext)
        val viewPager: ViewPager2? = activity?.findViewById(R.id.viewPager)
        btnNext.setOnClickListener {
            viewPager?.currentItem=1
        }
        return view
    }


}