package com.whatsappgrouper.spring.web

import com.test.api.WebhookMessagesResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WebhookReceiver {

    private val logger = KotlinLogging.logger{}

    @PostMapping("/webhook")
    fun receiveWebhookResponse(messages: WebhookMessagesResponse) {
        logger.debug { messages }
    }
}