package com.whatsappgrouper.spring.repository.db

import com.test.model.Message
import org.springframework.data.repository.CrudRepository

interface MessageDatabase : CrudRepository<Message, String> {

    fun getById(id: String): Message

}