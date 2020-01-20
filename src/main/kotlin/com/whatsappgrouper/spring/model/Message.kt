package com.test.model


data class Message(
    val id: String,
    val body: String,
    val type: String,
    val senderName: String,
    val fromMe: Boolean,
    val author: String,
    val time: Long,
    val chatId: String,
    val messageNumber: Int
)