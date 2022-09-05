package br.com.whatsapp2.data.remote.dto

import br.com.whatsapp2.domain.model.Message
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.*

@Serializable
data class MessageDto(
    val text: String,
    val timeStamp: Long,
    val userName: String,
    val id: String
){
    fun toMessage(): Message {
        val date = Date(timeStamp)
        val formattedDate = DateFormat
            .getDateInstance(DateFormat.DEFAULT)
            .format(date)
        return Message(
            text = text,
            formattedTime = formattedDate,
            userName = userName
        )
    }
}