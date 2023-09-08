package com.example.musicapp.ui.main.leaderboard.detailLeader

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.MusicApplication
import com.example.musicapp.R
import com.example.musicapp.databinding.SongItemBinding
import com.example.musicapp.databinding.SongTabBarItemBinding
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Artist
import com.example.musicapp.utils.loadImageWithSize
import com.example.musicapp.utils.toUpperString

interface DetailLeaderArtistInterface{
    fun onClickItem(path : String)
}

class DetailLeaderArtistAdapter(val listener  : DetailLeaderArtistInterface): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class DetailTopicViewHolder(val binding : SongItemBinding): RecyclerView.ViewHolder(binding.root)
    class DetailTopicHeaderViewHolder(val binding : SongTabBarItemBinding): RecyclerView.ViewHolder(binding.root)

    var artist : ArrayList<Artist>? = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data : ArrayList<Artist>){
        artist = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType){
            0-> {
                val binding = SongTabBarItemBinding.inflate(layoutInflater,parent,false)
                DetailTopicHeaderViewHolder(binding)
            }
            else ->
            {
                val binding = SongItemBinding.inflate(layoutInflater,parent,false)
                DetailTopicViewHolder(binding)
            }
        }
    }
    override fun getItemCount(): Int {
        return if( artist!!.size != 0) artist!!.size+1 else 0
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is DetailTopicViewHolder){
            val item = artist?.get(position-1)
            holder.binding.lbNumber.visibility = View.VISIBLE
            holder.binding.lbNumber.text = position.toString()
            holder.binding.lbTitle.text = item?.name?.toUpperString()
            holder.binding.lbSub.visibility = View.INVISIBLE
            loadImageWithSize(holder.binding.imv,item?.image,80,80)

            when(position){
                1 -> {
                    setColor(holder, R.color.gold)
                }
                2->{
                    setColor(holder, R.color.sliver)
                }
                3 ->{
                    setColor(holder, R.color.bron)
                }
                else -> {
                    setColor(holder, R.color.graya5)
                }
            }
            holder.binding.root.setOnClickListener {
                PlayerManager.api = item?.path.toString()
                listener.onClickItem(item?.path.toString())
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    fun setColor(holder : DetailTopicViewHolder,color : Int){
        holder.binding.lbNumber.setTextColor(ContextCompat.getColor(MusicApplication.appContext,color))
    }
}