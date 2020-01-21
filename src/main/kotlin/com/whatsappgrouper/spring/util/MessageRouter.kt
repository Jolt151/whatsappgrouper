package com.whatsappgrouper.spring.util

import com.whatsappgrouper.spring.model.Message
import com.whatsappgrouper.spring.repository.MessageRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

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

        if (message.type != "chat") { //Not a chat message. Instead of downloading and sending the file, PTT, or whatever, just forward the message

            val firstMessageBody = StringBuilder("""
                wa.me/${message.author.toPhoneNumber()}
                ~${message.senderName}
                ${message.type.messageTypeToReadable()}
            """.trimIndent())

            message.caption?.let { caption ->
                firstMessageBody.append("""
                    
                    Caption:
                    $caption
                """.trimIndent())
            } ?: firstMessageBody.append(": ")

            toChats.forEach { chatId ->
                messageRepository.sendMessage(chatId, firstMessageBody.toString())

                when (message.type) {
                    //"document" -> messageRepository.sendFile(chatId, message.body, UUID.randomUUID().toString(), message.caption)
                    "ptt" -> messageRepository.sendPTT(chatId, message.body)
                    else -> messageRepository.forwardMessage(chatId, message.id)
                }
            }
        } else {
            //send chat message
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
    }

    fun routeAll(messages: List<Message>) {
        messages.forEach { route(it) }
    }
}