package com.example.musicapp.ui.main.library.favoriteMusic

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.MusicApplication
import com.example.musicapp.R
import com.example.musicapp.database.MusicDB
import com.example.musicapp.databinding.HomeSuggestItemBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Music
import com.example.musicapp.ui.main.library.localMusic.LocalMusicAdapter
import com.example.musicapp.utils.getSongThumbnail
import com.example.musicapp.utils.loadImage
import com.example.musicapp.utils.toUpperString

class FavoriteAdapter(var arrayMusic :ArrayList<Music>): RecyclerView.Adapter<LocalMusicAdapter.LocalMusicViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalMusicAdapter.LocalMusicViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HomeSuggestItemBinding.inflate(layoutInflater,parent,false)
        return LocalMusicAdapter.LocalMusicViewHolder(binding)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun dataSetChange(array : ArrayList<Music>){
        arrayMusic = array
        notifyDataSetChanged()
    }
    fun removeAt(index: Int) {
       // arrayMusic.removeAt(index)
       // notifyItemRemoved(index)
        MusicDB.delete(arrayMusic[index].id)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: LocalMusicAdapter.LocalMusicViewHolder, position: Int) {
        val music = arrayMusic[position]
        holder.binding.lbTitle.text = music.nameSong.toUpperString()
        holder.binding.lbSub.text = music.artist?.toUpperString()
        holder.binding.btnMore.visibility = View.GONE
        if (music.path.isNullOrEmpty()) {
            loadImage(holder.binding.imv,music.image)
        } else {
            val imgByte = getSongThumbnail(music.path)
            Glide.with(MusicApplication.appContext).asBitmap().load(imgByte).error(R.drawable.imv_bg_music)
                .into(holder.binding.imv)
        }
        holder.binding.root.setBackgroundColor(ContextCompat.getColor(MusicApplication.appContext, R.color.white));
        if(position == arrayMusic.size-1){
            holder.binding.line.visibility = View.INVISIBLE
        }
        holder.binding.root.setOnClickListener {
            AppPreference.indexPlaying = position
            PlayerManager.music?.value = arrayMusic
            PlayerManager.playerService?.playIndex()
            AppPreference.playStatus = 2
        }
    }

    override fun getItemCount(): Int {
        return arrayMusic.size
    }
}