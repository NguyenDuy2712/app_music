package com.example.musicapp.ui.main.leaderboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentLeaderBoardBinding
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.ui.main.home.song.SongViewModel
import com.example.musicapp.utils.base.BaseFragment

class LeaderBoardFragment : BaseFragment() {

    private lateinit var viewModel: LeaderBoardViewModel
    private lateinit var binding : FragmentLeaderBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeaderBoardBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(LeaderBoardViewModel::class.java)
        (activity as MainActivity).showBottomNavigation()
        (activity as MainActivity).showBottomSheet()
        return binding.root
    }

    override fun setUpView() {
        super.setUpView()
        binding.viewVpop.setOnClickListener {
            PlayerManager.api = "nhactre"
            val action = LeaderBoardFragmentDirections.actionLeaderBoardFragmentToDetailLeaderFragment("nhactre")
            findNavController().navigate(action)
        }
        binding.viewUsUK.setOnClickListener {
            PlayerManager.api = "usuk"
            val action = LeaderBoardFragmentDirections.actionLeaderBoardFragmentToDetailLeaderFragment("usuk")
            findNavController().navigate(action)
        }
        binding.viewKpop.setOnClickListener {
            PlayerManager.api = "kpop"
            val action = LeaderBoardFragmentDirections.actionLeaderBoardFragmentToDetailLeaderFragment("kpop")
            findNavController().navigate(action)
        }
        binding.viewArtist.root.setOnClickListener {
            PlayerManager.api = "artist"
            val action = LeaderBoardFragmentDirections.actionLeaderBoardFragmentToDetailLeaderFragment("artist")
            findNavController().navigate(action)
        }
    }
    override fun bind() {
        super.bind()
    }

}