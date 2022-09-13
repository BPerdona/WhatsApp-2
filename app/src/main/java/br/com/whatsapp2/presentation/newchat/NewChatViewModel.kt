package br.com.whatsapp2.presentation.newchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.whatsapp2.data.local.daos.ChatDao
import br.com.whatsapp2.data.local.entity.Chat
import br.com.whatsapp2.util.RabbitUri
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class NewChatViewModel(val dao: ChatDao): ViewModel(){

    var lastChatPk: Int = -1
    var userPk: Int = -1
    var chatList: List<String> = listOf()

    fun setUserConst(lastChatId: Int, userPk: Int){
        this.lastChatPk = lastChatId
        this.userPk = userPk
    }

    fun createChat(username: String){
        if (chatList.contains(username))
            return
        viewModelScope.launch{
            dao.saveChat(
                Chat(
                    pk = lastChatPk,
                    contact = username,
                    userPk = userPk
                )
            )
        }
        viewModelScope.launch {
            addQueue(username)
        }
    }


    suspend fun addQueue(username: String){
        withContext(Dispatchers.IO){
            val factory = ConnectionFactory()
            factory.setUri(RabbitUri)
            val connection = factory.newConnection()
            val channel = connection.createChannel()
            channel.queueDeclare(username, false, false, false, null)
            channel.close()
            connection.close()
        }
    }
}

class NewChatVMFactory(private val dao: ChatDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewChatViewModel::class.java))
            return NewChatViewModel(dao) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}