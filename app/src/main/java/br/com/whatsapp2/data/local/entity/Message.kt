package br.com.whatsapp2.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.RESTRICT
import androidx.room.PrimaryKey

@Entity(
    tableName = "message",
    foreignKeys = [
        ForeignKey(
            entity = Chat::class,
            parentColumns = arrayOf("pk"),
            childColumns = arrayOf("chatPk"),
            onDelete = RESTRICT,
            onUpdate = CASCADE
        )
    ]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val pk: Int,
    val chatPk: Int,
    val text: String,
    val sender: String
)