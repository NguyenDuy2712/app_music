package com.example.musicapp.ui.main.topic

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.databinding.TopicFragmentBinding
import com.example.musicapp.utils.GridSpacingItemDecoration
import com.example.musicapp.utils.base.BaseFragment

class TopicFragment : BaseFragment(),TopicAdapterInterface {

    private lateinit var viewModel: TopicViewModel
    private lateinit var binding : TopicFragmentBinding
    private lateinit var adpater : TopicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TopicFragmentBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(TopicViewModel::class.java)
        (activity as MainActivity).showBottomNavigation()
        (activity as MainActivity).showBottomSheet()
        return binding.root
    }

    override fun setUpView() {
        super.setUpView()
    }

    override fun bind() {
        super.bind()
        viewModel.getData()?.observe(viewLifecycleOwner){
            it?.let {
                adpater = TopicAdapter(this,it)
                binding.listView.layoutManager = GridLayoutManager(context,2)
                binding.listView.addItemDecoration(GridSpacingItemDecoration(2,5,true,0))
                binding.listView.adapter = adpater
            }
        }

    }
    override fun onClickItem(lbTitle: String) {
        val action = TopicFragmentDirections.actionTopicFragmentToDetailTopicFragment(lbTitle)
        findNavController().navigate(action)
    }

}