package com.nguyenthilananh.finalterm.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyenthilananh.finalterm.Adapter.PostAdapter
//import com.nguyenthilananh.finalterm.Adapter.StoryAdapter
import com.nguyenthilananh.finalterm.Model.Post
import com.nguyenthilananh.finalterm.Model.Story
import com.nguyenthilananh.finalterm.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var postAdapter:PostAdapter?=null
    private var postList:MutableList<Post>?=null
    private var followingList:MutableList<String>?=null
    private var isDarkMode: Boolean = false

    private lateinit var switchDarkMode: Switch


    private var storyList: MutableList<Story> ?= null



    @SuppressLint("SuspiciousIndentation")
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


         val view= inflater.inflate(R.layout.fragment_home, container, false)

         // Inflate the layout for this fragment



        switchDarkMode = view.findViewById(R.id.switch_dark_mode)
            switchDarkMode.setOnClickListener(View.OnClickListener {


                if (isDarkMode == true) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                }
            }
            )

         // Get the SharedPreferences.Editor object


//         sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
//
//         // Get the switch view from the layout
//         switchDarkMode = view.findViewById(R.id.switch_dark_mode)
//
//         // Set a checked change listener on the switch
//         switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
//             // Toggle the dark mode preference
//             sharedPreferences.edit().putBoolean("darkMode", isChecked).apply()
//
//             // Apply the dark mode theme
//             applyTheme(isChecked)
//         }
        var recyclerView:RecyclerView?=null
         var recyclerViewStory:RecyclerView?=null

         recyclerView=view.findViewById(R.id.recycler_view_home)
         val linearlayoutManager=LinearLayoutManager(context)
         linearlayoutManager.reverseLayout=true
         //New posts at top
         linearlayoutManager.stackFromEnd=true
         recyclerView.layoutManager=linearlayoutManager
         //For Posts
         postList=ArrayList()
         postAdapter=context?.let { PostAdapter(it,postList as ArrayList<Post>) }
         recyclerView.adapter=postAdapter


         recyclerViewStory=view.findViewById(R.id.recycler_view_story)
         recyclerViewStory.setHasFixedSize(true)
         val linearlayoutManager2=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
         recyclerViewStory.layoutManager=linearlayoutManager2
         ///For Stories
         storyList=ArrayList()


         checkFollowings()

        return view
    }

    //to get the following List of logged-in user
    private fun checkFollowings() {
        followingList=ArrayList()

        val followingRef = FirebaseDatabase.getInstance().reference
                .child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("Following")

        followingRef.addValueEventListener(object :ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    (followingList as ArrayList<String>).clear() //to get previous data
                    for(snapshot in p0.children)
                    {
                        snapshot.key?.let { (followingList as ArrayList<String>).add(it) }
                    }
                    retrievePosts()
                    retrieveStories()
                }
            }
        })
    }

    private fun retrievePosts() {
        val postRef=FirebaseDatabase.getInstance().reference.child("Posts")

         postRef.addValueEventListener(object : ValueEventListener
         {
             override fun onCancelled(error: DatabaseError) {

             }

             override fun onDataChange(p0: DataSnapshot)
             {
                 if(p0.exists()) {
                     postList?.clear()
                     for (snapshot in p0.children) {
                         val post = snapshot.getValue(Post::class.java)

                         for (id in (followingList as ArrayList<String>)) {
                             if (post!!.getPublisher() == id) {
                                 postList!!.add(post)
                             }
                             postAdapter!!.notifyDataSetChanged()
                         }
                     }
                 }
             }

         })
    }

    private fun retrieveStories()
    {
        val storyRef= FirebaseDatabase.getInstance().reference.child("Story")

        storyRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                val timeCurrent=System.currentTimeMillis()

                (storyList as ArrayList<Story>).clear()

                (storyList as ArrayList<Story>).add(Story("",0,0,"",FirebaseAuth.getInstance().currentUser!!.uid))

                for (id in followingList!!)
                {
                    var countStory=0

                    var story:Story?=null

                    for (snapshot in datasnapshot.child(id).children)
                    {
                        story= snapshot.getValue(Story::class.java)

                        if(timeCurrent>story!!.getTimeStart() && timeCurrent<story!!.getTimeEnd())
                        {
                            countStory++
                        }
                    }
                    if (countStory>0){
                        (storyList as ArrayList<Story>).add(story!!)
                    }
                }
//                storyAdapter!!.notifyDataSetChanged()


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}
