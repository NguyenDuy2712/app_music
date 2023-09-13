package com.example.musicapp.ui.main.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.databinding.TopicItemBinding
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Topic
import com.example.musicapp.utils.loadImageWithSize
import com.example.musicapp.utils.toUpperString

interface TopicAdapterInterface {
    fun onClickItem(lbTitle : String)
}

class TopicAdapter(val listener : TopicAdapterInterface,val topic : ArrayList<Topic>) :RecyclerView.Adapter<TopicAdapter.TopicItemViewHolder>(){

    class TopicItemViewHolder(val binding : TopicItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicItemViewHolder {
        val layoutInFlate = LayoutInflater.from(parent.context)
        val binding = TopicItemBinding.inflate(layoutInFlate,parent,false)
        return TopicItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopicItemViewHolder, position: Int) {
 val item = topic[position]
            holder.binding.lbTitle.text = item.name.toUpperString()
        loadImageWithSize(holder.binding.imvTopic,item.image,300,300)

        holder.binding.root.setOnClickListener{
                PlayerManager.api = item.path
                listener.onClickItem(item.path)
        }
    }
    override fun getItemCount(): Int {
        return topic.size
    }

}