package com.whatsappgrouper.spring.util

import com.whatsappgrouper.spring.model.Message
import com.whatsappgrouper.spring.repository.MessageRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class MessageRouter {

    val logger = KotlinLogging.logger{}

    @Value("#{'\${com.whatsappgrouper.allGroupIds}'.split(\"&&\")}")
    lateinit var listOfGroups: List<List<String>> //List of the independent groupings our bot manages.

    @Autowired
    lateinit var messageRepository: MessageRepository

    fun route(message: Message) = GlobalScope.launch {
        messageRepository.save(message)

        if (message.fromMe) return@launch //don't route our own messages

        val groupTheMessageBelongsTo = listOfGroups.firstOrNull { it.contains(message.chatId) }
                ?: return@launch //Only route messages for one of our supported chats
        val toChats = groupTheMessageBelongsTo.filter { it != message.chatId } //List of all chats in the group minus the one our message belongs to

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