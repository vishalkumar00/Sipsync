package com.example.firebasegroupapp6

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class Review_Drinks : AppCompatActivity() {
    private  lateinit var auth: FirebaseAuth
    private lateinit var toggle : ActionBarDrawerToggle
    val uniquePhotoId = UUID.randomUUID().toString()
    private var adapter :ReviewAdapter ?= null
    var imageUpload:Int = 0
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_drinks)
        auth = FirebaseAuth.getInstance()
        val Review_RView :RecyclerView = findViewById(R.id.Review_RView)
        var uid = auth.uid



 //menu here
        val drawerLayout : DrawerLayout = findViewById(R.id.ReviewdrawerLayout)
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


        //adapter code here
        val query = FirebaseDatabase.getInstance().reference.child("Review")
        val options =  FirebaseRecyclerOptions.Builder<ReviewDataClass>()
            .setQuery(query,ReviewDataClass::class.java).build()
        adapter = ReviewAdapter(options)
        Review_RView.layoutManager = LinearLayoutManager(this)
        Review_RView.adapter=adapter



// adding data to database here
        val Upload_Review_Btn : Button = findViewById(R.id.uploadReview)
        val AddPhoto_Btn : Button = findViewById(R.id.addPhoto)
        AddPhoto_Btn.setOnClickListener {
            loadCamera()
        }
        Upload_Review_Btn.setOnClickListener {
            insertPhotoIntoDB(uniquePhotoId)
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
    private  fun loadCamera(){
        val Add_Photo_Btn : Button = findViewById(R.id.addPhoto)
        Add_Photo_Btn.setOnClickListener {
         val clickPictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
         camerLauncher.launch(clickPictureIntent)
        }

    }
    private  var camerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){

            val photo = result.data?.extras?.get("data") as Bitmap
            addPhoto(photo,uniquePhotoId)


        }
        else{
            Toast.makeText(baseContext,"please add image ",Toast.LENGTH_LONG).show()
        }
    }
    private  fun addPhoto(bitmap:Bitmap,id:String){
        val RefStorage = FirebaseStorage.getInstance().getReference(id)
        val BAOS = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,BAOS)
        val data = BAOS.toByteArray()
        val uploadTask = RefStorage.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(this,"Error ${it.message}",Toast.LENGTH_LONG).show()
        }.addOnSuccessListener {
            Toast.makeText(this,"Image Upload Successfully",Toast.LENGTH_LONG).show()
            imageUpload=1
        }

    }

private  fun insertPhotoIntoDB(id:String){

    var uid = auth.uid.toString()
//    val storage = FirebaseStorage.getInstance()
//    val storageRef: StorageReference = storage.reference
    val Review_Name :EditText = findViewById(R.id.Review_Name)
    val Review_Comment : EditText = findViewById(R.id.Review_comment)
    val Review_Name_Value = Review_Name.text.toString()
    val Review_Comment_Value = Review_Comment.text.toString()

    if(Review_Name_Value.isEmpty()){
        Review_Name.error = "Please Enter Name"
    }
    else if(Review_Comment_Value.isEmpty()){
        Review_Comment.error = "Please Enter Comment"
    }
    else if(imageUpload ==0 ){
        Toast.makeText(this,"Add image first",Toast.LENGTH_LONG).show()
    }
    else{

        val url = FirebaseStorage.getInstance().reference.toString()+id
        val CurrentDate = LocalDate.now()
        val formattedDate = CurrentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val MyReview = ReviewDataClass(uid,url,Review_Name_Value,Review_Comment_Value,formattedDate)
        val currentuser  = auth.currentUser

        FirebaseDatabase.getInstance().reference.child("Review").push().setValue(MyReview)
            .addOnCompleteListener{
                Toast.makeText(baseContext, "Review Added", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                // On failure
                Toast.makeText(baseContext, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }







}
}








