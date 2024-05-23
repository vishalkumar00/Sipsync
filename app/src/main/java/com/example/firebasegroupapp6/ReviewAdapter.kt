package com.example.firebasegroupapp6

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage

class ReviewAdapter(options: FirebaseRecyclerOptions<ReviewDataClass>):
                    FirebaseRecyclerAdapter<ReviewDataClass,ReviewAdapter.MyViewHolder>(options){
    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.review_row_layout,parent,false)){
        val Pname: TextView = itemView.findViewById(R.id.Review_pName)
        val Pcomment: TextView = itemView.findViewById(R.id.Review_pComment)
        val CommentDate: TextView = itemView.findViewById(R.id.Review_CommentDate)
        val ReviewImage:ImageView = itemView.findViewById(R.id.ReviewPimage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: ReviewDataClass) {
        holder.Pname.text=model.Name
      holder.Pcomment.text=model.Comment
      holder.CommentDate.text=model.CommentDate
        val ImageUrl:String = model.ImageUrl
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(ImageUrl)
        Glide.with(holder.ReviewImage.context)
            .load(storageReference)
            .into(holder.ReviewImage)
    }

}

