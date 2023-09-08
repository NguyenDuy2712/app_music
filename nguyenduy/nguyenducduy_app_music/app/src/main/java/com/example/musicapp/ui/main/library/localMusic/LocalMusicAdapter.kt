package com.example.musicapp.ui.main.library.localMusic

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.musicapp.MusicApplication
import com.example.musicapp.R
import com.example.musicapp.database.MusicDB
import com.example.musicapp.databinding.HomeSuggestItemBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Music
import com.example.musicapp.ui.main.home.HomeSuggestAdapter
import com.example.musicapp.utils.getSongThumbnail
import com.example.musicapp.utils.toUpperString
import java.security.AccessController.getContext


class LocalMusicAdapter(var arrayMusic :ArrayList<Music>):RecyclerView.Adapter<LocalMusicAdapter.LocalMusicViewHolder>() {
    class LocalMusicViewHolder(val binding : HomeSuggestItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalMusicViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HomeSuggestItemBinding.inflate(layoutInflater,parent,false)
        return LocalMusicViewHolder(binding)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun dataSetChange(array : ArrayList<Music>){
        arrayMusic = array
        notifyDataSetChanged()
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: LocalMusicViewHolder, position: Int) {
        val music = arrayMusic[position]
        Log.e("TAG","name="+music.nameSong)
        Log.e("TAG","artist="+music.artist)
        Log.e("TAG","image="+music.image)
        holder.binding.lbTitle.text = music.nameSong.toUpperString()
        holder.binding.lbSub.text = music.artist?.toUpperString()
        holder.binding.btnMore.visibility = View.GONE
        val imgByte = getSongThumbnail(music.path!!)
        Glide.with(MusicApplication.appContext).asBitmap().load(imgByte).error(R.drawable.imv_bg_music)
            .apply(RequestOptions().override(150, 150))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.binding.imv)
        holder.binding.root.setBackgroundColor(ContextCompat.getColor(MusicApplication.appContext, R.color.white));
        if(position == arrayMusic.size-1){
            holder.binding.line.visibility = View.INVISIBLE
        }
        holder.binding.root.setOnClickListener {
            AppPreference.indexPlaying = position
            PlayerManager.music?.value = arrayMusic
            PlayerManager.playerService?.playIndex()
            AppPreference.playStatus = 1
        }
    }

    override fun getItemCount(): Int {
        return arrayMusic.size
    }
    fun removeAt(index: Int) {
         arrayMusic.removeAt(index)
         notifyItemRemoved(index)
    }
}