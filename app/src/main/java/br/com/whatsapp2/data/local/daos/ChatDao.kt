package br.com.whatsapp2.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.whatsapp2.data.local.entity.Chat
import br.com.whatsapp2.data.local.entity.ChatWithMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChat(chat:Chat)

    @Query("SELECT * FROM chat WHERE userPk=:userPk")
    fun getUserChat(userPk: Int): Flow<List<ChatWithMessage>>

    @Query("SELECT * FROM chat WHERE pk=:chatPk")
    fun getChatWithMessages(chatPk: Int): Flow<ChatWithMessage>

    @Query("SELECT * FROM chat")
    fun getAllChats(): Flow<List<ChatWithMessage>>
}