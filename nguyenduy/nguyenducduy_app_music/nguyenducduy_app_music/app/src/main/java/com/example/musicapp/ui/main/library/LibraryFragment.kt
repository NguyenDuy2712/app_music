package com.example.musicapp.ui.main.library

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.MusicApplication
import com.example.musicapp.databinding.LibraryFragmentBinding
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.utils.base.BaseFragment


class LibraryFragment : BaseFragment() {

    private lateinit var viewModel: LibraryViewModel
    private lateinit var binding: LibraryFragmentBinding
    private lateinit var adapter: LibraryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LibraryFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(LibraryViewModel::class.java)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setUpView() {
        super.setUpView()
      //  getFileExternal()
        adapter = LibraryAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        setUpPager()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun bind() {
        super.bind()
    }

    fun setUpPager() {
        binding.segment.setOnPositionChangedListener {
            binding.viewPager.setCurrentItem(it, true)
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.segment.setPosition(position, true)

            }
        })
    }
}