package com.whatsappgrouper.spring.repository.api

import com.google.gson.JsonObject
import com.whatsappgrouper.spring.repository.api.model.DialogsResponse
import com.whatsappgrouper.spring.repository.api.model.MessagesResponse
import com.whatsappgrouper.spring.repository.api.model.SentMessageResponse
import retrofit2.Call
import retrofit2.http.*

interface WhatsappService {
    @GET("status")
    suspend fun getStatus(): JsonObject

    @GET("messages")
    suspend fun getMessages(@Query("lastMessageNumber") lastMessageNumber: Int? = null,
                            @Query("limit") limit: Int = 0): MessagesResponse

    @GET("messages")
    suspend fun getMessagesForChatId(@Query("chatId") chatId: String): MessagesResponse

    @GET("messages")
    suspend fun getAllMessages(@Query("limit") limit: Int = 0): MessagesResponse

    @POST("sendMessage")
    suspend fun sendMessage(
        @Field("phone") phone: String,
        @Field("body") body: String
    ): SentMessageResponse

    @POST("sendMessage")
    @FormUrlEncoded
    suspend fun sendMessageToChatId(
        @Field("chatId") chatId: String,
        @Field("body") body: String
    ): SentMessageResponse

    @POST("group")
    @FormUrlEncoded
    fun createGroup(
        @Field("phones") phones: List<String>,
        @Field("messageText") messageText: String,
        @Field("groupName") groupName: String
    ): Call<JsonObject>

    @GET("dialogs")
    fun getChatList(): Call<DialogsResponse>
}