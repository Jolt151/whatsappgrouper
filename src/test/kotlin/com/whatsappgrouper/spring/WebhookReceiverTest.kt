package com.whatsappgrouper.spring

import com.google.gson.Gson
import com.whatsappgrouper.spring.repository.api.model.WebhookMessagesResponse
import com.whatsappgrouper.spring.repository.db.MessageDatabase
import com.whatsappgrouper.spring.web.WebhookReceiver
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class WebhookReceiverTest {
    @Autowired
    lateinit var webhookReceiver: WebhookReceiver

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var messageDatabase: MessageDatabase

    @Test
    fun objectIsSerialized() {
        val content = """
            {
              "messages": [
                {
                  "id": "false_17472822486@c.us_DF38E6A25B42CC8CCE57EC40F",
                  "body": "Ok!",
                  "type": "chat",
                  "senderName": "Ilya",
                  "fromMe": true,
                  "author": "17472822486@c.us",
                  "time": 1504208593,
                  "chatId": "17472822486@c.us",
                  "messageNumber": -1
                }
              ]
            }
        """.trimIndent()

        mockMvc.perform { post("/webhook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .buildRequest(it) }
                .andExpect(status().isOk)

        //Cleanup
        val gson = Gson()
        val webhookResponse = gson.fromJson(content, WebhookMessagesResponse::class.java)
        messageDatabase.deleteAll(webhookResponse.messages)
    }
}