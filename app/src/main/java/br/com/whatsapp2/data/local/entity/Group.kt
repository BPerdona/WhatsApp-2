package br.com.whatsapp2.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.RESTRICT
import androidx.room.PrimaryKey

@Entity(
    tableName = "group",
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
data class Group(
    @PrimaryKey(autoGenerate = true)
    val pk: Int,
    val groupName: String,
    val userPk: Int
)