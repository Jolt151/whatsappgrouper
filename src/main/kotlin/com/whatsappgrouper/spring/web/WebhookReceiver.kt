package com.whatsappgrouper.spring.web

import com.whatsappgrouper.spring.repository.MessageRepository
import com.whatsappgrouper.spring.repository.api.model.WebhookMessagesResponse
import com.whatsappgrouper.spring.util.MessageRouter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WebhookReceiver {

    private val logger = KotlinLogging.logger{}

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Autowired
    private lateinit var messageRouter: MessageRouter

    @PostMapping("/webhook")
    fun receiveWebhookResponse(@RequestBody response: WebhookMessagesResponse) {
        logger.info { response }

        GlobalScope.launch {
            messageRepository.saveAll(response.messages)
            messageRouter.routeAll(response.messages)
        }
    }
}