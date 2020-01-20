package com.whatsappgrouper.spring.repository.db

import com.whatsappgrouper.spring.model.Message
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageDatabase : CrudRepository<Message, String> {

    fun getById(id: String): Message

}