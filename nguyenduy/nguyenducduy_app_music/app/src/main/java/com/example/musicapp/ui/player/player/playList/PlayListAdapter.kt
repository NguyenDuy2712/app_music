package com.example.musicapp.ui.player.player.playList

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.MusicApplication
import com.example.musicapp.R
import com.example.musicapp.databinding.PlayListItemBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.utils.toUpperString

class PlayListAdapter:RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder>(){
    class PlayListViewHolder(val binding: PlayListItemBinding):RecyclerView.ViewHolder(binding.root)

    var arrayMusic = PlayerManager.music?.value

    override fun getItemCount(): Int {
        return arrayMusic?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        val music = arrayMusic!![position]
        holder.binding.lbTitle.text = music.nameSong.toUpperString()
        holder.binding.lbSubTitle.text = music.artist?.toUpperString()
        itemPlaying(position,holder)
        holder.binding.root.setOnClickListener {
            AppPreference.indexPlaying = position
            notifyDataSetChanged()
            PlayerManager.music?.value = arrayMusic
            PlayerManager.playerService?.playIndex()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PlayListItemBinding.inflate(layoutInflater,parent,false)
        return PlayListViewHolder(binding)
    }
    fun moveItem(fromPosition: Int,toPosition:Int) : Boolean{
        val fromIndex = fromPosition
        val toIndex = toPosition

        if(toIndex >= 0 ){
            val fromItem = arrayMusic?.get(fromIndex)
            val toItem = arrayMusic?.get(toIndex)
            if (toItem != null) {
                arrayMusic?.set(fromIndex, toItem)
            }
            fromItem?.let { arrayMusic?.set(toIndex, it) }
            notifyItemMoved(fromPosition, toPosition)
            PlayerManager.music?.value = arrayMusic

        }

        return true
    }
    fun itemPlaying(position : Int,holder:PlayListViewHolder){
        if(AppPreference.indexPlaying == position){
            holder.binding.lbTitle.setTextColor(ContextCompat.getColor(MusicApplication.appContext, R.color.colorAccent))
            holder.binding.lbSubTitle.setTextColor(ContextCompat.getColor(MusicApplication.appContext, R.color.colorAccent))
            holder.binding.avi.setIndicatorColor(ContextCompat.getColor(MusicApplication.appContext, R.color.colorAccent))
            holder.binding.avi.show()
        }else{
            holder.binding.lbTitle.setTextColor(ContextCompat.getColor(MusicApplication.appContext, R.color.black))
            holder.binding.lbSubTitle.setTextColor(ContextCompat.getColor(MusicApplication.appContext, R.color.graya5))
            holder.binding.avi.setIndicatorColor(ContextCompat.getColor(MusicApplication.appContext, R.color.black))
            holder.binding.avi.hide()
        }
    }
}