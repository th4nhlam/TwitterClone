package com.nguyenthilananh.finalterm.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nguyenthilananh.finalterm.Fragments.PostDetailFragment
import com.nguyenthilananh.finalterm.Model.Post
import com.nguyenthilananh.finalterm.R
import com.squareup.picasso.Picasso
//import binding
import com.nguyenthilananh.finalterm.databinding.MypostLayoutBinding

class MyPostAdapter(private val mContext: Context, private  val mPost:List<Post>): RecyclerView.Adapter<MyPostAdapter.ViewHolder>() {
 private lateinit var binding: MypostLayoutBinding
    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var postedImg: ImageView
        init
        {
            postedImg = itemView.findViewById(R.id.my_posted_picture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = MypostLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view= binding.root


        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post=mPost[position]

        Picasso.get().load(post.getPostImage()).into(holder.postedImg)
        holder.postedImg.setOnClickListener {

                val pref=mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit()
                pref.putString("postid",post.getPostId())
                pref.apply()

                (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, PostDetailFragment()).commit()
        }
    }
}