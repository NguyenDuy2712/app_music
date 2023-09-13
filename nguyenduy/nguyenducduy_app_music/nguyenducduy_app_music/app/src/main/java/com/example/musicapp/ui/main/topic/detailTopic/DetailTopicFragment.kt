package com.example.musicapp.ui.main.topic.detailTopic

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
import com.example.musicapp.databinding.DetailTopicFragmentBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Music
import com.example.musicapp.ui.main.home.song.SongAdapter
import com.example.musicapp.ui.main.home.song.SongFragmentArgs
import com.example.musicapp.utils.base.BaseFragment
import com.example.musicapp.utils.loadImageWithSize

class DetailTopicFragment() : BaseFragment() {
    private lateinit var viewModel: DetailTopicViewModel
    private lateinit var binding : DetailTopicFragmentBinding
    private lateinit var adapter : DetailTopicAdapter
    val args : DetailTopicFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailTopicFragmentBinding.inflate(inflater,container,false)
         viewModel = ViewModelProvider(this).get(DetailTopicViewModel::class.java)
        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideBottomSheet()
        return binding.root
    }

    override fun setUpView() {
        super.setUpView()
        Log.e("api : ",PlayerManager.api)
        binding.lbTitle.text = "Bài Hát"
        adapter = DetailTopicAdapter()
        binding.listView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.listView.adapter = adapter
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnPlay.setOnClickListener {
            viewModel.getData()?.let { music ->
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
        viewModel.path = args.lbTitle
        binding.appBarLayout.visibility = View.INVISIBLE
        binding.btnPlay.visibility = View.INVISIBLE
        viewModel.getData()?.observe(viewLifecycleOwner){
            it?.let { musics ->
                binding.loadingView.visibility = View.GONE
                binding.appBarLayout.visibility = View.VISIBLE
                binding.btnPlay.visibility = View.VISIBLE
                loadImageWithSize(binding.imv,musics[0].image,350,350)
                adapter.setData(musics)
            }
        }
    }


}