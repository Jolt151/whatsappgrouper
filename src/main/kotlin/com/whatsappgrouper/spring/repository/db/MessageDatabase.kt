package com.whatsappgrouper.spring.repository.db

import com.whatsappgrouper.spring.model.Message
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageDatabase : CrudRepository<Message, String> {

    fun getById(id: String): Message

    @Query(nativeQuery = true, value = "SELECT * from Message ORDER BY message_number DESC LIMIT 1")
    fun getLatestMessageNumber(): Message
}