package br.com.whatsapp2.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.RESTRICT
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("pk"),
            childColumns = arrayOf("userPk"),
            onUpdate = CASCADE,
            onDelete = RESTRICT
        )
    ]
)
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val pk: Int,
    val contact: String,
    val userPk: Int
)
