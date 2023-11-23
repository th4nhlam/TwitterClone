package com.alrubaye.twitterwebservice

import android.Manifest
import  android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.alrubaye.twitterwebservice.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*


class Register : AppCompatActivity() {

    var mAuth:FirebaseAuth?=null
    private var binding: ActivityRegisterBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        mAuth= FirebaseAuth.getInstance()
        signInAnonymously()

        binding!!.ivUserImage.setOnClickListener {

            checkPermission()
        }
    }


    fun buRegisterEvent(view:View){
        binding!!.buRegister.isEnabled=false
        val name =URLEncoder.encode(binding!!?.etName?.text.toString(),"utf-8")

        val url="localhost:80/TwitterAndroidServer/Register.php?first_name="+ name + "&email="+ binding!!.etEmail.text.toString() +  "&password="+ binding!!.etPassword.text.toString()
        MyAsyncTask().execute(url)
//        SaveImageInFirebase()
        val myTask = Login.MyExecutorService(this)
        myTask.execute(url)
    }


    fun signInAnonymously(){

        mAuth!!.signInAnonymously().addOnCompleteListener(this,{ task->

            Log.d("LoginInfo:",task.isSuccessful.toString())
        })
    }


    val READIMAGE:Int=253
    fun checkPermission(){

        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf( Manifest.permission.READ_EXTERNAL_STORAGE),READIMAGE)
                return
            }
        }

        loadImage()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            READIMAGE->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loadImage()
                }else{
                    Toast.makeText(applicationContext,"Cannot access your images",Toast.LENGTH_LONG).show()
                }
            }
            else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }


    }

    val PICK_IMAGE_CODE=123
    fun loadImage(){

        var intent=Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==PICK_IMAGE_CODE  && data!=null && resultCode == RESULT_OK){

            val selectedImage=data.data
            val filePathColum= arrayOf(MediaStore.Images.Media.DATA)
            val cursor= contentResolver.query(selectedImage!!,filePathColum,null,null,null)
            cursor!!.moveToFirst()
            val coulomIndex=cursor!!.getColumnIndex(filePathColum[0])
            val picturePath=cursor!!.getString(coulomIndex)
            cursor!!.close()
            binding!!.ivUserImage.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }

    }

    fun SaveImageInFirebase(){
        var currentUser =mAuth!!.currentUser
        val email:String=currentUser!!.email.toString()
        val storage=FirebaseStorage.getInstance()
        val storgaRef=storage.getReferenceFromUrl("gs://twitterwebservice-b75b6.appspot.com")
        val df=SimpleDateFormat("ddMMyyHHmmss")
        val dataobj=Date()
        val imagePath= SplitString(email) + "."+ df.format(dataobj)+ ".jpg"
        val ImageRef=storgaRef.child("images/"+imagePath )
        binding!!.ivUserImage.isDrawingCacheEnabled = true
        binding!!.ivUserImage.buildDrawingCache()

        val drawable=binding!!.ivUserImage.drawable as BitmapDrawable
        val bitmap=drawable.bitmap
        val baos=ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data= baos.toByteArray()
        val uploadTask=ImageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(applicationContext,"fail to upload",Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->

            var DownloadURL= taskSnapshot.storage.downloadUrl.toString()!!

             // TODO: register to datavase

            Log.d("DownloadURL:",DownloadURL)

           val name =URLEncoder.encode(binding!!?.etName?.text.toString(),"utf-8")
            DownloadURL=URLEncoder.encode(DownloadURL,"utf-8")
            val url="localhost:80/TwitterAndroidServer/Register.php?first_name="+ name + "&email="+ binding!!.etEmail.text.toString() +  "&password="+ binding!!.etPassword.text.toString() +"&picture_path="+ DownloadURL
            MyAsyncTask().execute(url)
        }

    }

    fun SplitString(email:String):String{
        val split= email.split("@")
        return split[0]
    }


    // CALL HTTP
    inner class MyAsyncTask: AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            //Before task started
        }
        override fun doInBackground(vararg p0: String?): String {
            try {

                val url=URL(p0[0])

                val urlConnect=url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout=7000

                val op=Operations()

                var inString= op.ConvertStreamToString(urlConnect.inputStream)
                //Cannot access to ui
                publishProgress(inString)
            }catch (ex:Exception){}


            return " "

        }

        override fun onProgressUpdate(vararg values: String?) {
            try{
                var json=JSONObject(values[0])
                Toast.makeText(applicationContext,json.getString("msg"),Toast.LENGTH_LONG).show()

                if (json.getString("msg")== "user is added"){
                 finish()
                }else{
                    binding!!?.buRegister?.isEnabled = true
                }

            }catch (ex:Exception){}
        }

        override fun onPostExecute(result: String?) {

            //after task done
        }


    }


// Progress Dialog



}
