package com.example.firebasegroupapp6

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import java.util.zip.Inflater
import kotlin.math.log

class ProductAdapter(options: FirebaseRecyclerOptions<ProductDataClass>):
    FirebaseRecyclerAdapter<ProductDataClass,ProductAdapter.MyViewHolder>(options){

        class MyViewHolder(inflater: LayoutInflater,parent:ViewGroup):
                RecyclerView.ViewHolder(inflater.inflate(R.layout.product_row_layout,parent,false)){
                    val Pname:TextView= itemView.findViewById(R.id.Pname)
                    val Pprice:TextView= itemView.findViewById(R.id.Pprice)
                    val Pdescription:TextView= itemView.findViewById(R.id.Pdescription)
                    val Pimage:ImageView= itemView.findViewById(R.id.Pimage)
                }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater,parent)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: ProductDataClass) {
        holder.Pname.text=model.Pname
        holder.Pprice.text= model.Pprice.toString()
        holder.Pdescription.text=model.Pdescription
        val ImageUrl: String = model.Pimage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(ImageUrl)
        Glide.with(holder.Pimage.context)
            .load(storageReference)
            .into(holder.Pimage)

holder.itemView.setOnClickListener {
    val productDetail_intent = Intent(it.context,Product_Detail::class.java)
    val product_id:Int = model.Pid
    Log.d("Before passing p Id", "onBindViewHolder: $product_id")
         productDetail_intent.putExtra("Pid",product_id)
         productDetail_intent.putExtra("Pprice",model.Pprice)
         productDetail_intent.putExtra("Pname",model.Pname)
         productDetail_intent.putExtra("Pdescription",model.Pdescription)
         productDetail_intent.putExtra("Pimage",model.Pimage)
    it.context.startActivity(productDetail_intent)
}


    }
}