package com.example.musicapp.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.databinding.HomeSuggestItemBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Music
import com.example.musicapp.utils.loadImage
import com.example.musicapp.utils.toUpperString

class HomeSuggestAdapter(val arrayMusic : ArrayList<Music>) : RecyclerView.Adapter<HomeSuggestAdapter.HomeSuggestViewHolder>()  {
    class HomeSuggestViewHolder(val binding : HomeSuggestItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeSuggestViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HomeSuggestItemBinding.inflate(layoutInflater,parent,false)
        return HomeSuggestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeSuggestViewHolder, position: Int) {
        val music = arrayMusic[position]
        holder.binding.lbTitle.text = music.nameSong.toUpperString()
        holder.binding.lbSub.text = music.artist?.toUpperString()
        loadImage(holder.binding.imv,music.image)
        holder.binding.root.setOnClickListener {
            AppPreference.indexPlaying = position
            PlayerManager.music?.value = arrayMusic
            PlayerManager.playerService?.playIndex()
            AppPreference.playStatus = 0
          AppPreference.saveAPI = "top100"
        }
    }

    override fun getItemCount(): Int {
        return arrayMusic.size
    }
}