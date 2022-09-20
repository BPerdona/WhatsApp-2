package br.com.whatsapp2.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import br.com.whatsapp2.data.local.entity.MessageGroup

@Dao
interface MessageGroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMessage(messageGroup: MessageGroup)
}