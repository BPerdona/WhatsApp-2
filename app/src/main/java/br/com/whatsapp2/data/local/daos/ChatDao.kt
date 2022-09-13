package br.com.whatsapp2.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.whatsapp2.data.local.entity.Chat
import br.com.whatsapp2.data.local.entity.ChatWithMessage

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChat(chat:Chat)

    @Query("SELECT * FROM chat WHERE userPk=:userPk")
    suspend fun getUserChat(userPk: Int): List<ChatWithMessage>

}