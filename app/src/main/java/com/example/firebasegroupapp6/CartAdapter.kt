package com.example.firebasegroupapp6

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.zip.Inflater
import kotlin.time.times

class CartAdapter (options:FirebaseRecyclerOptions<CartDataClass>,private val totalPrice: TextView ):
            FirebaseRecyclerAdapter<CartDataClass,CartAdapter.MyViewHolder>(options){

             var Total:Double=0.0

            class MyViewHolder(inflater: LayoutInflater,parent: ViewGroup):
            RecyclerView.ViewHolder(inflater.inflate(R.layout.cart_row_layout,parent,false)){
                val cartImage:ImageView  =itemView.findViewById(R.id.cartPimage)
                val CartPName :TextView = itemView.findViewById(R.id.cart_Pname)
                val CartPprice :TextView = itemView.findViewById(R.id.cart_Pprice)
                val CartQty : TextView = itemView.findViewById(R.id.Cart_P_qty)
                val removeItemFromCart :Button = itemView.findViewById(R.id.cart_remove_btn)




            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: CartDataClass) {
        holder.CartPName.text=model.cart_Pname
        holder.CartPprice.text=model.cart_Pprice.toString()
        holder.CartQty.text=model.cart_P_qty.toString()
        val name = model.cart_Pname
        Log.d("name", "$name")
        val imageUrl : String? = model.cart_Pimage
        Log.d("imageUrl", "onCreate: $imageUrl")
        val storageReference = imageUrl?.let { FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl) }
        Glide.with(holder.cartImage.context)
            .load(storageReference)
            .into(holder.cartImage)


        GetTotalPrice(model)



        holder.removeItemFromCart.setOnClickListener {

             val ProductId = model.pid
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val productIdToRemove = "Product$ProductId"
            val productRef = FirebaseDatabase.getInstance().reference.child("Cart/$uid").child(productIdToRemove)
            productRef.removeValue()
                .addOnSuccessListener {
                    GetTotalPriceAfterItemRemove( model)
                }
                .addOnFailureListener { e ->
                    Log.e("RemoveItem", "Error removing product: ${e.message}")
                }
        }





    }
    fun GetTotalPrice( model: CartDataClass){

        val ItemPrice = model.cart_Pprice
        val ItemQty = model.cart_P_qty
         Total =( ItemPrice * ItemQty!!)+Total
        val formattedValue = String.format("%.2f", Total)
        totalPrice.text=formattedValue.toString()
        Log.d("Total", "GetTotalPrice: $Total")


    }

    fun GetTotalPriceAfterItemRemove( model: CartDataClass){

        val ItemPrice = model.cart_Pprice
        val ItemQty = model.cart_P_qty
        Total = Total-( ItemPrice * ItemQty!!)
        val formattedValue = String.format("%.2f", Total)
        totalPrice.text=formattedValue.toString()
        Log.d("Total", "GetTotalPrice: $Total")

    }

}