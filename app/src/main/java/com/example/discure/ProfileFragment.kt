package com.example.discure

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.discure.databinding.FragmentProfileBinding
import com.example.discure.models.User
import com.example.discure.viewModels.ProfileViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private val recipientEmail = "sks225dv@gmail.com"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]


        viewModel.user.observe(viewLifecycleOwner) { putValues(it) }

        binding.profilePic.setOnClickListener {  }
        binding.account.setOnClickListener{ }

        binding.notification.setOnClickListener {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            startActivity(intent)
        }

        binding.contactUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$recipientEmail")
            }
            intent.putExtra(Intent.EXTRA_SUBJECT,"Query regarding Discure")
            startActivity(intent)
        }
        binding.privacyPolicy.setOnClickListener {
            showPrivacyPolicyDialog(requireContext())
        }

        binding.logout.setOnClickListener {
            Firebase.auth.signOut()
            requireActivity().startActivity(Intent(activity, LoginActivity::class.java))
            requireActivity().finishAffinity()
        }

        return binding.root
    }

    private fun putValues(it: User) {
        binding.user = it
        Log.d("TAG",it.photoUrl.toString())
        Glide.with(this).load(it.photoUrl).into(binding.profilePic)
    }

    private fun showPrivacyPolicyDialog(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle("Privacy Policy")
            .setMessage(R.string.privacyPolicy)
            .setNeutralButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}