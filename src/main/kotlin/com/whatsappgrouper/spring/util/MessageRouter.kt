package com.whatsappgrouper.spring.util

import com.whatsappgrouper.spring.model.Message
import com.whatsappgrouper.spring.repository.MessageRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
class MessageRouter {
    @Value("\${com.whatsappgrouper.allGroupIds}")
    lateinit var groupIds: List<String>

    @Autowired
    lateinit var messageRepository: MessageRepository

    fun route(message: Message) = GlobalScope.launch {
        messageRepository.save(message)

        if (message.fromMe) return@launch //don't route our own messages
        if (!groupIds.contains(message.chatId)) return@launch //Only route messages meant for our chats

        val toChats = groupIds.filter { it != message.chatId } //List of all chats minus the one our message belongs to

        val forwardingMessage = """
                                wa.me/${message.author.toPhoneNumber()}
                                ~${message.senderName}
                                
                                ${message.body}
                            """.trimIndent()

        toChats.forEach { chatId ->
            GlobalScope.launch {
                messageRepository.sendMessage(chatId, forwardingMessage)
            }
        }
    }

    fun routeAll(messages: List<Message>) {
        messages.forEach { route(it) }
    }
}