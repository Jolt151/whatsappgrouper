package com.test.api

import com.test.model.Message

data class WebhookMessagesResponse(
    val instanceId: String,
    val messages: List<Message>
)