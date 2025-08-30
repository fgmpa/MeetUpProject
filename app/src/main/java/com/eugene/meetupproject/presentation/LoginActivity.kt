package com.eugene.meetupproject.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.eugene.meetupproject.R
import com.eugene.meetupproject.di.MyApp
import com.eugene.meetupproject.presentation.viewmodel.AuthViewModel
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupObservers()
        setupUi()
    }

    private fun setupUi() {
        val loginEditText = findViewById<EditText>(R.id.login_edit)
        val passwordEditText = findViewById<EditText>(R.id.password_edit)
        val loginButton = findViewById<Button>(R.id.log_but)

        loginButton.setOnClickListener {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.signIn(login, password)
        }
    }

    private fun setupObservers() {
        viewModel.userState.observe(this) { user ->
            if (user != null) {
                startActivity(Intent(this, EventListActivity::class.java))
                finish()
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}