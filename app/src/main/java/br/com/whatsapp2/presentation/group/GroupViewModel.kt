package br.com.whatsapp2.presentation.group

import android.util.Log
import androidx.lifecycle.*
import br.com.whatsapp2.data.local.daos.GroupDao
import br.com.whatsapp2.data.local.daos.MessageDao
import br.com.whatsapp2.data.local.daos.MessageGroupDao
import br.com.whatsapp2.data.local.entity.GroupWithMessage
import br.com.whatsapp2.data.local.entity.Message
import br.com.whatsapp2.data.local.entity.MessageGroup
import br.com.whatsapp2.util.RabbitUri
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import java.nio.charset.Charset
import java.util.*

class GroupViewModel(private val daoGroup: GroupDao, private val daoMessage: MessageGroupDao): ViewModel(){

    var userName: String = ""
    private var _groupWithMessage: LiveData<GroupWithMessage> = MutableLiveData()
    val groupWithMessage: LiveData<GroupWithMessage>
    get() {
        return _groupWithMessage
    }

    fun getGroupMessages(userPk: Int){
        _groupWithMessage = daoGroup.getGroupWithMessages(userPk).asLiveData()
    }

    fun sendMessage(message: String, group: String){
        viewModelScope.launch {
            daoMessage.saveMessage(
                MessageGroup(
                    pk = UUID.randomUUID().toString(),
                    text = message,
                    sender = userName,
                    groupPk = groupWithMessage.value?.group?.pk ?: 1
                )
            )
        }
        viewModelScope.launch{
            sendMessageRMQ(message, group)
        }
    }

    private suspend fun sendMessageRMQ(messageChat: String, group: String){
        withContext(Dispatchers.IO){
            try{
                val factory = ConnectionFactory()
                factory.setUri(RabbitUri)
                val connection = factory.newConnection()
                val channel = connection.createChannel()
                val message = "$userName|?|$messageChat"
                channel.basicPublish(group, "", null, message.toByteArray(Charset.forName("UTF-8")))
                channel.close()
                connection.close()
                Log.e("grupo", "Mensagem enviada: $messageChat")
            }catch (e: Exception){
                Log.e("grupo", "Erro ao enviar mensagem: $messageChat")
                delay(500L)
                sendMessageRMQ(messageChat, group)
            }
        }
    }
}

class GroupVMFactory(private val daoChat: GroupDao, private val daoMessage: MessageGroupDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GroupViewModel::class.java))
            return GroupViewModel(daoChat, daoMessage) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}