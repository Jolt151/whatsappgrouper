package com.whatsappgrouper.spring.repository

import com.whatsappgrouper.spring.model.Message
import com.whatsappgrouper.spring.repository.api.WhatsappService
import com.whatsappgrouper.spring.repository.db.MessageDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MessageRepository {

    @Autowired lateinit var messageDatabase: MessageDatabase
    @Autowired lateinit var whatsappService: WhatsappService

    fun save(message: Message) {
        messageDatabase.save(message)
    }

    suspend fun sendMessage(chatId: String, body: String) {
        val sentMessageResponse = whatsappService.sendMessageToChatId(chatId, body)

        TODO("retrieve message from server and save it")
    }

}