package com.example.musicapp.ui.player.player.playList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.example.musicapp.R
import com.example.musicapp.databinding.PlayListFragmentBinding
import com.example.musicapp.manager.PlayerManager

class PlayListFragment : SuperBottomSheetFragment() {

    private lateinit var viewModel: PlayListViewModel
    private lateinit var binding : PlayListFragmentBinding
    private lateinit var adapter : PlayListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlayListFragmentBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(PlayListViewModel::class.java)
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        bind()
    }
    fun setUp(){
        if(PlayerManager.music != null){
            adapter = PlayListAdapter()
            binding.listView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            binding.listView.adapter = adapter

            val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP + ItemTouchHelper.DOWN, 0) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    adapter.moveItem(viewHolder.adapterPosition,target.adapterPosition)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }
            }
            )

            touchHelper.attachToRecyclerView(binding.listView)
        }
    }
    fun bind(){

    }

    override fun getPeekHeight(): Int {
        return 1000
    }

    override fun getExpandedHeight(): Int {
        return super.getExpandedHeight()
    }

    override fun getCornerRadius(): Float {
        return 20f
    }

}