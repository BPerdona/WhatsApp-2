package br.com.whatsapp2.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "messageGroup",
    foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = arrayOf("pk"),
            childColumns = arrayOf("groupPk"),
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class MessageGroup(
    @PrimaryKey
    val pk: String,
    val text: String,
    val sender: String,
    val groupPk: Int
)