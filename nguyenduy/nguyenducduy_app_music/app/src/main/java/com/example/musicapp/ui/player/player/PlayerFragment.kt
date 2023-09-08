package com.example.musicapp.ui.player.player

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.musicapp.MusicApplication
import com.example.musicapp.R
import com.example.musicapp.database.MusicDB
import com.example.musicapp.databinding.PlayerFragmentBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.services.PlayerService
import com.example.musicapp.ui.player.player.playList.PlayListFragment
import com.example.musicapp.utils.*
import com.example.musicapp.utils.base.BaseFragment
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.MultiFileDownloadListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class PlayerFragment : BaseFragment() {

    private val playerService: PlayerService?
        get() = PlayerManager.playerService

    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: PlayerFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlayerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setUpView() {
        super.setUpView()
        binding.txtSongTitle.isSelected = true
        binding.fabPlayPause.setOnClickListener {
            playerService?.playOrPause()
        }
        binding.btnNext.setOnClickListener {
            playerService?.nextSong()
        }
        binding.btnPrevious.setOnClickListener {
            playerService?.previousSong()
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    playerService?.seekTo(progress)
                    binding.txtStartDuration.text = millisToString(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
        })
        binding.btnPlayList.setOnClickListener {
            PlayListFragment().show(requireActivity().supportFragmentManager, "play_list")
        }
        onClickBtnFavorite()
        setUpRepeatSong()
        downloadMp3()
        btnShare()
    }

    override fun bind() {
        super.bind()
        PlayerManager.changeSong.observe(viewLifecycleOwner) {
            binding.fabPlayPause.setImageResource(R.drawable.ic_play)

            if (it) {
                PlayerManager.music?.value?.let { music ->
                    val path = music[AppPreference.indexPlaying].path
                    val thumb = music[AppPreference.indexPlaying].thumb
                    if (thumb != null) {
                        if(thumb.isNotEmpty()){
                            loadImageWithPlaceholder(
                                R.drawable.imv_bg_play_music,
                                binding.imgBg,
                                thumb
                            )
                        }
                    }
                    if (path.isNullOrEmpty()) {
                        loadImagePlayer(
                            binding.imgThumbnail,
                            music[AppPreference.indexPlaying].image
                        )
                    } else {
                        val imgByte = getSongThumbnail(path)
                        Glide.with(MusicApplication.appContext).asBitmap().load(imgByte).error(R.drawable.imv_bg_music)
                            .apply(RequestOptions().override(250, 250))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(binding.imgThumbnail)
                    }
                    binding.txtSongTitle.text = music[AppPreference.indexPlaying].nameSong.toUpperString()
                    binding.txtArtistName.text = music[AppPreference.indexPlaying].artist?.toUpperString()
                    Log.e("AVATAR",music[AppPreference.indexPlaying].image)
                }
                binding.btnFavorite.isSelected = false
                binding.btnFavorite.setColorFilter(requireContext().getColor(R.color.black), PorterDuff.Mode.SRC_IN)
            }
        }
        PlayerManager.isPlaying.observe(viewLifecycleOwner) {
            if (it) {
                binding.txtStartDuration.text =
                    millisToString(PlayerManager.playerService!!.getCurrentPosition())
                binding.txtEndDuration.text =
                    millisToString(PlayerManager.playerService!!.getDurationPosition())
                setUpSeekBar()
            } else {
            }
            binding.fabPlayPause.setImageResource(if (it) R.drawable.ic_pause else R.drawable.ic_play)
        }
    }
    private fun millisToString(duration: Int): String {
        val minutes = duration / 1000 / 60
        val seconds = duration / 1000 % 60

        var timeString = "$minutes:"
        if (minutes < 10) {
            timeString = "0$minutes:"
        }
        if (seconds < 10) timeString += "0"
        timeString += seconds

        return timeString
    }
    private fun setUpSeekBar() = lifecycleScope.launch(Dispatchers.Main) {
        binding.seekBar.max = playerService!!.getDurationPosition()
        if (playerService?.mediaPlayer != null) {
            try {
                binding.seekBar.progress = playerService!!.getCurrentPosition()
                while (playerService?.mediaPlayer!!.isPlaying) {
                    binding.txtStartDuration.text =
                        millisToString(playerService!!.getCurrentPosition())
                    binding.seekBar.progress = playerService!!.getCurrentPosition()
                    delay(1000)
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }
    fun setUpRepeatSong(){
        if(AppPreference.repeatSong){
            binding.btnShuffle.setColorFilter(requireContext().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        }
        binding.btnShuffle.setOnClickListener {
            if(AppPreference.repeatSong){
                AppPreference.repeatSong = false
                binding.btnShuffle.setColorFilter(requireContext().getColor(R.color.black), PorterDuff.Mode.SRC_IN)
            }else{
                AppPreference.repeatSong = true
                binding.btnShuffle.setColorFilter(requireContext().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN)

            }
        }
    }
    fun onClickBtnFavorite(){
        binding.btnFavorite.setOnClickListener {
            if(AppPreference.isLogin) {
//                if (it.isSelected) {
//                    it.isSelected = false
//                    binding.btnFavorite.setColorFilter(
//                        requireContext().getColor(R.color.black),
//                        PorterDuff.Mode.SRC_IN
//                    )
//
//                } else {
//                    it.isSelected = true
//                    binding.btnFavorite.setColorFilter(
//                        requireContext().getColor(R.color.gold),
//                        PorterDuff.Mode.SRC_IN
//                    )
//                    PlayerManager.music?.value?.let { music ->
//                        MusicDB.saveDB(music[AppPreference.indexPlaying])
//                        Log.e("nameSong : ", music[AppPreference.indexPlaying].nameSong)
//                    }
//                }
            }else{
                showToast("you have not sign in!")
            }
        }
    }
    fun downloadMp3(){
        var urlSong : String = ""
        PlayerManager.changeSong.observe(viewLifecycleOwner) {
            if (it) {
                PlayerManager.music?.value?.let { music ->
                    urlSong = music[AppPreference.indexPlaying].url
                }
            }
        }
        binding.btnDownload.setOnClickListener {
            showToast("Downloading")
            PlayerManager.music?.value?.let { music->
                MusicDB.saveDB(music[AppPreference.indexPlaying])
            }
            Handler(Looper.getMainLooper()).postDelayed({
                showToast("Success")
            },1500)
//            if(!it.isSelected){
//                it.isSelected = true
//                if(urlSong != ""){
//                 showToast("Downloading")
//                    loadMp3(urlSong)
//                }else{
//                }
//            }
        }
    }
    fun loadMp3(url : String){
        FileLoader.multiFileDownload(context)
            .fromDirectory(Environment.DIRECTORY_DOWNLOADS, FileLoader.DIR_EXTERNAL_PUBLIC)
            .progressListener(object : MultiFileDownloadListener() {
                override fun onProgress(downloadedFile: File?, progress: Int, totalFiles: Int) {
                    Log.e("progress : ",progress.toString())
                    var percent = (100.0 * progress)/totalFiles
                    showToastShort(percent.toString()+"%")
                    if(progress == totalFiles){
                        binding.btnDownload.isSelected = false
                        val intent = Intent("download")
                    activity?.sendBroadcast(intent)
                    }
                }
                override fun onError(e: Exception, progress: Int) {
                    super.onError(e, progress)
                }
            }).loadMultiple(url)
    }
    fun btnShare(){
        binding.btnShare.setOnClickListener {
        }
    }

}



