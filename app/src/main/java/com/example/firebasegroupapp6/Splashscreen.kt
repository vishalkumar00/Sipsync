package com.example.firebasegroupapp6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splashscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        supportActionBar?.hide()
        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            gotoNextActivity()

        }
    }
fun gotoNextActivity(){
    val intent = Intent(this, Login::class.java)
    startActivity(intent)
    finish()
}

}