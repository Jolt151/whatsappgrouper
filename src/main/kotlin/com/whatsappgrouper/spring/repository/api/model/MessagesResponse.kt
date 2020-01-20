package com.test.api

import com.test.model.Message

data class MessagesResponse(
    val messages: List<Message>,
    val lastMessageNumber: Int
)