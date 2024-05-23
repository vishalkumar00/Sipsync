package com.example.firebasegroupapp6

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class Product_Detail : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var auth:FirebaseAuth
    private lateinit var toggle : ActionBarDrawerToggle
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
    //menu code here
        val drawerLayout : DrawerLayout = findViewById(R.id.productDetail_DrawerLayout)
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






        //firebase code here
        auth = FirebaseAuth.getInstance()
        LoadUi()


        val addtoCartBtn : Button = findViewById(R.id.addToCartBtn)
        addtoCartBtn.setOnClickListener {
            val cart_Pqty :EditText= findViewById(R.id.cart_Pqty)
            val qty: Int? = cart_Pqty.text.toString().toIntOrNull()
            if(qty==null){
                Toast.makeText(this,"please enter the Quantity",Toast.LENGTH_LONG).show()
            }
            else if (qty<=0 || qty>6){
                Toast.makeText(this,"Quantity Range 1 to 6",Toast.LENGTH_LONG).show()
            }
            else{
//
                val intentData = intent

                val Iid = intentData.getIntExtra("Pid",1)
                val Iprice = intentData.getDoubleExtra("Pprice",0.0) // Default value is set to 0
                val Iname = intentData.getStringExtra("Pname")

                val Iimage = intentData.getStringExtra("Pimage")
                val user = FirebaseAuth.getInstance().currentUser
                val uid : String? = user?.uid
                val intPId = Iid?.toInt()
                Log.d("Product id", "$intPId")

                val myCart = CartDataClass(intPId,Iname,Iprice,qty,Iimage)

                val cartRef = FirebaseDatabase.getInstance().reference.child("Cart/$uid").child("Product$intPId")
                cartRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(baseContext, "Already Added To The Cart", Toast.LENGTH_LONG).show()
                        }
                        else{
                            FirebaseDatabase.getInstance().reference.child("Cart/$uid").child("Product$intPId").setValue(myCart)
                                .addOnCompleteListener{
                                    Toast.makeText(baseContext, "Added to the cart", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(baseContext,Product::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    // On failure
                                    Toast.makeText(baseContext, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(baseContext,Product::class.java))
                                    finish()
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })


               }
            }
        }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    fun  LoadUi(){

        val intentData = intent

        val Iid = intentData.getStringExtra("Pid")
        val Iprice = intentData.getDoubleExtra("Pprice",0.0) // Default value is set to 0
        val Iname = intentData.getStringExtra("Pname")
        val Idescription = intentData.getStringExtra("Pdescription")
        val Iimage = intentData.getStringExtra("Pimage")

        val Pprice :TextView= findViewById(R.id.cart_Pprice)
        val Pname :TextView= findViewById(R.id.cart_Pname)
        val Pdescription:TextView= findViewById(R.id.cart_Pdescription)
        val Pimage :ImageView= findViewById(R.id.cart_Pimage)
        Pprice.text=Iprice.toString()
        Pname.text=Iname
        Pdescription.text=Idescription

        val storageReference = Iimage?.let { FirebaseStorage.getInstance().getReferenceFromUrl(it) }
        Glide.with(Pimage.context)
            .load(storageReference)
            .into(Pimage)
    }


}