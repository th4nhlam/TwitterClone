package com.nguyenthilananh.finalterm

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

import com.github.dhaval2404.imagepicker.ImagePicker
import com.nguyenthilananh.finalterm.databinding.ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {

    private  var myUrl=""
    private  var imageUri: Uri?=null
    private  var storagePostPictureRef: StorageReference?=null
    private lateinit var binding: ActivityAddPostBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storagePostPictureRef= FirebaseStorage.getInstance().reference.child("Post Picture")

        binding.dontPostPicture.setOnClickListener {
            val intent=Intent(this@AddPostActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.postPicture.setOnClickListener {
            uploadPost()
        }

        binding.pictureToBePosted.setOnClickListener {
            ImagePicker.with(this).crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
        }

    }

    private fun uploadPost() {
        when
        {
            imageUri == null -> Toast.makeText(this, "Please select image first.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(binding.writePost.text.toString()) -> Toast.makeText(this, "Please write caption", Toast.LENGTH_LONG).show()

            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Posting")
                progressDialog.setMessage("Please wait, we are posting..")
                progressDialog.show()

                val fileRef = storagePostPictureRef!!.child(System.currentTimeMillis().toString()+ ".jpg")

                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)

                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                    if (!task.isSuccessful)
                    {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener (OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful)
                    {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()



                        val ref = FirebaseDatabase.getInstance().reference.child("Posts")
                        val postid=ref.push().key

                        val postMap = HashMap<String, Any>()

                        postMap["postid"] = postid!!
                        postMap["caption"] = binding.writePost.text.toString()
                        postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                        postMap["postimage"] = myUrl

                        ref.child(postid).updateChildren(postMap)


                        val commentRef=FirebaseDatabase.getInstance().reference.child("Comment").child(postid)
                        val commentMap = HashMap<String, Any>()
                        commentMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                        commentMap["comment"] =  binding.writePost.text.toString()

                        commentRef.push().setValue(commentMap)

                        Toast.makeText(this, "Uploaded successfully", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@AddPostActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    }
                    else
                    {
                        progressDialog.dismiss()
                    }
                })
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data
            imageUri = fileUri

            binding.pictureToBePosted.setImageURI(fileUri)
            Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show()
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
 }