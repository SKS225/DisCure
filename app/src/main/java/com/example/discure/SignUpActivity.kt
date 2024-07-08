package com.example.discure

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.discure.databinding.ActivitySignUpBinding
import com.example.discure.viewModels.SignUpActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: SignUpActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        auth = Firebase.auth

        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SignUpActivityViewModel::class.java]

        binding.etEmail.addTextChangedListener { viewModel.email.value = it.toString() }
        binding.etPass.doAfterTextChanged { viewModel.pass.value=it.toString() }
        binding.etConfirmPass.doAfterTextChanged { viewModel.confirmPass.value=it.toString() }
        binding.etName.doAfterTextChanged { viewModel.name.value=it.toString() }

        binding.tvChoosePic.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            launcher.launch(intent)
        }

        binding.btnSignUp.setOnClickListener {
            binding.prg.visibility = View.VISIBLE
            viewModel.signUp(binding,this)
        }

        binding.back.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finishAffinity()
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                binding.done.visibility = View.VISIBLE
                viewModel.uriAvail.value = true
                viewModel.uri.value = it.data!!.data
            }
        }
}