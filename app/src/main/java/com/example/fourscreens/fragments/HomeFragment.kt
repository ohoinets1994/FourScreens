package com.example.fourscreens.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fourscreens.R
import com.example.fourscreens.activities.CameraActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btnStart.setOnClickListener {
            startActivity(Intent(activity, CameraActivity::class.java))
        }

        btnList.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sessionsListFragment)
        }
    }
}