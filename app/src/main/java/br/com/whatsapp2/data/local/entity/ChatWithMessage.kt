package br.com.whatsapp2.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ChatWithMessage(
    @Embedded
    val Chat: Chat,
    @Relation(
        parentColumn = "userPk",
        entityColumn = "pk"
    )
    val user: User,
    @Relation(
        parentColumn = "pk",
        entityColumn = "chatPk"
    )
    val messages: List<Message>
)
