package com.example.musicapp.ui.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.databinding.HomeListenTodayItemBinding
import com.example.musicapp.databinding.HomeSuggestItemBinding
import com.example.musicapp.models.home.Home
import com.example.musicapp.models.home.Song
import com.example.musicapp.models.home.Top
import com.example.musicapp.utils.loadImage
import com.example.musicapp.utils.loadImagePicasso
import com.example.musicapp.utils.toUpperString

interface HomeListenerToDayInterface{
    fun onClickItem(type: Int,position: Int)
}


class HomeListenTodayAdapter(val arrayList: ArrayList<Top>,val listener : HomeListenerToDayInterface,val type : Int):RecyclerView.Adapter<HomeListenTodayAdapter.HomeListTodayViewHolder>() {

    class HomeListTodayViewHolder(val binding : HomeListenTodayItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListTodayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HomeListenTodayItemBinding.inflate(layoutInflater,parent,false)
        return HomeListTodayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeListTodayViewHolder, position: Int) {
        var item = arrayList[position]
        holder.binding.lbTitle.text = item.name.toUpperString()
        if(item.artist != null){
            holder.binding.lbSubTitle.text = item.artist?.toUpperString()
        }
        loadImage(holder.binding.imv,item.image)
        holder.binding.root.setOnClickListener {
           listener.onClickItem(type,position)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}