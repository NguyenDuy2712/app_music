package com.example.musicapp.ui.main.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.databinding.HomeFragmentBinding
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.HomeFirebase
import com.example.musicapp.ui.main.home.homeIcon.HomeIconFragment
import com.example.musicapp.ui.main.home.song.SongAdapter
import com.example.musicapp.utils.base.BaseFragment
import com.example.musicapp.utils.loadImage
import com.example.musicapp.utils.loadImagePicasso
import com.example.musicapp.utils.loadImageWithSize
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class HomeFragment : BaseFragment(),HomeListenerToDayInterface,HomeInterFace {

    private lateinit var binding : HomeFragmentBinding
    private lateinit var adapter: HomeAdapter
    private lateinit var viewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(layoutInflater,container,false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        (activity as MainActivity).showBottomNavigation()
        (activity as MainActivity).showBottomSheet()

        return binding.root
    }

    override fun setUpView() {
        super.setUpView()
        binding.loadingView.visibility = View.VISIBLE
        binding.scrollView.visibility = View.INVISIBLE

         viewModel.path = "home"
        //loadImagePicasso(binding.viewHeaderItem.imv,"https://topshare.com.vn/htdocs/images/news/2021/02/20/800/webp/6030cb2c1e3f8_maxresdefault.webp")
//        getDataFirebase("home"){ snapshot ->
//            val home = snapshot.getValue<HomeFirebase>()
//            if(home != null){
//                binding.loadingView.visibility = View.INVISIBLE
//                binding.scrollView.visibility = View.VISIBLE
//                adapter = HomeAdapter(this@HomeFragment,home,this@HomeFragment)
//                binding.listView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
//                binding.listView.isNestedScrollingEnabled = false
//                binding.listView.adapter = adapter
//            }
//        }
        binding.viewHomeSuggestForYou.root.setOnClickListener {
            PlayerManager.api = "nhactre"
            val action = HomeFragmentDirections.actionHomeFragmentToSongFragment("nhactre")
            findNavController().navigate(action)
        }
        binding.btnSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }
        binding.viewHeaderItem.root.setOnClickListener {
            PlayerManager.api = "top100"
            val action = HomeFragmentDirections.actionHomeFragmentToDetailTopicFragment("top100")
            findNavController().navigate(action)
        }
    }
    override fun bind() {
        super.bind()
        viewModel.getSuggest()?.observe(viewLifecycleOwner){
            it?.let { home ->
                binding.loadingView.visibility = View.INVISIBLE
                binding.scrollView.visibility = View.VISIBLE
                adapter = HomeAdapter(this@HomeFragment,home,this@HomeFragment)
                binding.listView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                binding.listView.isNestedScrollingEnabled = false
                binding.listView.adapter = adapter
            }
        }
    }
    override fun onClickItem(type: Int, position: Int) {
        when(type){
            0->{
                when(position){
                    0 -> {
                        PlayerManager.api = "tiktok"
                       navigateToDetail("tiktok")
                    }
                    1->{
                        PlayerManager.api = "china"
                        navigateToDetail("china")
                    }
                    else ->{
                        PlayerManager.api = "nhactre"
                        navigateToDetail("nhactre")
                    }
                }
            }
            1->{
                PlayerManager.api = "newSong"
                navigateToDetail("newSong")
            }
            else ->{
                when(position){
                    0 -> {
                        PlayerManager.api = "tara"
                        navigateToDetail("tara")
                    }
                    1->{
                        PlayerManager.api = "usuk"
                        navigateToDetail("usuk")
                    }
                    else ->{
                        PlayerManager.api = "snsd"
                        navigateToDetail("snsd")
                    }
                }
            }
        }
    }
    fun navigateToDetail(title : String){
        val action = HomeFragmentDirections.actionHomeFragmentToDetailTopicFragment(title)
        findNavController().navigate(action)
    }

    override fun onClickBXH() {
        PlayerManager.api = "topSong"
        val action = HomeFragmentDirections.actionHomeFragmentToDetailTopicFragment("topSong")
        findNavController().navigate(action)
    }


}