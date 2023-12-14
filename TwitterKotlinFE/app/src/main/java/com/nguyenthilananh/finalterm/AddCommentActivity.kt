package com.nguyenthilananh.finalterm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyenthilananh.finalterm.Adapter.CommentAdapter
import com.nguyenthilananh.finalterm.Model.Comment
import com.nguyenthilananh.finalterm.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

//import binding
import com.nguyenthilananh.finalterm.databinding.ActivityAddCommentBinding

class AddCommentActivity : AppCompatActivity() {

    private var firebaseUser: FirebaseUser?=null
    private var commentAdapter:CommentAdapter?=null
    private var commentList:MutableList<Comment>?=null
    private lateinit var binding: ActivityAddCommentBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>( R.id.comments_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Comments"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })



        var recyclerView:RecyclerView?=null
        recyclerView=findViewById(R.id.recyclerview_comments)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager=linearLayoutManager

        commentList=ArrayList()
        commentAdapter= this.let { CommentAdapter(it,commentList as ArrayList<Comment>) }
        recyclerView.adapter=commentAdapter


        firebaseUser= FirebaseAuth.getInstance().currentUser

         val add_comment=findViewById<EditText>(R.id.add_comment)
         val post_comment=findViewById<TextView>(R.id.post_comment)
         val postid = intent.getStringExtra("POST_ID")

        getImage()
        readComments(postid!!)
        getPostImage(postid!!)


        post_comment.setOnClickListener {
            if(add_comment.text.toString().equals(""))
            {
              Toast.makeText(this,"You can't send an empty comment",Toast.LENGTH_SHORT).show()
            }
            else
            {
                postComment(postid!!)
            }
        }

    }

    private fun postComment(postid:String) {

        val commentRef : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Comment").child(postid)

        val commentMap = HashMap<String, Any>()
        commentMap["publisher"] = firebaseUser!!.uid
        commentMap["comment"] = binding.addComment.text.toString()

        commentRef.push().setValue(commentMap)
        pushNotification(postid)
        binding.addComment.setText("")
        Toast.makeText(this, "posted!!", Toast.LENGTH_LONG).show()
        
    }

    private fun getImage() {
        val ref : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)
val image = user!!.getImage()
                    if (image != null) {
                        print(image)
                      //  binding.userProfileImage.setImageURI(image.toUri())
                        Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(binding.userProfileImage)
                    }
                    else{
                        binding.userProfileImage.setImageResource(R.drawable.profile)
                    }

//                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(binding.userProfileImage)
                }
            }
        })
    }

    private fun pushNotification(postid: String) {
        // if the user comments on his own post, don't send notification

        val userRef = FirebaseDatabase.getInstance().reference
            .child("Posts").child(postid).child("publisher").get()
        if (userRef.equals(firebaseUser!!.uid)) {
            val ref = FirebaseDatabase.getInstance().reference.child("Notification")
                .child(firebaseUser!!.uid)

            val notifyMap = HashMap<String, Any>()
            notifyMap["userid"] = FirebaseAuth.getInstance().currentUser!!.uid
            notifyMap["text"] = "commented :" + binding.addComment.text.toString()
            notifyMap["postid"] = postid
            notifyMap["ispost"] = true


            ref.push().setValue(notifyMap)
        }
        else {
            return


        }
    }

    private fun readComments(postid: String) {
        val ref: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Comment").child(postid)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                    commentList?.clear()
                    for (snapshot in p0.children) {
                        val cmnt: Comment? = snapshot.getValue(Comment::class.java)
                        commentList!!.add(cmnt!!)
                    }
                        commentAdapter!!.notifyDataSetChanged()
                    }
        })
    }

    private fun getPostImage(postid: String){
        val postRef = FirebaseDatabase.getInstance()
            .reference.child("Posts")
            .child(postid).child("postimage")

        postRef.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    val image = p0.value.toString()

                    Picasso.get().load(image).placeholder(R.drawable.profile).into(binding.postImageComment)
                }
            }
        })
    }
}