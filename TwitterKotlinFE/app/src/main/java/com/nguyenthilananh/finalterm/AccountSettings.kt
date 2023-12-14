package com.nguyenthilananh.finalterm

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.nguyenthilananh.finalterm.Model.User
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
// import binding
import com.nguyenthilananh.finalterm.databinding.ActivityAccountSettingsBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class AccountSettings : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private var checker=""
    private  var myUrl=""
    private  var imageUri: Uri?=null
    private  var storageProfileRef:StorageReference?=null
    private lateinit var binding: ActivityAccountSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAccountSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfileRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")
        getUserInfo()

        binding.accountSettingsLogoutbtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@AccountSettings, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.closeButton.setOnClickListener {
            val intent=Intent(this@AccountSettings,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


        binding.accountSettingsChangeProfile.setOnClickListener {
             checker="clicked"

            ImagePicker.with(this).crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        binding.saveEditedInfo.setOnClickListener {
            if (checker == "clicked")
            {
              uploadProfileImageandInfo()
            }
            else
            {
                 updateUserInfoOnly()
            }

        }
    }

    private fun uploadProfileImageandInfo() {

        when {
            imageUri == null -> Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT)
                .show()

            TextUtils.isEmpty(binding.accountSettingsFullnameProfile.text.toString()) -> {
                Toast.makeText(this, "Full Name is required", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(binding.accountSettingsUsernameProfile.text.toString()) -> {
                Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val progressDialog=ProgressDialog(this)
                progressDialog.setTitle("Profile Settings")
                progressDialog.setMessage("Please wait! Updating...")
                progressDialog.show()

                val fileRef = storageProfileRef!!.child(firebaseUser!!.uid+ ".png")

                val uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)

                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful)
                    {
                        task.exception?.let {
                            throw it
                            Toast.makeText(this, "exception:--"+it, Toast.LENGTH_SHORT).show()
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener ( OnCompleteListener<Uri>{task ->
                    if(task.isSuccessful)
                    {
                        val downloadUrl=task.result
                        myUrl=downloadUrl.toString()

                        val ref=FirebaseDatabase.getInstance().reference.child("Users")
                        val userMap = HashMap<String, Any>()
                        userMap["fullname"] = binding.accountSettingsFullnameProfile.text.toString()
                        userMap["username"] = binding.accountSettingsUsernameProfile.text.toString().toLowerCase()
                        userMap["bio"] = binding.accountSettingsBioProfile.text.toString()
                        userMap["image"] = myUrl

                        ref.child(firebaseUser.uid).updateChildren(userMap)
                        Toast.makeText(this, "Account is updated", Toast.LENGTH_SHORT).show()

                        val intent=Intent(this@AccountSettings,MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()

                    }
                    else {
                        progressDialog.dismiss()
                    }
                } )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data
            imageUri = fileUri

            binding.accountSettingsImageProfile.setImageURI(fileUri)
            Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show()
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
//            if (resultCode == Activity.RESULT_OK) {
//                val result = CropImage.getActivityResult(data)
//                if (data != null){
//                    imageUri= result.uri
////                    binding.bindingAccountSettingsImageProfile.setImageURI(imageUri)
//                }
////            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
////                val error = result.error
//            }
//        }
    }

    private fun updateUserInfoOnly() {

        when {
            TextUtils.isEmpty(binding.accountSettingsFullnameProfile.text.toString()) -> {
                Toast.makeText(this, "Full Name is required", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(binding.accountSettingsUsernameProfile.text.toString()) -> {
                Toast.makeText(this, "username is required", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
                //using hashmap to store values
                val userMap = HashMap<String, Any>()
                userMap["fullname"] = binding.accountSettingsFullnameProfile.text.toString()
                userMap["username"] = binding.accountSettingsUsernameProfile.text.toString().toLowerCase()
                userMap["bio"] = binding.accountSettingsBioProfile.text.toString()

                userRef.child(firebaseUser.uid).updateChildren(userMap)

                Toast.makeText(this, "Account is updated", Toast.LENGTH_SHORT).show()

                val intent=Intent(this@AccountSettings,MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun getUserInfo() {
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>(User::class.java)


                    val image = user!!.getImage()
                    if (image != null) {
                       // binding.accountSettingsImageProfile.setImageURI(image.toUri())

                       Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(binding.accountSettingsImageProfile)
                    }
                    else{
                        binding.accountSettingsImageProfile.setImageResource(R.drawable.profile)
                    }



//                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(binding.accountSettingsImageProfile)
                    binding.accountSettingsFullnameProfile?.setText(user.getFullname())
                    binding.accountSettingsUsernameProfile?.setText(user.getUsername())
                    binding.accountSettingsBioProfile?.setText(user.getBio())

                }
            }
        })
    }
}