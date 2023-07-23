package com.example.ace.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ace.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ace.databinding.ActivityDiscussionForumTopicsBinding
import com.example.ace.data.model.DiscussionForumTopic


class DiscussionForumTopics : AppCompatActivity() {
    private lateinit var binding: ActivityDiscussionForumTopicsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDiscussionForumTopicsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // -- Create a list of discussion thread topics
        val topics : MutableList<DiscussionForumTopic> = mutableListOf()
        val topic1 = DiscussionForumTopic(topicName = "ECE 452: Question about exam?")
        val topic2 = DiscussionForumTopic(topicName = "CLAS 104: Textbook for course?")
        val topic3 = DiscussionForumTopic(topicName = "Campus Events: When is semi?")
        topics.add(topic1)
        topics.add(topic2)
        topics.add(topic3)

        val recyclerview : RecyclerView = findViewById<RecyclerView>(R.id.recycler_discussion_forum_topics)
        recyclerview.layoutManager= LinearLayoutManager(this)
        val adapter = DiscussionForumTopicsAdapter(topics){ topic: DiscussionForumTopic, position: Int ->
            val topicName = topic.topicName
            val intent = Intent(this, DiscussionForumChat::class.java)
            intent.putExtra("topic", topicName )
            startActivity(intent)
            finish()
        }
        recyclerview.adapter = adapter
    }
}
