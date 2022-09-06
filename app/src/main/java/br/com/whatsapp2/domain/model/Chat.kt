package br.com.whatsapp2.domain.model

data class Chat(
    val id: Int,
    val messages: List<Message>,
    val user: User,
    val contact: User,
    val lastMessage: String,
)
