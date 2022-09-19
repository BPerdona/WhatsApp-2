package br.com.whatsapp2.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.whatsapp2.data.local.entity.Group
import br.com.whatsapp2.data.local.entity.GroupWithMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGroup(group: Group)

    @Query("SELECT * FROM `group` WHERE userPk=:groupPk")
    fun getUserGroups(groupPk: Int): Flow<List<GroupWithMessage>>

    @Query("SELECT * FROM `group` WHERE pk=:groupPk")
    fun getGroupWithMessages(groupPk: Int): Flow<GroupWithMessage>

    @Query("SELECT * FROM `group`")
    fun getAllGroups(): Flow<List<GroupWithMessage>>

}