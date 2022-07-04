package com.example.hearingapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.example.hearingapp.MainActivityRight
import com.example.hearingapp.R


class InstructFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_instruct, container, false)
        val btnFinish: Button = view.findViewById(R.id.btnFinish)
        btnFinish.setOnClickListener {
            startActivity(Intent(context,MainActivityRight::class.java))
            activity?.finish()
        }
        return view
    }
}