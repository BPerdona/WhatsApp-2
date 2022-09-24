package br.com.whatsapp2.presentation.newchat

import androidx.lifecycle.*
import br.com.whatsapp2.data.local.daos.ChatDao
import br.com.whatsapp2.data.local.entity.Chat
import br.com.whatsapp2.data.local.entity.ChatWithMessage
import br.com.whatsapp2.remote.RabbitApi
import br.com.whatsapp2.remote.models.SourceQueue
import br.com.whatsapp2.util.RabbitUri
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class NewChatViewModel(val dao: ChatDao): ViewModel(){

    init {
        viewModelScope.launch {
            _queueList = MutableLiveData(RabbitApi.retrofitService.getQueues())
        }
    }

    var lastChatPk: Int = 0
    var user: Int = 0
    private var _chatList: LiveData<List<ChatWithMessage>> = MutableLiveData()
    val chatList: LiveData<List<ChatWithMessage>>
        get() = _chatList

    private var _queueList: LiveData<List<SourceQueue>> = MutableLiveData()
    val queueList: LiveData<List<SourceQueue>>
        get(){
            val names = _chatList.value?.map { it.Chat.contact } ?: listOf()
            val queues = _queueList.value?.filter {
                !names.contains(it.name)
            }
            return if(_filter.value == "") {
                MutableLiveData(queues)
            }
            else{
                MutableLiveData(queues?.filter { it.name.contains(_filter.value ?: "")})
            }
        }

    private val _filter: MutableLiveData<String> = MutableLiveData("")
    val filter: LiveData<String>
        get() = _filter

    fun updateFilter(word: String){
        _filter.value = word
    }

    fun setUserConst(lastChatId: Int, userPk: Int){
        this.lastChatPk = lastChatId
        this.user = userPk
        _chatList = dao.getUserChat(userPk).asLiveData()
    }

    fun createChat(username: String){
        _chatList.value?.forEach {
            if(it.Chat.contact == username){
                return
            }
        }
        viewModelScope.launch{
            dao.saveChat(
                Chat(
                    pk = lastChatPk,
                    contact = username,
                    userPk = user
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