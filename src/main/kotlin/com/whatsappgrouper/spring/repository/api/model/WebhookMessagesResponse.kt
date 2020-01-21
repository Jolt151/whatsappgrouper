package com.whatsappgrouper.spring.repository.api.model

import com.whatsappgrouper.spring.model.Message

data class WebhookMessagesResponse(
    val instanceId: String?,
    val messages: List<Message>
)