package br.com.whatsapp2.presentation.chat

import android.util.Log
import androidx.lifecycle.*
import br.com.whatsapp2.data.local.daos.ChatDao
import br.com.whatsapp2.data.local.daos.MessageDao
import br.com.whatsapp2.data.local.entity.Chat
import br.com.whatsapp2.data.local.entity.ChatWithMessage
import br.com.whatsapp2.data.local.entity.Message
import br.com.whatsapp2.util.RabbitUri
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.IllegalArgumentException
import java.nio.charset.Charset
import java.util.*
import kotlin.random.Random

class ChatViewModel(private val daoChat: ChatDao, private val daoMessage: MessageDao): ViewModel() {

    var userName: String = ""
    private var _chatWithMessage: LiveData<ChatWithMessage> = MutableLiveData()
    val chatWithMessage: LiveData<ChatWithMessage>
    get() {
        return _chatWithMessage
    }

    fun getChatMessages(userPk:Int){
        _chatWithMessage = daoChat.getChatWithMessages(userPk).asLiveData()
    }

    fun sendMessage(message: String, contact:String){
        viewModelScope.launch {
            daoMessage.saveMessage(Message(
                pk = UUID.randomUUID().toString(),
                text = message,
                sender = chatWithMessage.value?.user?.username?:"anon",
                chatPk = chatWithMessage.value?.Chat?.pk ?: -1,
            ))
        }
        viewModelScope.launch{
            sendMessageRMQ(message, contact)
        }
    }

    private suspend fun sendMessageRMQ(messageChat: String, contact: String){
        withContext(Dispatchers.IO){
            val factory = ConnectionFactory()
            factory.setUri(RabbitUri)
            val connection = factory.newConnection()
            val channel = connection.createChannel()
            channel.queueDeclare(contact, false, false, false, null)
            val message = "${userName}|?|${messageChat}"
            channel.basicPublish("", contact, null, message.toByteArray(Charset.forName("UTF-8")))
            Log.e("chat", "Mensagem enviada: $messageChat")
            channel.close()
            connection.close()
        }
    }
}


class ChatVMFactory(private val daoChat: ChatDao, private val daoMessage: MessageDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ChatViewModel::class.java))
            return ChatViewModel(daoChat, daoMessage) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}