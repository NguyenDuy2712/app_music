package com.example.musicapp.ui.main.home.homeIcon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.HomeIconFragmentBinding
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.ui.main.home.HomeFragmentDirections
import com.example.musicapp.utils.base.BaseFragment


class HomeIconFragment() : BaseFragment() {
    private lateinit var binding : HomeIconFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = HomeIconFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun setUpView() {
        super.setUpView()
        binding.btnTop100.setOnClickListener {
            PlayerManager.api = "top100"
            val action = HomeFragmentDirections.actionHomeFragmentToSongFragment("top100")
            findNavController().navigate(action)
        }
        binding.btnSong.setOnClickListener {
            PlayerManager.api = "topSong"
            val action = HomeFragmentDirections.actionHomeFragmentToSongFragment("topSong")
            findNavController().navigate(action)
        }
        binding.btnUpload.setOnClickListener {
            PlayerManager.api = "upload"
            val action = HomeFragmentDirections.actionHomeFragmentToSongFragment("upload")
            findNavController().navigate(action)
        }
        binding.btnArtist.setOnClickListener {
            PlayerManager.api = "artist"
            val action = HomeFragmentDirections.actionHomeFragmentToDetailLeaderFragment("artist")
            findNavController().navigate(action)
        }
    }
    override fun bind() {
        super.bind()
    }




}