package com.test.api

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.test.model.Dialog
import com.test.model.Message
import retrofit2.Call
import retrofit2.http.*

interface WhatsappService {
    @GET("status")
    fun getStatus(): Call<JsonObject>

    @GET("messages")
    fun getMessages(@Query("lastMessageNumber") lastMessageNumber: Int? = null): Call<MessagesResponse>

    @POST("sendMessage")
    fun sendMessage(
        @Field("phone") phone: String,
        @Field("body") body: String
    )

    @POST("sendMessage")
    @FormUrlEncoded
    fun sendMessageToChatId(
        @Field("chatId") chatId: String,
        @Field("body") body: String
    ): Call<SentMessageResponse>

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