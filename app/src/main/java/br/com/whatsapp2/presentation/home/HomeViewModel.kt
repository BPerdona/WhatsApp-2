package br.com.whatsapp2.presentation.home

import androidx.lifecycle.*
import br.com.whatsapp2.data.local.daos.ChatDao
import br.com.whatsapp2.data.local.daos.MessageDao
import br.com.whatsapp2.data.local.entity.ChatWithMessage
import br.com.whatsapp2.data.local.entity.User
import java.lang.IllegalArgumentException

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
}


class HomeVMFactory(private val dao: ChatDao, private val messageDao: MessageDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(dao, messageDao) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}