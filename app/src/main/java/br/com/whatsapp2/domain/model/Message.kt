package br.com.whatsapp2.domain.model

data class Message(
    val text: String,
    val formattedTime: String,
    val userName: String
)