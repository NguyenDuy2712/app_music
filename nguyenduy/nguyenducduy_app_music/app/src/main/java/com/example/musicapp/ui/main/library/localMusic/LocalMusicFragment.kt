package com.example.musicapp.ui.main.library.localMusic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.databinding.FavoriteMusicFragmentBinding
import com.example.musicapp.databinding.LocalMusicFragmentBinding
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Music
import com.example.musicapp.utils.base.BaseFragment

class LocalMusicFragment() : BaseFragment() {


    private lateinit var binding : LocalMusicFragmentBinding
    private lateinit var adapter: LocalMusicAdapter
    var arrayMusic = PlayerManager.musicLocal


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LocalMusicFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun setUpView() {
        super.setUpView()
        adapter = LocalMusicAdapter(PlayerManager.musicLocal)
//        binding.listView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
//        binding.listView.adapter = adapter
//        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
//            ItemTouchHelper.LEFT, 0) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false
//            }
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                if(direction == ItemTouchHelper.LEFT){
//                    Log.e("onSwiped : ","true")
//                    val position = viewHolder.adapterPosition
//                   adapter.removeAt(position)
//                }
//            }
//            override fun getMovementFlags(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder
//            ): Int {
//                val dragFlags = ItemTouchHelper.LEFT
//                val swipeFlags = ItemTouchHelper.LEFT
//                return makeMovementFlags(dragFlags, swipeFlags)
//            }
//        }
//        )
//        touchHelper.attachToRecyclerView(binding.listView)
    }

    override fun bind() {
        super.bind()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}