package com.example.firebasegroupapp6

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Dumpable
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Cart : AppCompatActivity() {
    private var adapter :CartAdapter ?=null
    private lateinit var toggle : ActionBarDrawerToggle
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        //menu code here
        val drawerLayout : DrawerLayout = findViewById(R.id.cartDrawerLayout)
        val navView : NavigationView = findViewById(R.id.navView)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Sip Sync"
        navView.setNavigationItemSelectedListener{menuItem ->
            when(menuItem.itemId){
                R.id.home->{
                    Toast.makeText(applicationContext,"Home", Toast.LENGTH_LONG).show()
                    startActivity(Intent(baseContext,Product::class.java))
                    finish()
                }

                R.id.logout ->{
                    Toast.makeText(applicationContext,"Logout", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(baseContext,Login::class.java))
                    finish()

                }
                R.id.cart ->{
                    Toast.makeText(applicationContext,"Cart", Toast.LENGTH_LONG).show()
                    startActivity(Intent(baseContext,Cart::class.java))


                }
                R.id.review ->{
                    Toast.makeText(applicationContext,"Review", Toast.LENGTH_LONG).show()
                    startActivity(Intent(baseContext,Review_Drinks::class.java))


                }
            }
            true
        }


     //adapter code here
        val user = FirebaseAuth.getInstance().currentUser
        val uid : String? = user?.uid
        val query = FirebaseDatabase.getInstance().reference.child("Cart/$uid")
        val totalPrice:TextView = findViewById(R.id.totalPrice)
        Log.d("query", "onCreate: $query")
        val options = FirebaseRecyclerOptions.Builder<CartDataClass>()
            .setQuery(query,CartDataClass::class.java)
            .build()
        Log.d("options", "onCreate: $options")
        adapter= CartAdapter(options,totalPrice)

        val cart_Rview :RecyclerView = findViewById(R.id.cart_rView)
        cart_Rview.layoutManager = LinearLayoutManager(this)
        cart_Rview.adapter=adapter



        val cart_back: Button = findViewById(R.id.cart_back)
        val cart_Checkout :Button = findViewById(R.id.cart_checkout)
        cart_back.setOnClickListener {
            startActivity(Intent(this,Product::class.java))
            finish()
        }



        cart_Checkout.setOnClickListener {
            val totalPrice:TextView = findViewById(R.id.totalPrice)
            val totalPriceValue :String? = totalPrice.text.toString()
            Log.d("Cart TotalPrice", "Total Price: $totalPriceValue")
            val i = Intent(this,Checkout::class.java)
            i.putExtra("totalPrice",totalPriceValue)
            startActivity(i)
            finish()
        }


    }
    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}