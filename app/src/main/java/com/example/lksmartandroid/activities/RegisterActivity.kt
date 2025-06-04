package com.example.lksmartandroid.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.lksmartandroid.R
import com.example.lksmartandroid.databinding.ActivityRegisterBinding
import com.example.lksmartandroid.models.request.RegisterRequest
import com.example.lksmartandroid.services.ClientService
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        with(binding) {
            btnRegister.setOnClickListener {
                val fullName = inputFullName.text.toString().trim()
                val address = inputAddress.text.toString().trim()
                val phone = inputPhone.text.toString().trim()
                val username = inputUsername.text.toString().trim()
                val password = inputPassword.text.toString().trim()
                val confirm = inputConfirmPassword.text.toString().trim()
                if (fullName.isEmpty()) {
                    inputFullName.error = "Full name is required!"
                    inputFullName.requestFocus()
                    return@setOnClickListener
                }
                if (address.isEmpty()) {
                    inputAddress.error = "Address is required!"
                    inputAddress.requestFocus()
                    return@setOnClickListener
                }
                if (phone.isEmpty()) {
                    inputPhone.error = "Phone number is required!"
                    inputPhone.requestFocus()
                    return@setOnClickListener
                }
                if (username.isEmpty()) {
                    inputUsername.error = "Username is required!"
                    inputUsername.requestFocus()
                    return@setOnClickListener
                }
                if (password.isEmpty()) {
                    inputPassword.error = "Password is required!"
                    inputPassword.requestFocus()
                    return@setOnClickListener
                }
                if (confirm.isEmpty() || confirm != password) {
                    inputConfirmPassword.error = "Confirm your password!"
                    inputConfirmPassword.requestFocus()
                    return@setOnClickListener
                }
                register(RegisterRequest(fullName, username, address, phone, password))
            }
            tvLogin.setOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this@RegisterActivity) {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun register(request: RegisterRequest) {
        try {
            binding.btnRegister.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                with(ClientService) {
                    val response = service.register(request)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            token = "Bearer $it"
                            setResult(RESULT_OK)
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Register Failed\n${response.body()}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        Log.d(
                            "LKS MART API ERROR",
                            "register: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        } finally {
            binding.btnRegister.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }
}