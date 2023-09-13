package com.example.musicapp.ui.main.home.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.MainActivity
import com.example.musicapp.MusicApplication
import com.example.musicapp.databinding.SearchFragmentBinding
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Music
import com.example.musicapp.ui.main.home.song.SongAdapter
import com.example.musicapp.utils.base.BaseFragment


class SearchFragment : BaseFragment() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: SearchFragmentBinding
    private lateinit var adapter : SongAdapter
    var arrayMusic = ArrayList<Music>()
    val filterList = ArrayList<Music>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideBottomSheet()
        return binding.root
    }

    override fun setUpView() {
        super.setUpView()
        binding.listResponse.visibility = View.VISIBLE
        setUpAdapter()

        binding.btnBack.setOnClickListener {
            hiddenFocus()
            findNavController().popBackStack()
        }
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                Log.e("text : ",s.toString())
                if(s.toString() != ""){
                    filter(s.toString())
                }
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })
        binding.btnCancel.setOnClickListener {
            binding.searchBar.setText("")
        }
    }
    override fun bind() {
        super.bind()
        binding.loadingView.visibility = View.VISIBLE
        viewModel.getData()?.observe(viewLifecycleOwner) {
            binding.loadingView.visibility = View.INVISIBLE
            arrayMusic = it
            arrayMusic.addAll(PlayerManager.musicLocal)
        }
    }
    private fun filter(name : String){
        filterList.clear()
        for(i in arrayMusic){
            if(i.nameSong.lowercase().contains(name.lowercase()) ||i.artist?.lowercase()!!.contains(name.lowercase()) ){
                filterList.add(i)
            }
        }
        adapter.nofiDataChange(filterList)
        Log.e("filter: ","size "+filterList.size)
    }
    fun setUpAdapter(){
        adapter = SongAdapter(filterList)
        binding.listResponse.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.listResponse.adapter = adapter
    }
    fun hiddenFocus(){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow( requireActivity().currentFocus?.windowToken, 0)
    }
}