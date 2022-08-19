package com.example.snapchatclone

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.snapchatclone.databinding.ActivitySnapsBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class Snaps : AppCompatActivity() {

    lateinit var binding: ActivitySnapsBinding
    lateinit var ImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.selectImageBtn.setOnClickListener {

            selectImage()

        }

        binding.uploadImageBtn.setOnClickListener {

            uploadImage()


        }
    }

    private fun uploadImage() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("uploading File ....")
        progressDialog.setCancelable(true)
        progressDialog.show()

        val formatter = SimpleDateFormat( "yyyy_mm_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        val message = binding.snapMessage.text.toString()
        storageReference.putFile(ImageUri).
                continueWithTask{ task->
                    if(!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    storageReference.downloadUrl
                }.addOnCompleteListener{task ->
            if(task.isSuccessful) {
                val downloadUrl = task.result
                val intent = Intent(this, ChooseUserActivity::class.java)

                intent.putExtra("imageURL",downloadUrl.toString())
                intent.putExtra("imageName",fileName)
                intent.putExtra("message",message)
                startActivity(intent)
            }

        }








    }

    private fun selectImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK){

            ImageUri = data?.data!!
            binding.firebaseImage.setImageURI(ImageUri)



        }

    }
}