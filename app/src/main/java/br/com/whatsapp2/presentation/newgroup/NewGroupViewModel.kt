package br.com.whatsapp2.presentation.newgroup

import android.util.Log
import androidx.lifecycle.*
import br.com.whatsapp2.data.local.daos.ChatDao
import br.com.whatsapp2.data.local.daos.GroupDao
import br.com.whatsapp2.data.local.entity.Chat
import br.com.whatsapp2.data.local.entity.Group
import br.com.whatsapp2.data.local.entity.GroupWithMessage
import br.com.whatsapp2.data.local.entity.User
import br.com.whatsapp2.util.RabbitUri
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class NewGroupViewModel(val dao: GroupDao): ViewModel(){

    var lastGroupPk: Int = -1
    var user: User = User(-1,"", "")
    var groupList: LiveData<List<GroupWithMessage>> = dao.getUserGroups(user.pk).asLiveData()

    fun setUserConst(lastChatId: Int, user: User){
        this.lastGroupPk = lastChatId
        this.user = user
    }

    fun createEntryGroup(groupName: String){
        Log.e("aa", "entrando group na lista: $groupList")
        groupList.value?.forEach {
            if(it.group.groupName==groupName){
                return
            }
        }
        Log.e("aa", "Continuou para salvar lista")
        viewModelScope.launch{
            dao.saveGroup(
                Group(
                    pk = lastGroupPk,
                    groupName = groupName,
                    userPk = user.pk
                )
            )
        }
        viewModelScope.launch {
            bindQueue(groupName)
        }
    }


    private suspend fun bindQueue(groupName: String){
        withContext(Dispatchers.IO){
            val factory = ConnectionFactory()
            factory.setUri(RabbitUri)
            val connection = factory.newConnection()
            val channel = connection.createChannel()
            channel.exchangeDeclare(groupName, BuiltinExchangeType.FANOUT, true)
            channel.queueBind(user.username, groupName, "")
            channel.close()
            connection.close()
        }
    }
}

class NewGroupVMFactory(private val dao: GroupDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewGroupViewModel::class.java))
            return NewGroupViewModel(dao) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}