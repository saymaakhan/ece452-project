package com.example.ace.data

import com.example.ace.data.model.ChatMessage
import com.example.ace.data.model.ChatUser
import java.sql.Timestamp

class ChatMessageSource {
    fun getMessages(topic : String, user : String) : MutableList<ChatMessage> {
        // -- Retrieve chat messages based on topic & user
        val messages : MutableList<ChatMessage> = mutableListOf()

        // -- Message 1
        val text1 = "Lool nice"
        val msg1 = getMessage(topic,user,text1)
        messages.add(msg1)

        // -- Message 2
        val text2 = "Hey when you're free, call me!"
        val msg2 = getMessage(topic,user,text2)
        messages.add(msg2)

        return messages
    }

    fun getMessage(topic : String, user : String, text : String) : ChatMessage {
        val sendinguser = ChatUser(userName = user, profileUrl = "")
        val ts = Timestamp(System.currentTimeMillis()).time
<<<<<<< HEAD
        val msg = ChatMessage(sender =sendinguser.toString(), timestamp =ts, message =text)
=======
        val msg = ChatMessage(sender=sendinguser.toString(), timestamp=ts, message=text)
>>>>>>> 0c9dc5c (load users and save sent messages)
        return msg
    }

    fun getResponseMessage(index : Int, user : String)  : ChatMessage {
        if(index == 0) {
            return getMessage("", user, "I got part 1 working btw")
        }
        if(index == 1) {
            return getMessage("", user, "Good since this is due soon")
        }
        if(index == 2) {
            return getMessage("", user, "Do you want to go get food and then continue?")
        }
        return getMessage("", user, "Sorry, I didn't get that ):")
    }
}