package com.whatsappgrouper.spring.util

fun String.toPhoneNumber(): String? {
    if (!this.endsWith("@c.us")) return null

    return this.replace("@c.us", "")
}