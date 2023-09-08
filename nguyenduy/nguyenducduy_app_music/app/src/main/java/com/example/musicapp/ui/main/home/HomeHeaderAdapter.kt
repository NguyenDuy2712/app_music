package com.example.musicapp.ui.main.home

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeHeaderAdapter(activity:AppCompatActivity ):FragmentStateAdapter(activity) {
    val fragment = arrayListOf(
        HomeHeaderFragment(),
    )
    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        return fragment[position]
    }
}