package br.com.whatsapp2.domain.model

data class Chat(
    val contact: User,
    var messages: List<Message>,
)
