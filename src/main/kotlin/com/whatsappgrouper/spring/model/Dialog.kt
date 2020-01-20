package com.whatsappgrouper.spring.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Dialog(
        @Id val id: String,
        val name: String,
        val image: String
    //val metadata: String?
)