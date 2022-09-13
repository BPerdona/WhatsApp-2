package br.com.whatsapp2.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import br.com.whatsapp2.data.local.entity.Message

@Dao
interface MessageDao{

    @Insert
    suspend fun saveMessage(message: Message)

}