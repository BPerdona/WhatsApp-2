package br.com.whatsapp2.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation


data class GroupWithMessage(
    @Embedded
    val group: Group,
    @Relation(
        parentColumn = "pk",
        entityColumn = "groupPk"
    )
    val messages: List<Message>
)