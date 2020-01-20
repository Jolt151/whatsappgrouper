package com.whatsappgrouper.spring.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Message(
        @Id val id: String,
        val body: String,
        val type: String,
        val senderName: String,
        val fromMe: Boolean,
        val author: String,
        val time: Long,
        val chatId: String,
        val messageNumber: Int
)