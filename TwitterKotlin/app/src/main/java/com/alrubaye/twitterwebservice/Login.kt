package com.alrubaye.twitterwebservice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alrubaye.twitterwebservice.databinding.ActivityLoginBinding
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService


class Login : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


    }



    fun buLoginEvent(view:View){
        // user login
        val url="localhost:80/TwitterAndroidServer/Login.php?email=" + binding?.etEmail?.text.toString() +"&password="+ binding?.etPassword?.text.toString()
        val myTask = MyExecutorService(this)
        myTask.execute(url)





        /*
        * Your task will be executed here
        * you can execute anything here that
        * you cannot execute in UI thread
        * for example a network operation
        * This is a background thread and you cannot
        * access view elements here
        *
        * its like doInBackground()
        * */



        // maketoast url
//        Toast.makeText(this,url,Toast.LENGTH_LONG).show()
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)

    }

    fun buRegisterUserEvent(view:View){
        val intent=Intent(this,Register::class.java)
        startActivity(intent)
    }

    interface ProgressListener {
        fun onProgressUpdate(progress: String)
    }


    // Usage:



    // Create an ExecutorService instance

    // Define the background task as a lambda function or a separate function



    class MyExecutorService (private val applicationContext: Context) {
        private val executor: ExecutorService = Executors.newFixedThreadPool(1)

        fun execute(url: String) {
            executor.submit {
                try {
                    val urlConnect = URL(url).openConnection() as HttpURLConnection
                    urlConnect.connectTimeout = 7000
                    val op = Operations()
                    val inString = op.ConvertStreamToString(urlConnect.inputStream)
                    onProgressUpdate(inString)
                } catch (ex: Exception) {
                }
            }
        }

        private fun onProgressUpdate(value: String) {
            try {
                val json = JSONObject(value)
                if (json.getString("msg") == "pass login") {
                    val userInfo = JSONArray(json.getString("info"))
                    val userCredentials = userInfo.getJSONObject(0)
                    showToast(userCredentials.getString("first_name"))
                    val user_id = userCredentials.getString("user_id")
                    val saveSettings = SaveSettings(applicationContext)
                    saveSettings.saveSettings(user_id)
                    finish()
                } else {
                    showToast(json.getString("msg"))
                }
            } catch (ex: Exception) {
            }
        }

        private fun showToast(message: String) {
            // Show toast message
        }

        private fun finish() {
            // Finish the activity
        }
    }



    // CALL HTTP



}
