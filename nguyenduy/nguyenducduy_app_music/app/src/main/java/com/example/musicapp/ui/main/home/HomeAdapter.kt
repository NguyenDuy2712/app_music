package com.example.musicapp.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.databinding.*
import com.example.musicapp.models.home.Home
import com.example.musicapp.models.home.HomeFirebase
import com.example.musicapp.models.home.Music
import com.example.musicapp.utils.GridSpacingItemDecoration
import com.example.musicapp.utils.loadImage
import com.example.musicapp.utils.toUpperString
import com.google.firebase.database.ValueEventListener

interface HomeInterFace{
    fun onClickBXH()
}

class HomeAdapter(val listener: HomeListenerToDayInterface, val home: HomeFirebase, val homeInterFace: HomeInterFace):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class HomeListenTodayViewHolder(val binding : HomeListenTodayBinding):RecyclerView.ViewHolder(binding.root)
    class HomeNewSongViewHolder(val binding : HomeListenTodayBinding):RecyclerView.ViewHolder(binding.root)
    class HomeNewAlbumViewHolder(val binding : HomeListenTodayBinding):RecyclerView.ViewHolder(binding.root)
    class HomeSongChartViewHolder(val binding : HomeListenTodayBinding):RecyclerView.ViewHolder(binding.root)
    class HomeSuggestViewHolder(val binding : HomeListenTodayBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        when(viewType){
            0->{
                 val adapterListenToday = HomeListenTodayAdapter(home.todaySong,listener,0)

                val binding = HomeListenTodayBinding.inflate(layoutInflater,parent,false)
                binding.lbTitle.setText(R.string.listen_today)
                binding.listView.layoutManager = GridLayoutManager(parent.context,3)
                binding.listView.addItemDecoration(GridSpacingItemDecoration(3,3,true,0))
                binding.listView.adapter = adapterListenToday
                return HomeListenTodayViewHolder(binding)
            }
            1->{
                val adapterListenToday = HomeListenTodayAdapter(home.newSong,listener,1)

                val binding = HomeListenTodayBinding.inflate(layoutInflater,parent,false)
                binding.lbTitle.setText(R.string.released_song)
                binding.listView.layoutManager = GridLayoutManager(parent.context,3)
                binding.listView.addItemDecoration(GridSpacingItemDecoration(3,3,true,0))
                binding.listView.adapter = adapterListenToday
                return HomeNewSongViewHolder(binding)
            }
            2->{
                val adapterListenToday = HomeListenTodayAdapter(home.albums,listener,2)
                val binding = HomeListenTodayBinding.inflate(layoutInflater,parent,false)
                binding.lbTitle.setText(R.string.albums)
                binding.listView.layoutManager = GridLayoutManager(parent.context,3)
                binding.listView.addItemDecoration(GridSpacingItemDecoration(3,3,true,0))
                binding.listView.adapter = adapterListenToday
                return HomeNewAlbumViewHolder(binding)
            }
            3->{
                val binding = HomeListenTodayBinding.inflate(layoutInflater,parent,false)
                binding.lbTitle.setText(R.string.v_pop_chart)
                val bindingSong = HomeSongChartItemBinding.inflate(layoutInflater,parent,false)
                loadImage(bindingSong.imv1,home.topSong[0].image)
                bindingSong.lb1.text = home.topSong[0].name.toUpperString()
                bindingSong.sub1.text = home.topSong[0].artist?.toUpperString()

                loadImage(bindingSong.imv2,home.topSong[1].image)
                bindingSong.lb2.text = home.topSong[1].name.toUpperString()
                bindingSong.sub2.text = home.topSong[1].artist?.toUpperString()

                loadImage(bindingSong.imv3,home.topSong[2].image)
                bindingSong.lb3.text = home.topSong[2].name.toUpperString()
                bindingSong.sub3.text = home.topSong[2].artist?.toUpperString()

                binding.view.addView(bindingSong.root)
                binding.view.setOnClickListener {
                    homeInterFace.onClickBXH()
                }
                return HomeSongChartViewHolder(binding)
            }
            else -> {
                 var adapterSuggest  = HomeSuggestAdapter(home.top100)

                val binding = HomeListenTodayBinding.inflate(layoutInflater,parent,false)
                binding.lbTitle.setText(R.string.suggested_for_you)
                binding.listView.layoutManager = LinearLayoutManager(parent.context,LinearLayoutManager.VERTICAL,false)
                binding.listView.adapter = adapterSuggest

                return HomeSuggestViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}