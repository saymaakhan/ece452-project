package com.example.ace.data

import com.example.ace.data.model.ChatMessage
import com.example.ace.data.model.ChatUser
import java.sql.Timestamp

class DiscussionForumMessageSource
{
    fun getMessages(topic : String) : MutableList<ChatMessage> {
        val messages : MutableList<ChatMessage> = mutableListOf()

        // -- Message 1
        val text1 = "What is covered on the 452 exam?"
        val message1 = getMessage(topic,"John",text1)
        messages.add(message1)

        // -- Message 2
        val text2 = "All lectures for exam are on learn"
        val message2 = getMessage(topic,"Jane",text2)
        messages.add(message2)

        return messages
    }

    fun getMessage(topic : String, user : String, text : String) : ChatMessage {
        val sendingUser = ChatUser(userName = user, profileUrl = "")
        val timeStamp = Timestamp(System.currentTimeMillis()).time
        val message = ChatMessage(topic=topic, sender=user, timestamp=timeStamp, message=text, receiver="")
        return message
    }

    fun getResponseMessage( index : Int, topic : String, user : String )  : ChatMessage {
        if(index == 0) {
            return getMessage( topic, user, "What about from the midterm?" )
        }
        if(index == 1) {
            return getMessage( "", user, "Are lab topics included?" )
        }
        if(index == 2) {
            return getMessage( "", user, "What is the marks distribution?" )
        }
        return getMessage( "", user, "I don't know, we should ask the prof" )
    }
}
