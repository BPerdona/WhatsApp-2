package br.com.whatsapp2.presentation.home

import android.util.Log
import androidx.lifecycle.*
import br.com.whatsapp2.data.local.daos.ChatDao
import br.com.whatsapp2.data.local.entity.ChatWithMessage
import java.lang.IllegalArgumentException

class HomeViewModel(private val dao: ChatDao): ViewModel() {

    private var _chats: LiveData<List<ChatWithMessage>> = MutableLiveData()
    val chats: LiveData<List<ChatWithMessage>>
    get() {
       return _chats
    }

    fun getChats(userId: Int){
        _chats = dao.getUserChat(userId).asLiveData()
        Log.e("aa", "Chats: ${chats.value?.size}")
    }

    fun getChatLastIndex(): Int{
        return _chats.value?.last()?.Chat?.pk ?: 0
    }

}


class HomeVMFactory(private val dao: ChatDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(dao) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}