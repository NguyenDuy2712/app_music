package com.example.musicapp.utils.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.HomeFirebase
import com.example.musicapp.ui.main.home.HomeAdapter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        bind()
    }

    //
    open fun setUpView() {
    }

    open fun bind() {

    }

    fun getDataFirebase(path: String, success: (DataSnapshot) -> Unit) {
//        PlayerManager.database.getReference(path).get().addOnSuccessListener { snapshot ->
//            success(snapshot)
//        }
//            .addOnFailureListener { error ->
//                Log.e("database", "loadPost:onCancelled " + error.message.toString())
//
//            }
         PlayerManager.database.getReference(path).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("data", "change :"+path)

                success(snapshot)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("database", "loadPost:onCancelled "+ error.message.toString())
            }
        })
    }

    fun storeRef(): StorageReference {
        return FirebaseStorage.getInstance().reference.child("myUpload")
    }

    //
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    fun showToastShort(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showAlert(message: String) {
        val builder = AlertDialog.Builder(requireActivity())
        with(builder)
        {
            setTitle("")
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
                //nothing
            }
            show()
        }
    }

}