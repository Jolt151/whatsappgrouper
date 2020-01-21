package com.whatsappgrouper.spring.util

fun String.toPhoneNumber(): String? {
    if (!this.endsWith("@c.us")) return null

    return this.replace("@c.us", "")
}

fun String.messageTypeToReadable(): String? {
    return when(this) {
        "chat" -> "Chat"
        "image" -> "Image"
        "ptt" -> "Voice note"
        "document" -> "Document"
        "audio" -> "Audio"
        "video" -> "Video"
        "location" -> "Location"
        "call_log" -> "Call log"
        else -> null
    }
}