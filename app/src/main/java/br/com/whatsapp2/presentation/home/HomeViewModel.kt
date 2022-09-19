package br.com.whatsapp2.presentation.home

import androidx.lifecycle.*
import br.com.whatsapp2.data.local.daos.ChatDao
import br.com.whatsapp2.data.local.daos.GroupDao
import br.com.whatsapp2.data.local.daos.MessageDao
import br.com.whatsapp2.data.local.entity.ChatWithMessage
import br.com.whatsapp2.data.local.entity.GroupWithMessage
import br.com.whatsapp2.data.local.entity.User
import java.lang.IllegalArgumentException

class HomeViewModel(private val dao: ChatDao, private val groupDao: GroupDao): ViewModel() {

    var user: User = User(-1,"","")

    private var _chats: LiveData<List<ChatWithMessage>> = MutableLiveData()
    val chats: LiveData<List<ChatWithMessage>>
    get() {
        return _chats
    }

    private var _groups: LiveData<List<GroupWithMessage>> = MutableLiveData()
    val groups: LiveData<List<GroupWithMessage>>
    get() {
        return _groups
    }

    fun getChats(userId: Int){
        _chats = dao.getUserChat(userId).asLiveData()
        _groups = groupDao.getUserGroups(userId).asLiveData()
    }
}


class HomeVMFactory(private val dao: ChatDao, private val groupDao: GroupDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(dao, groupDao) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}