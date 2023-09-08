package com.example.musicapp.ui.main.home.song

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.MusicApplication
import com.example.musicapp.R
import com.example.musicapp.databinding.HomeSuggestItemBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Music
import com.example.musicapp.utils.getSongThumbnail
import com.example.musicapp.utils.loadImage
import com.example.musicapp.utils.loadImagePicasso
import com.example.musicapp.utils.toUpperString

class SongAdapter(var arrayMusic : ArrayList<Music>):RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    class SongViewHolder(val binding : HomeSuggestItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HomeSuggestItemBinding.inflate(layoutInflater,parent,false)
        return SongViewHolder(binding)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun nofiDataChange(music : ArrayList<Music>){
        arrayMusic = music
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
       val music = arrayMusic[position]
        if (music.path.isNullOrEmpty()) {
            loadImage(holder.binding.imv,music.image)
        } else {
            val imgByte = getSongThumbnail(music.path)
            Glide.with(MusicApplication.appContext).asBitmap().load(imgByte).error(R.drawable.imv_bg_music)
                .into(holder.binding.imv)
        }
        holder.binding.lbTitle.text = music.nameSong.toUpperString()
        holder.binding.lbSub.text = music.artist?.toUpperString()
        if( position == arrayMusic.size){
            holder.binding.root.visibility = View.INVISIBLE
        }
        holder.binding.root.setOnClickListener {
            Log.e("onClick","item")
            AppPreference.indexPlaying = position
            PlayerManager.music?.value = arrayMusic
            PlayerManager.playerService?.playIndex()
            AppPreference.playStatus = 0
            AppPreference.saveAPI = PlayerManager.api
        }
    }

    override fun getItemCount(): Int {
        return arrayMusic.size
    }
}