package br.com.whatsapp2.domain.model

data class Message(
    val text: String,
    val isMine: Boolean = false
)