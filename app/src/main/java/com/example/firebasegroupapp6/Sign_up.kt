package com.example.firebasegroupapp6

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Sign_up : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        val btnSignUp: TextView = findViewById(R.id.btnSignUp)
        val btnSignUpBack: TextView = findViewById(R.id.btnSignUpBack)
        btnSignUpBack.setOnClickListener {
            startActivity(Intent(baseContext,Login::class.java))
            finish()

        }

        btnSignUp.setOnClickListener {
            val signUp_email: EditText = findViewById(R.id.signUp_email)
            val signUp_pwd: EditText = findViewById(R.id.signUp_pwd)
            val email = signUp_email.text.toString()
            val password = signUp_pwd.text.toString()
            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()


            if (email.isEmpty()){

                signUp_email.error = "Email should not be empty"
            }
            else if(!email.matches(emailPattern)){
                signUp_email.error = "Email should be a valid email"

            }

            else if(password.isEmpty()){
                signUp_pwd.error = "Password cannot be empty"

            }
            else if(password.length<6){
                signUp_pwd.error = "Password length greater then or equal to 6"

            }
            else{
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "Account Created",
                                Toast.LENGTH_SHORT,
                            ).show()
                            startActivity(Intent(baseContext,Product::class.java))
                            finish()

                        } else {

                            Toast.makeText(
                                baseContext,
                                "Authentication failed.${task.exception?.message}",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }
            }




        }


    }

}