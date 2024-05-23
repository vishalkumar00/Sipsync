package com.example.firebasegroupapp6

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth

@Suppress("UNREACHABLE_CODE")
class Login : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        //2nd api method installSplashScreen()
        installSplashScreen()
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        supportActionBar?.hide()


        val btnLogin: Button = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val login_email: EditText = findViewById(R.id.login_email)
            val login_pwd: EditText = findViewById(R.id.login_pwd)
            val email = login_email.text.toString()
            val password = login_pwd.text.toString()
            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()


            if (email.isEmpty()){

                login_email.error = "Email should not be empty"
            }
            else if(!email.matches(emailPattern)){
                login_email.error = "Email should be a valid email"

            }

            else if(password.isEmpty()){
                login_pwd.error = "Password cannot be empty"

            }
            else if(password.length<6){
                login_pwd.error = "Password length greater then or equal to 6"

            }
            else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "Welcome Back",
                                Toast.LENGTH_SHORT,
                            ).show()
                        startActivity(Intent(baseContext,Product::class.java))
                        finish()}
                        else {

                            Toast.makeText(
                                baseContext,
                                "${task.exception?.message}",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }


                    }
            }


        val txtCreateAccount:TextView = findViewById(R.id.txtCreateAccount)
        txtCreateAccount.setOnClickListener {
            startActivity(Intent(this,Sign_up::class.java))
            finish()
        }


            }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser!==null){
            startActivity(Intent(baseContext,Product::class.java))
            finish()
        }
    }

        }

