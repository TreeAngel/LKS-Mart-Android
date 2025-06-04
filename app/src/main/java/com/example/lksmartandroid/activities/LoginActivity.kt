package com.example.lksmartandroid.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.lksmartandroid.R
import com.example.lksmartandroid.databinding.ActivityLoginBinding
import com.example.lksmartandroid.models.request.LoginRequest
import com.example.lksmartandroid.services.ClientService
import com.example.lksmartandroid.services.UserSessionHelper
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
            }
        with(binding) {
            btnLogin.setOnClickListener {
                val username = inputUsername.text.toString().trim()
                val password = inputPassword.text.toString().trim()
                if (username.isEmpty()) {
                    inputUsername.error = "Username is needed!"
                    inputUsername.requestFocus()
                    return@setOnClickListener
                }
                if (password.isEmpty()) {
                    inputPassword.error = "Password is needed!"
                    inputPassword.requestFocus()
                    return@setOnClickListener
                }
                login(LoginRequest(username, password))
            }
            tvRegister.setOnClickListener {
                launcher.launch(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
        val session = UserSessionHelper.checkSession(this@LoginActivity)
        if (session) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }

    private fun login(request: LoginRequest) {
        try {
            binding.btnLogin.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                with(ClientService) {
                    val response = service.login(request)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            token = "Bearer $it"
                            if (binding.cbKeepSession.isChecked) {
                                UserSessionHelper.keepSession(this@LoginActivity)
                            }
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Sign in failed\n${response.body()}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        Log.d(
                            "LKS MART API ERROR",
                            "login: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        } finally {
            binding.btnLogin.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }
}