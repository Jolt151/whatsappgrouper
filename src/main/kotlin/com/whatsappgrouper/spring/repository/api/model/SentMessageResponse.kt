package com.whatsappgrouper.spring.repository.api.model

data class SentMessageResponse(
    val sent: Boolean,
    val message: String,
    val id: String
)