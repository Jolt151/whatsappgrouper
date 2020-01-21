package com.whatsappgrouper.spring.repository

import com.whatsappgrouper.spring.model.Message
import com.whatsappgrouper.spring.repository.api.WhatsappService
import com.whatsappgrouper.spring.repository.db.MessageDatabase
import kotlinx.coroutines.delay
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import kotlin.math.log

@Component
class MessageRepository {

    private val logger = KotlinLogging.logger{}

    @Autowired lateinit var messageDatabase: MessageDatabase
    @Autowired lateinit var whatsappService: WhatsappService

    fun save(message: Message) {
        messageDatabase.save(message)
    }

    suspend fun sendMessage(chatId: String, body: String) {
        val sentMessageResponse = whatsappService.sendMessageToChatId(chatId, body)

        val lastMessage = messageDatabase.getLatestMessageNumber()
        delay(1000) //necessary so we get the sent message back from the api
        val messagesResponse = whatsappService.getMessages(lastMessageNumber = lastMessage.messageNumber)
        logger.info { messagesResponse }
        messageDatabase.saveAll(messagesResponse.messages)
    }

}