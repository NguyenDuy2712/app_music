package com.example.musicapp.ui.main.library.favoriteMusic

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.database.DataBaseManager
import com.example.musicapp.database.MusicDB
import com.example.musicapp.databinding.FavoriteMusicFragmentBinding
import com.example.musicapp.models.home.Music
import com.example.musicapp.utils.base.BaseFragment
import io.realm.Realm
import io.realm.RealmChangeListener


class FavoriteMusicFragment : BaseFragment() {

    private lateinit var viewModel: FavoriteMusicViewModel
    private lateinit var binding : FavoriteMusicFragmentBinding
    private lateinit var adapter: FavoriteAdapter
    private lateinit var realmListener: RealmChangeListener<Realm>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavoriteMusicFragmentBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(FavoriteMusicViewModel::class.java)
        return binding.root
    }

    override fun setUpView() {
        super.setUpView()
        setUpAdapter()
        registerRealmListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        DataBaseManager.realm.removeChangeListener(realmListener)
    }

    override fun bind() {
        super.bind()
    }
    @SuppressLint("SuspiciousIndentation")
    fun setUpAdapter(){
        val arrayMusic = ArrayList<Music>()
            MusicDB.getDB().forEach {
            val music = Music(it.id,it.nameSong,it.artist,it.url,it.image,it.thumb,it.path)
            arrayMusic.add(music)
        }
        if(arrayMusic.size != 0){
            adapter = FavoriteAdapter(arrayMusic)
            binding.listView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false)
            binding.listView.adapter = adapter

            val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT, 0) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if(direction == ItemTouchHelper.LEFT){
                        Log.e("onSwiped : ","true")
                        val position = viewHolder.adapterPosition
                        adapter.removeAt(position)
                    }
                }
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    val dragFlags = ItemTouchHelper.LEFT
                    val swipeFlags = ItemTouchHelper.LEFT
                    return makeMovementFlags(dragFlags, swipeFlags)
                }
            }
            )
            touchHelper.attachToRecyclerView(binding.listView)
        }
    }
    fun registerRealmListener(){
        realmListener = RealmChangeListener {
            setUpAdapter()
            Log.e("realm : ","change")
        }
        DataBaseManager.realm.addChangeListener(realmListener)
    }
}