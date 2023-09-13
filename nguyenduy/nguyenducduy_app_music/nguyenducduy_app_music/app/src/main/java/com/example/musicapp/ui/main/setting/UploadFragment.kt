package com.example.musicapp.ui.main.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.MainActivity
import com.example.musicapp.databinding.UploadFragmentBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Music
import com.example.musicapp.utils.base.BaseFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.callbackFlow

class UploadFragment : BaseFragment() {
    private lateinit var viewModel: UploadViewModel
    private lateinit var binding: UploadFragmentBinding
    private val CODE_IMAGE = 101
    private val CODE_AUDIO = 102

    var imageUri: Uri? = null
    var audioUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideMiniPlayer()
        binding = UploadFragmentBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get(UploadViewModel::class.java)

        return binding.root
    }

    override fun setUpView() {
        super.setUpView()
        binding.txtImage.setOnClickListener {
            hiddenFocus()
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, CODE_IMAGE)
        }
        binding.txtMp3.setOnClickListener {
            hiddenFocus()
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "audio/*"
            startActivityForResult(intent, CODE_AUDIO)
        }
        binding.btnUpload.setOnClickListener {
            if(AppPreference.isLogin){
                upload()
            }else{
                showToast("You have not sign in !")
            }
        }
    }
    fun upload(){
        checkNotEmpty()
    }
    fun checkNotEmpty(){

        if(binding.txtNameSong.text.toString() == ""){
            showToast("Bạn chưa nhập tên bài hát")
            return
        }
        if(binding.txtArtist.text.toString() == ""){
            showToast("Bạn chưa nhập tên nghệ sĩ")
            return
        }
        if(imageUri == null){
            showToast("Bạn chưa chọn file ảnh")
            return
        }
        if(audioUri == null){
            showToast("Bạn chưa chọn file mp3")
            return
        }
        binding.progress.visibility = View.VISIBLE
        binding.btnUpload.visibility = View.INVISIBLE

        uploadImage(){ imageUrl,statusMusic ->
            if(statusMusic){
                uploadAudio { audioUrl,statusAudio ->
                    if(statusAudio){
                        val music = Music("id",
                            binding.txtNameSong.text.toString(),
                            binding.txtArtist.text.toString(),
                            audioUrl,
                            imageUrl,
                            "https://upanh123.com/wp-content/uploads/2020/11/hinh-anh-mua-buon-dep-lang-man.jpg",
                            null
                        )
                        Log.e("music :","name :"+music.nameSong.toString())
                        Log.e("music :","artist :"+music.artist.toString())
                        Log.e("music :","imageUrl :"+music.image.toString())
                        Log.e("music :","audioUrl :"+music.url.toString())
                        addDataToFirebase(music)
                    }
                }
            }

        }

    }
    fun uploadImage(calBack:(String,Boolean)->Unit){
        if (imageUri != null) {
                val filePath = storeRef().child(binding.txtNameSong.text.toString()+ ".jpg")
                filePath.putFile(imageUri!!)
                    .addOnSuccessListener {
                        filePath.downloadUrl.addOnSuccessListener { uri ->
                            calBack(uri.toString(),true)
                        }

                    }.addOnProgressListener {
                    val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
                    Log.e("progress", "Upload is $progress% done")
                        binding.progress.progress = (progress/2).toFloat()
                }
                    .addOnFailureListener {
                        Log.e("error : ", it.message.toString())
                        showToastShort("Upload Image Failed")
                        calBack("",false)

                    }
            }
    }
    fun uploadAudio(callBack:(String,Boolean)->Unit){
        if(audioUri != null){
                val filePath = storeRef().child(binding.txtNameSong.text.toString() + ".mp3")
                    filePath.putFile(audioUri!!)
                    .addOnSuccessListener {
                        filePath.downloadUrl.addOnSuccessListener { uri ->
                            callBack(uri.toString(),true)
                        }
                    }.addOnProgressListener {
                        val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
                            binding.progress.progress = 50+(progress/2).toFloat()
                        }
                    .addOnFailureListener {
                        Log.e("error : ", it.message.toString())
                        callBack("",false)
                    }
            }
    }
    fun hiddenFocus(){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow( requireActivity().currentFocus?.windowToken, 0)
    }

    fun addDataToFirebase(music : Music) {

        val refer =  PlayerManager.database.getReference("upload")
        refer.push().setValue(music).addOnSuccessListener {
            binding.progress.visibility = View.INVISIBLE
            binding.btnUpload.visibility = View.VISIBLE
            showToast("Upload Music Success")

        }.addOnFailureListener {
            showToast("Upload Music Failed")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_IMAGE && data != null) {
            imageUri = data.data
            binding.txtImage.text = imageUri.toString()
        } else if (requestCode == CODE_AUDIO && data != null) {
            audioUri = data.data
            binding.txtMp3.text = audioUri.toString()
        } else {

        }

    }


}