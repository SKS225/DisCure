package com.example.discure

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.discure.databinding.ActivityLoginBinding
import com.example.discure.viewModels.LoginActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth

        setContentView(binding.root)

        checkUser()
        viewModel = ViewModelProvider(this)[LoginActivityViewModel::class.java]


        binding.etEmail.doAfterTextChanged { viewModel.email.value=it.toString() }
        binding.etPass.doAfterTextChanged { viewModel.pass.value=it.toString() }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finishAffinity()
        }

        binding.btnLogin.setOnClickListener {
            binding.progressBar.visibility=View.VISIBLE
            viewModel.login(binding,this)
        }

    }

    /*private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            Log.d(TAG, "Sign in successful!")
            startActivity(Intent(this,MainActivity::class.java))
        } else {
            Toast.makeText(
                this,
                "There was an error signing in",
                Toast.LENGTH_SHORT).show()

            val response = result.idpResponse
            if (response == null) {
                Log.w(TAG, "Sign in canceled")
            } else {
                Log.w(TAG, "Sign in error", response.error)
            }
        }
    }*/

    private fun checkUser(){
        if(auth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finishAffinity()
        }
    }

    /*private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                viewModel.handleResult(task, binding, this, button)
            }
        }*/

}