package com.example.discure.viewModels

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.discure.MainActivity
import com.example.discure.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase

class LoginActivityViewModel : ViewModel() {
    //private val webClientId = "99593684623-qv422jv6ibmrjafgn6onuulucmmk56ue.apps.googleusercontent.com"
    private val auth = Firebase.auth
    private val database = Firebase.database

    private val emailPattern:String = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    val email: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val pass: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun login(binding: ActivityLoginBinding, activity: AppCompatActivity) {
        if (email.value.isNullOrBlank()) {
            binding.etEmail.error = "Please provide email"
            binding.progressBar.visibility = View.GONE
            return
        } else if (pass.value.isNullOrBlank()) {
            binding.etPass.error = "Please provide password"
            binding.progressBar.visibility = View.GONE
            return
        }else if (!email.value!!.matches(emailPattern.toRegex())){
            binding.etEmail.error = "Enter correct E-mail"
            binding.progressBar.visibility=View.GONE
            return
        } else if (pass.value!!.length<6){
            binding.etPass.error = "Password too short!"
            binding.progressBar.visibility=View.GONE
            return
        } else {
            auth.signInWithEmailAndPassword(email.value!!, pass.value!!).addOnSuccessListener {
                activity.startActivity(Intent(activity, MainActivity::class.java))
            }.addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    /*fun handleResult(
        task: Task<GoogleSignInAccount>,
        binding: ActivityLoginBinding,
        activity: AppCompatActivity,
    ) {
        if (task.isSuccessful) {
            val account = task.result
            if (account != null)
                updateUI(account, binding, activity)
            else Snackbar.make(
                binding.root,
                task.exception?.message.toString(),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
    private fun updateUI(
        account: GoogleSignInAccount,
        binding: ActivityLoginBinding,
        activity: AppCompatActivity,
    ) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful)
                saveUserData(it.result.user!!, activity, binding)
            else {
                Snackbar.make(binding.root, it.exception?.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun saveUserData(
        fUser: FirebaseUser,
        activity: AppCompatActivity,
        binding: ActivityLoginBinding,
    ) {
        val user = com.example.discure.models.User(fUser.displayName, fUser.email, fUser.uid, fUser.photoUrl.toString())
        database.getReference("users").child(user.uid!!).setValue(user).addOnSuccessListener {
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finishAffinity()
        }
            .addOnFailureListener {
                Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
            }
    }*/
}