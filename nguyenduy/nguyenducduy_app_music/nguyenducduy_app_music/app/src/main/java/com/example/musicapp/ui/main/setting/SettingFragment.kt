package com.example.musicapp.ui.main.setting

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.navigation.fragment.findNavController
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.databinding.SettingFragmentBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.AuthProviderServices
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.services.PlayerService
import com.example.musicapp.ui.LoginActivity
import com.example.musicapp.utils.base.BaseFragment
import com.example.musicapp.utils.loadImage
import com.example.musicapp.utils.loadImagePicasso

class SettingFragment : BaseFragment() {

    private lateinit var viewModel: SettingViewModel
    private lateinit var binding : SettingFragmentBinding

    private val playerService: PlayerService?
        get() = PlayerManager.playerService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).showBottomNavigation()
        (activity as MainActivity).showMiniPlayer()
        binding = SettingFragmentBinding.inflate(inflater,container,false)
        return binding.root

    }
    @SuppressLint("SetTextI18n")
    override fun setUpView() {
        super.setUpView()
        binding.btnUpload.setOnClickListener{
            val action = SettingFragmentDirections.actionSettingFragmentToUploadFragment()
            findNavController().navigate(action)
        }
        if(AppPreference.isLogin){
            binding.titleLogin.text = "Sign Out"
            binding.name.text = AppPreference.user_email
            loadImagePicasso(binding.profileImage,AppPreference.user_name!!)
        }else{
            binding.name.text = "Nguyen Duy"
            binding.titleLogin.text = "Sign In"
        }

        binding.viewLogout.setOnClickListener {
            playerService?.pause()
            playerService?.stopAudio()
            AppPreference.isLogin = false
            val intent = Intent(requireActivity(),LoginActivity::class.java)
            startActivity(intent)
            finishAffinity(requireActivity())
            AuthProviderServices.signOutGoogle {
            }
        }
    }



}