package com.example.lksmartandroid.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.example.lksmartandroid.R
import com.example.lksmartandroid.databinding.FragmentProfileBinding
import com.example.lksmartandroid.services.ClientService
import com.example.lksmartandroid.services.UserSessionHelper
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener {
            if (ClientService.token.isNotEmpty()) {
                UserSessionHelper.deleteSession(requireContext())
            } else {
                activity?.finish()
            }
        }
        if (ClientService.token.isNotEmpty()) {
            getProfile()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            if (ClientService.session == null) {
                getProfile()
            }
        }
    }

    private fun setView() {
        with(binding) {
            if (ClientService.session != null) {
                tvFullName.text = ClientService.session!!.nama
                tvPhone.text = ClientService.session!!.telepon
                tvAddress.text = ClientService.session!!.alamat
                ClientService.loadImage(requireContext(), ivProfile, ClientService.session!!.image)
            } else {
                ivProfile.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.person
                    )
                )
                tvFullName.text = "Not signed yet"
                tvPhone.text = "00000"
                tvAddress.text = ""
            }
        }
    }

    private fun getProfile() {
        try {
            lifecycleScope.launch {
                with(ClientService) {
                    val response = service.profile()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            session = it
                            setView()
                        }
                    } else {
                        Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT)
                            .show()
                        Log.d(
                            "LKS MART API ERROR",
                            "getProfile: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }
}