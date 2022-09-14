package br.com.whatsapp2.presentation.home

import android.util.Log
import androidx.lifecycle.*
import br.com.whatsapp2.data.local.daos.ChatDao
import br.com.whatsapp2.data.local.daos.MessageDao
import br.com.whatsapp2.data.local.entity.Chat
import br.com.whatsapp2.data.local.entity.ChatWithMessage
import br.com.whatsapp2.data.local.entity.Message
import br.com.whatsapp2.data.local.entity.User
import br.com.whatsapp2.util.RabbitUri
import com.rabbitmq.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import java.nio.charset.StandardCharsets
import java.util.*

class HomeViewModel(private val dao: ChatDao, private val messageDao: MessageDao): ViewModel() {

    var user: User = User(-1,"","")
    private var _chats: LiveData<List<ChatWithMessage>> = MutableLiveData()
    val chats: LiveData<List<ChatWithMessage>>
    get() {
        return _chats
    }

    fun getChats(userId: Int){
        _chats = dao.getUserChat(userId).asLiveData()
    }

    fun getChatLastIndex(): Int{
        return if(_chats.value?.isEmpty() == true) 0 else _chats.value?.last()?.Chat?.pk?.plus(1) ?: 0
    }

    fun startConsume(){
        val factory = ConnectionFactory()
        factory.setUri(RabbitUri)
        val job = viewModelScope.launch{
            consumeMessages(factory)
        }
    }

    private suspend fun consumeMessages(factory: ConnectionFactory){
        withContext(Dispatchers.IO){
            val connection = factory.newConnection()
            val channel = connection.createChannel()
            channel.queueDeclare(user.username, false, false, false, null)

            val deliverCallback = DeliverCallback{consumerTag: String?, delivery: Delivery ->
                val message = String(delivery.body, StandardCharsets.UTF_8).split(",")
                addMessage(message[0], message[1])
                Log.e("a", "$consumerTag receive a message: $message")
            }

            val cancelCallback = CancelCallback { consumerTag: String? ->
                Log.e("a", "$consumerTag was cancelled")
            }

            channel.basicConsume(user.username, true, deliverCallback, cancelCallback)
            delay(200L)
            channel.close()
            connection.close()
            consumeMessages(factory)
        }
    }

    private fun addMessage(contact: String, text:String){
        Log.e("a", "Entrou add msg")
        viewModelScope.launch {
            var chatPk = -1
            chats.value?.forEach {
                if(it.Chat.contact == contact){
                    chatPk=it.Chat.pk
                }
            }
            Log.e("a", "Passou ForEach com index: $chatPk")
            if(chatPk == -1){
                var newIndex = getChatLastIndex()
                dao.saveChat(
                    Chat(
                        newIndex,
                        contact,
                        user.pk
                    )
                )
                Log.e("a", "Salvou chat: $newIndex")
                chatPk=newIndex
            }
            messageDao.saveMessage(
                Message(
                    pk=UUID.randomUUID().toString(),
                    sender = contact,
                    chatPk = chatPk,
                    text = text
                )
            )
            getChats(user.pk)
        }
    }
}


class HomeVMFactory(private val dao: ChatDao, private val messageDao: MessageDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(dao, messageDao) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}