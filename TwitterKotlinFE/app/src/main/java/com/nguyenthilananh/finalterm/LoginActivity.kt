package com.nguyenthilananh.finalterm

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
//import binding
import com.nguyenthilananh.finalterm.databinding.ActivityLoginBinding
import com.nguyenthilananh.finalterm.viewmodel.MainViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signupLink.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = binding.loginEmail.text.toString()
        val password = binding.loginPassword.text.toString()
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.login(email, password, onLoginComplete = {
            Log.d("Login", "onCreate: ${it.message}")
        })
        viewModel.loginResponse.observe(this, {

            //forwarding to home page
            val intent=Intent(this@LoginActivity,MainActivity::class.java)


        })

        when {
            TextUtils.isEmpty(email) -> Toast.makeText(
                this,
                "Username is required",
                Toast.LENGTH_SHORT
            ).show()
            TextUtils.isEmpty(password) -> Toast.makeText(
                this,
                "Password is required",
                Toast.LENGTH_SHORT
            ).show()

            else -> {
                val progressDialog = ProgressDialog(this@LoginActivity)
                progressDialog.setTitle("Login")
                progressDialog.setMessage("Logging in...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        } else {
                            val message = task.exception!!.toString()
                            Toast.makeText(this, "Password or Email Invalid", Toast.LENGTH_LONG).show()
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }

    }

    override fun onStart() {
        super.onStart()




        if(FirebaseAuth.getInstance().currentUser!=null)
        {
            //forwarding to home page
            val intent=Intent(this@LoginActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}