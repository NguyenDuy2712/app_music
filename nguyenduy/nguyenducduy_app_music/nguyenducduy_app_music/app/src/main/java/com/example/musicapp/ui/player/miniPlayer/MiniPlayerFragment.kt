package com.example.musicapp.ui.player.miniPlayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.musicapp.MainActivity
import com.example.musicapp.MusicApplication
import com.example.musicapp.R
import com.example.musicapp.databinding.MiniPlayerFragmentBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.services.PlayerService
import com.example.musicapp.utils.getSongThumbnail
import com.example.musicapp.utils.loadImage
import com.example.musicapp.utils.loadImagePicasso
import com.example.musicapp.utils.toUpperString


class MiniPlayerFragment() : Fragment() {

    private lateinit var viewModel: MiniPlayerViewModel
    private lateinit var binding : MiniPlayerFragmentBinding
    private val playerService: PlayerService?
        get() = PlayerManager.playerService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MiniPlayerFragmentBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        onBind()
    }
    fun setUpView(){

        binding.lbTitle.isSelected = true
        binding.root.setOnClickListener {
            (activity as MainActivity).expandPanel()
        }
        binding.iconNext.setOnClickListener {
            PlayerManager.playerService?.nextSong()
        }
        binding.iconPrevious.setOnClickListener {
            PlayerManager.playerService?.previousSong()
        }
        binding.iconPlay.setOnClickListener {
            playerService?.playOrPause()
        }
    }
    fun onBind(){
        PlayerManager.changeSong.observe(viewLifecycleOwner){
            binding.iconPlay.setImageResource(R.drawable.ic_play)
            if(it){
                PlayerManager.music?.value?.let { music ->
                    val path = music[AppPreference.indexPlaying].path
                    if (path.isNullOrEmpty()) {
                        loadImage(
                            binding.imv,
                            music[AppPreference.indexPlaying].image
                        )
                    } else {
                        val imgByte = getSongThumbnail(path)
                        Glide.with(MusicApplication.appContext).asBitmap().load(imgByte).error(R.drawable.imv_bg_music)
                            .into(binding.imv)
                    }
                    binding.lbTitle.text = music[AppPreference.indexPlaying].nameSong.toUpperString()
                }
            }
        }
        PlayerManager.isPlaying.observe(viewLifecycleOwner){
            binding.iconPlay.setImageResource(if(it){ R.drawable.ic_pause }else R.drawable.ic_play)
        }
    }


}