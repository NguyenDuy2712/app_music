package com.example.musicapp.ui.main.leaderboard.detailLeader

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.databinding.DetailLeaderFragmentBinding
import com.example.musicapp.databinding.DetailTopicFragmentBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.ui.main.topic.detailTopic.DetailTopicAdapter
import com.example.musicapp.ui.main.topic.detailTopic.DetailTopicFragmentArgs
import com.example.musicapp.ui.main.topic.detailTopic.DetailTopicViewModel
import com.example.musicapp.utils.base.BaseFragment
import com.example.musicapp.utils.loadImagePlayer
import com.example.musicapp.utils.loadImageWithSize

class DetailLeaderFragment : BaseFragment(),DetailLeaderArtistInterface {
    private lateinit var viewModel: DetailLeaderViewModel
    private lateinit var binding : DetailLeaderFragmentBinding
    private lateinit var adapter : DetailLeaderAdapter
    private lateinit var adapterArtist : DetailLeaderArtistAdapter

    val args : DetailTopicFragmentArgs by navArgs()
    val api : String = PlayerManager.api

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailLeaderFragmentBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(DetailLeaderViewModel::class.java)
        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideBottomSheet()
        return binding.root
    }
    override fun setUpView() {
        super.setUpView()
        PlayerManager.api = api
        binding.lbTitle.setText(args.lbTitle.toUpperCase())
        adapter = DetailLeaderAdapter()
        adapterArtist = DetailLeaderArtistAdapter(this)
        binding.listView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        binding.listView.adapter = if(PlayerManager.api == "artist") adapterArtist else adapter

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnPlay.setOnClickListener {
            viewModel.getMusic()?.let { music ->
                AppPreference.indexPlaying = 0
                PlayerManager.music?.value = music.value
                PlayerManager.playerService?.playIndex()
                AppPreference.playStatus = 0
                AppPreference.saveAPI = PlayerManager.api
            }
        }
    }
    override fun bind() {
        super.bind()
        binding.appBarLayout.visibility = View.INVISIBLE
        binding.btnPlay.visibility = View.INVISIBLE
        getData()
    }
    fun getData() {
        if (PlayerManager.api == "artist") {
            viewModel.getArtist()?.observe(viewLifecycleOwner) {
                it?.let {
                    binding.loadingView.visibility = View.GONE
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.btnPlay.visibility = View.INVISIBLE
                    loadImageWithSize(binding.imv, it[0].image, 350, 350)
                    adapterArtist.setData(it)
                }
            }
        } else {
            viewModel.getMusic()?.observe(viewLifecycleOwner) {
                it?.let {
                    binding.loadingView.visibility = View.GONE
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.btnPlay.visibility = View.VISIBLE
                    loadImageWithSize(binding.imv, it[0].image, 350, 350)
                    adapter.setData(it)
                }
            }
        }
    }

    override fun onClickItem(path:String) {
        val action = DetailLeaderFragmentDirections.actionDetailLeaderFragmentToSongFragment(path)
        findNavController().navigate(action)
    }

}