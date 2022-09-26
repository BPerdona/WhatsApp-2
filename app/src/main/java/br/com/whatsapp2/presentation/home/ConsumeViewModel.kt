package br.com.whatsapp2.presentation.home

import android.util.Log
import androidx.lifecycle.*
import br.com.whatsapp2.data.local.daos.ChatDao
import br.com.whatsapp2.data.local.daos.GroupDao
import br.com.whatsapp2.data.local.daos.MessageDao
import br.com.whatsapp2.data.local.daos.MessageGroupDao
import br.com.whatsapp2.data.local.entity.*
import br.com.whatsapp2.util.RabbitUri
import com.rabbitmq.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import java.nio.charset.StandardCharsets
import java.util.*

class ConsumeViewModel(
    private val dao: ChatDao,
    private val groupDao: GroupDao,
    private val messageDao: MessageDao,
    private val messageGroup: MessageGroupDao
    ): ViewModel(){

    var user: User = User(-1,"","")
    private var _chats: LiveData<List<ChatWithMessage>> = dao.getAllChats().asLiveData()
    val chats: LiveData<List<ChatWithMessage>>
        get() {
            return _chats
        }

    private var _groups: LiveData<List<GroupWithMessage>> = groupDao.getAllGroups().asLiveData()
    val groups: LiveData<List<GroupWithMessage>>
    get() {
        return _groups
    }

    fun getChatLastIndex(): Int{
        return if(_chats.value?.isEmpty() == true) 1 else _chats.value?.last()?.Chat?.pk?.plus(1) ?: 1
    }

    fun getLastGroupIndex(): Int{
        return if(_groups.value?.isEmpty() == true) 1 else _groups.value?.last()?.group?.pk?.plus(1) ?: 1
    }

    fun newGroupConsume(exchange: String){
        val factory = ConnectionFactory()
        factory.setUri(RabbitUri)
        viewModelScope.launch {
            consumeGroupMessages(factory, exchange)
        }
    }

    fun startConsume(){
        val factory = ConnectionFactory()
        factory.setUri(RabbitUri)
        viewModelScope.launch{
            consumeMessages(factory)
        }
        viewModelScope.launch {
            while (user.pk == -1){
                delay(200L)
            }
            groups.value?.filter { it.group.userPk == user.pk }?.forEach {
                consumeGroupMessages(factory, it.group.groupName)
            }
        }
    }

    private suspend fun consumeGroupMessages(factory: ConnectionFactory, exchange: String){
        withContext(Dispatchers.IO){
            try {
                val connection = factory.newConnection()
                val channel = connection.createChannel()
                try{
                    channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT, true)
                    val queueName = channel.queueDeclare().queue
                    channel.queueBind(queueName, exchange,"")

                    val deliverCallback = DeliverCallback{consumerTag: String?, delivery: Delivery ->
                        val message = String(delivery.body, StandardCharsets.UTF_8).split("|?|")
                        addMessageGroup(exchange, message[0], message[1])
                        Log.e("group", "receive a message: $message")
                    }

                    val cancelCallback = CancelCallback { consumerTag: String? ->
                        Log.e("group", "$consumerTag was cancelled")
                    }
                    channel.basicConsume(queueName, true, deliverCallback, cancelCallback)
                    Log.e("group", "Iniciou basic consume: $exchange")
                }catch (e: Exception){
                    Log.e("group", "Erro ao consumir mensagens: ${e.message}")
                    channel.close()
                    connection.close()
                }
            }catch (e: Exception){
                Log.e("group", "Erro ao conectar mensagens: ${e.message}")
            }
        }
    }

    private suspend fun consumeMessages(factory: ConnectionFactory){
        while (user.pk == -1){
            delay(200L)
        }
        withContext(Dispatchers.IO){
            while (true){
                try {
                    val connection = factory.newConnection()
                    val channel = connection.createChannel()
                    try{
                        channel.queueDeclare(user.username, false, false, false, null)
                        val deliverCallback = DeliverCallback{consumerTag: String?, delivery: Delivery ->
                            val message = String(delivery.body, StandardCharsets.UTF_8).split("|?|")
                                addMessageChat(message[0], message[1])
                            Log.e("chat", "$consumerTag receive a message: $message")
                        }

                        val cancelCallback = CancelCallback { consumerTag: String? ->
                            Log.e("chat", "$consumerTag was cancelled")
                        }
                        channel.basicConsume(user.username, true, deliverCallback, cancelCallback)
                        Log.e("chat", "Iniciou basic consume")
                        delay(5000L)
                        channel.close()
                        connection.close()
                        Log.e("chat", "Fechou as conexões")
                    }catch (e: Exception){
                        Log.e("chat", "Erro ao consumir mensagens: ${e.message}")
                        channel.close()
                        connection.close()
                    }
                }catch (e: Exception){
                    Log.e("chat", "Erro ao conectar mensagens: ${e.message}")
                }
                delay(100L)
            }
        }
    }

    private fun addMessageGroup(group: String, contact: String, text: String){
        Log.e("aa","Armazenando mensagem")
        viewModelScope.launch {
            if(contact == user.username){
                Log.e("aa","Recebeu mensagem de si mesmo")
                return@launch
            }

            var groupPk = -1
            groups.value?.filter { it.group.userPk == user.pk }?.forEach {
                if(it.group.groupName == group){
                    groupPk=it.group.pk
                }
            }
            if(groupPk==-1){
                Log.e("a", "Pegou o indice -1 e saiu do armazenamento")
                return@launch
            }
            messageGroup.saveMessage(
                MessageGroup(
                    pk = UUID.randomUUID().toString(),
                    text = text,
                    sender = contact,
                    groupPk = groupPk
                )
            )
            Log.e("a","Save message: \npk:UUID\ntext:$text\nsender:$contact\ngroup:$groupPk")
        }
    }

    private fun addMessageChat(contact: String, text:String){
        viewModelScope.launch {
            var chatPk = -1
            chats.value?.filter{ it.Chat.userPk == user.pk }?.forEach {
                if(it.Chat.contact == contact){
                    chatPk=it.Chat.pk
                }
            }
            if(chatPk == -1){
                var newIndex = getChatLastIndex()
                dao.saveChat(
                    Chat(
                        newIndex,
                        contact,
                        user.pk
                    )
                )
                chatPk=newIndex
            }
            delay(10L)
            messageDao.saveMessage(
                Message(
                    pk= UUID.randomUUID().toString(),
                    sender = contact,
                    chatPk = chatPk,
                    text = text
                )
            )
        }
    }

}


class ConsumeVMFactory(
    private val dao: ChatDao,
    private val groupDao: GroupDao,
    private val messageDao: MessageDao,
    private val messageGroup: MessageGroupDao
    ) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ConsumeViewModel::class.java))
            return ConsumeViewModel(dao, groupDao, messageDao, messageGroup) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}