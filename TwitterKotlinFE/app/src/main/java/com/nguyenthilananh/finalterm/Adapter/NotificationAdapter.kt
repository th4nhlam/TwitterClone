package com.nguyenthilananh.finalterm.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nguyenthilananh.finalterm.Fragments.PostDetailFragment
import com.nguyenthilananh.finalterm.Fragments.ProfileFragment
import com.nguyenthilananh.finalterm.Model.Notification
import com.nguyenthilananh.finalterm.Model.Post
import com.nguyenthilananh.finalterm.Model.User
import com.nguyenthilananh.finalterm.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class NotificationAdapter(
    private var mContext: Context,
    private var mNotification:List<Notification>)
        : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {


        class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {

            var username: TextView = itemView.findViewById(R.id.notification_username)
            var notifyText: TextView = itemView.findViewById(R.id.notification_text)
            var profileimage: ImageView = itemView.findViewById(R.id.notification_image_profile)
            var postimg: ImageView = itemView.findViewById(R.id.posted_image)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(mContext).inflate(R.layout.notification_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return mNotification.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val notification = mNotification[position]
        holder.notifyText.text=notification.getText()

        publisherInfo(holder.profileimage,holder.username,notification.getUserId())
        if(notification.getIsPost())
        {
            holder.postimg.visibility=View.VISIBLE
            getPostedImg(holder.postimg,notification.getPostId())
        }
        else
        {
            holder.postimg.visibility=View.GONE
        }

        holder.postimg.setOnClickListener {
            if(notification.getIsPost()) {
                val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                pref.putString("postid", notification.getPostId())
                pref.apply()

                (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, PostDetailFragment()).commit()
            }
            else
            {

                val pref=mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit()
                pref.putString("profileid",notification.getUserId())
                pref.apply()

                (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProfileFragment()).commit()
            }
        }
    }

    private fun publisherInfo(imgView: ImageView, username: TextView, publisherid: String) {

        val userRef= FirebaseDatabase.getInstance().reference.child("Users").child(publisherid)
        userRef.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>(User::class.java)

                Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(imgView)
                username.text =(user.getUsername())
            }
        })
    }

    private fun getPostedImg(postimg:ImageView, postid:String?) {

        val postRef= FirebaseDatabase.getInstance().reference.child("Posts").child(postid!!)

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot)
            {
                val post = snapshot.getValue(Post::class.java)
                val postImage = post?.getPostImage()
                if (!postImage.isNullOrEmpty()) {
                    Picasso.get().load(postImage).into(postimg)
                }
                else {
                    postimg.setImageResource(R.drawable.profile)
                }

              //  Picasso.get().load(post!!.getPostImage()).into(postimg)
            }
        })
    }
}
