package br.com.whatsapp2.presentation.newgroup

import android.util.Log
import androidx.lifecycle.*
import br.com.whatsapp2.data.local.daos.GroupDao
import br.com.whatsapp2.data.local.entity.*
import br.com.whatsapp2.remote.RabbitApi
import br.com.whatsapp2.remote.models.SourceExchenge
import br.com.whatsapp2.util.RabbitUri
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class NewGroupViewModel(val dao: GroupDao): ViewModel(){

    init {
        viewModelScope.launch {
            _exchangeList = MutableLiveData(RabbitApi.retrofitService.getExchengs())
        }
    }

    var lastGroupPk: Int = -1
    var user: User = User(-1,"", "")

    private var _groupList: LiveData<List<String>> = MutableLiveData()
    val groupList: LiveData<List<String>>
        get() = _groupList

    private var _exchangeList: LiveData<List<SourceExchenge>> = MutableLiveData()
    val exchangeList: LiveData<List<SourceExchenge>>
        get() {
            viewModelScope.launch {
                _exchangeList = MutableLiveData(RabbitApi.retrofitService.getExchengs())
            }
            if(_exchangeList.value?.isEmpty()==true)
                return MutableLiveData()
            val names = _groupList.value ?: listOf()
            Log.e("", names.toString())
            var exchanges = _exchangeList.value
                ?.filter { it.name.isNotEmpty() }
                ?.filter { it.name[0]=='*' }
                ?.filter { !names.contains(it.name) }

            return if(_filter.value == ""){
                MutableLiveData(exchanges)
            }
            else{
                MutableLiveData(exchanges?.filter { it.name.contains(_filter.value?:"") })
            }
        }

    private var _filter: MutableLiveData<String> = MutableLiveData("")
    val filter: LiveData<String>
        get() = _filter

    fun updateFilter(word:String){
        _filter.value = word
    }

    fun setUserConst(groupId: Int, user: User){
        this.lastGroupPk = groupId
        this.user = user
        _groupList = dao.getAllGroupsNames(user.pk).asLiveData()
    }

    fun createGroup(groupName: String){
        groupList.value?.forEach {
            if(it==groupName){
                return
            }
        }
        viewModelScope.launch {
            dao.saveGroup(
                Group(
                    pk = lastGroupPk,
                    groupName = "*$groupName",
                    userPk = user.pk
                )
            )
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val factory = ConnectionFactory()
                factory.setUri(RabbitUri)
                val connection = factory.newConnection()
                val channel = connection.createChannel()
                channel.exchangeDeclare("*$groupName", BuiltinExchangeType.FANOUT, true)
                channel.close()
                connection.close()
            }
        }
    }

    fun entryGroup(groupName: String){
        groupList.value?.forEach {
            if(it==groupName){
                return
            }
        }
        viewModelScope.launch{
            dao.saveGroup(
                Group(
                    pk = lastGroupPk,
                    groupName = groupName,
                    userPk = user.pk
                )
            )
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