package com.example.musicapp

import android.Manifest
import android.content.*
import android.content.res.Resources
import android.os.Bundle
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.services.PlayerService
import com.example.musicapp.utils.UIApplicationUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class MainActivity : AppCompatActivity() {

    companion object{
        var playerService : PlayerService? = null
    }
    private var serviceToken: PlayerManager.ServiceToken? = null

    private lateinit var binding : ActivityMainBinding
    private lateinit var fragmentHost : NavHostFragment
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    val receiverDownload: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            PlayerManager.musicLocal.clear()
            PlayerManager.queryAudio()
            Log.e("receiver","on")
        }
    }
    private val panelState: Int
        get() = bottomSheetBehavior.state

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    collapsePanel()
                }
                else -> {

                }
            }
        }
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            setMiniPlayerAlpha(slideOffset)
           setBottomNavigationViewTransition(slideOffset)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intenFilter = IntentFilter("download")
        registerReceiver(receiverDownload, intenFilter)
        getFileExternal()
        setUpService()
        fragmentHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            true
        }
        binding.bottomNav.setupWithNavController(fragmentHost.navController)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.slidingPanel)
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        onBind()


    }

    override fun onResume() {
        super.onResume()
        window.navigationBarColor = ContextCompat.getColor(this, R.color.colorAccent);
        UIApplicationUtils.transparentStatusBar(this, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallback)
        unregisterReceiver(receiverDownload)
    }

    override fun onSupportNavigateUp(): Boolean {
        return fragmentHost.navController.navigateUp()
    }
    fun hideBottomNavigation(){
        binding.bottomNav.visibility = View.GONE
    }
    fun showBottomNavigation(){
        binding.bottomNav.visibility = View.VISIBLE
        actionBar?.hide()
    }

    private fun setMiniPlayerAlpha(slideOffset: Float) {
        val alpha = (0.5 - slideOffset).toFloat()
        //Log.e("alpha : ",alpha.toString())
        binding.miniPlayerFragment.alpha = alpha
        binding.miniPlayerFragment.visibility = if (alpha == 0f) View.INVISIBLE else View.VISIBLE
    }
    private fun setBottomNavigationViewTransition(slideOffset: Float) {
       binding.bottomNav.translationY = slideOffset * 550
    }
     fun expandPanel() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        setMiniPlayerAlpha(1f)
    }
    private fun collapsePanel() {
        setMiniPlayerAlpha(-0.5f)
       // binding.miniPlayerFragment.alpha = 1f
    }
    fun hideBottomSheet() {
        bottomSheetBehavior.peekHeight = 195
    }
    fun showBottomSheet(){
        bottomSheetBehavior.peekHeight = 385
    }
    fun setUpService(){
        serviceToken = PlayerManager.bindToService(this, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
              //  reloadPlayerFragment()
                if (PlayerManager.playerService?.mediaPlayer == null && PlayerManager.music != null) {
                   PlayerManager.playerService?.playIndex()
                }
            }
            override fun onServiceDisconnected(name: ComponentName) {
                PlayerManager.playerService = null
            }
        })
    }
    private fun onBind(){

    }
    fun getFileExternal(){
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Log.e("permission : ","onClick")
                    if(PlayerManager.musicLocal.isEmpty()){
                        PlayerManager.queryAudio()
                    }
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }
            }).check()
    }
    fun hideMiniPlayer(){
        binding.slidingPanel.visibility = View.INVISIBLE
    }
    fun showMiniPlayer(){
        binding.slidingPanel.visibility = View.VISIBLE

    }
}

