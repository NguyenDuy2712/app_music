package com.example.musicapp.ui.main.library

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicapp.ui.main.library.favoriteMusic.FavoriteMusicFragment
import com.example.musicapp.ui.main.library.localMusic.LocalMusicFragment

class LibraryAdapter(val parent : Fragment):FragmentStateAdapter(parent) {
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
      return  when(position){
            0->LocalMusicFragment()
            else -> FavoriteMusicFragment()
        }
    }
}