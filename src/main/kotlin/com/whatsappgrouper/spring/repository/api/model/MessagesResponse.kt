package com.whatsappgrouper.spring.repository.api.model

import com.whatsappgrouper.spring.model.Message

data class MessagesResponse(
        val messages: List<Message>,
        val lastMessageNumber: Int?
)