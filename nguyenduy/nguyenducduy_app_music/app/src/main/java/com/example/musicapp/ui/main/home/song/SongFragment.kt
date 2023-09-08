package com.example.musicapp.ui.main.home.song

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.MainActivity
import com.example.musicapp.databinding.SongFragmentBinding
import com.example.musicapp.models.home.Music
import com.example.musicapp.ui.main.home.HomeAdapter
import com.example.musicapp.utils.base.BaseFragment
import com.google.firebase.database.ktx.getValue
import kotlin.reflect.typeOf


class SongFragment : BaseFragment() {


    private lateinit var viewModel: SongViewModel
    private lateinit var binding : SongFragmentBinding
    private lateinit var adapter : SongAdapter
    val args : SongFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SongFragmentBinding.inflate(inflater,container,false)
       viewModel = ViewModelProvider(this).get(SongViewModel::class.java)
        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideBottomSheet()

        return binding.root
    }

    override fun setUpView() {
        super.setUpView()
        binding.loadingView.visibility = View.VISIBLE
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    override fun bind() {
        super.bind()
        Log.e("arg : ", args.type.toString())
        viewModel.path = args.type
        viewModel.getData()?.observe(viewLifecycleOwner){
            it?.let { musics ->
                binding.loadingView.visibility = View.INVISIBLE
                adapter = SongAdapter(musics)
                binding.listView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                binding.listView.adapter = adapter
            }
        }
//        getDataFirebase(args.type){ snap ->
//                for (music in snap.children) {
//                    val item: Music? = music.getValue(Music::class.java)
//                    item?.let { mu ->
//                        musics.add(mu)
//                    }
//                }
//
//
//        }
    }

}