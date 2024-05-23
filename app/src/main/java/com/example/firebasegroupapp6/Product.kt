package com.example.firebasegroupapp6

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Product : NavigationDrawer()  {
     private lateinit var toggle :ActionBarDrawerToggle
     private var adapter :ProductAdapter ?= null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.navView)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

       supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Sip Sync"

       navView.setNavigationItemSelectedListener{menuItem ->
           when(menuItem.itemId){
               R.id.home->{
                   Toast.makeText(applicationContext,"Home",Toast.LENGTH_LONG).show()
                   startActivity(Intent(baseContext,Product::class.java))
                   finish()
               }

              R.id.logout ->{
                  Toast.makeText(applicationContext,"Logout",Toast.LENGTH_LONG).show()
                  FirebaseAuth.getInstance().signOut()
                  startActivity(Intent(baseContext,Login::class.java))
                  finish()

                }
               R.id.cart ->{
                   Toast.makeText(applicationContext,"Cart",Toast.LENGTH_LONG).show()
                   startActivity(Intent(baseContext,Cart::class.java))


               }
               R.id.review ->{
                   Toast.makeText(applicationContext,"Review",Toast.LENGTH_LONG).show()
                   startActivity(Intent(baseContext,Review_Drinks::class.java))


               }
           }
           true
       }


        //code for the adapter start from here
        val query = FirebaseDatabase.getInstance().reference.child("Inventory")
        val options =  FirebaseRecyclerOptions.Builder<ProductDataClass>()
            .setQuery(query,ProductDataClass::class.java).build()
        adapter = ProductAdapter(options)
        val product_RView :RecyclerView = findViewById(R.id.product_RView)

        if (isInLandscapeMode()) {
            product_RView.layoutManager = GridLayoutManager(this, 4)
            product_RView.adapter=adapter
        } else {
            product_RView.layoutManager = GridLayoutManager(this, 2)
            product_RView.adapter=adapter
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
    private fun isInLandscapeMode(): Boolean {
        val orientation = resources.configuration.orientation
        return orientation == Configuration.ORIENTATION_LANDSCAPE
    }
}