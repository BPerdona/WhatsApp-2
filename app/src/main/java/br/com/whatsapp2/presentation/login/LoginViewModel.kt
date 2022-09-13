package br.com.whatsapp2.presentation.login

import android.util.Log
import androidx.lifecycle.*
import br.com.whatsapp2.data.local.daos.UserDao
import br.com.whatsapp2.data.local.entity.User
import br.com.whatsapp2.util.RabbitUri
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import java.nio.charset.Charset

class LoginViewModel(private val dao: UserDao): ViewModel() {
    private lateinit var users: List<User>
    var loggedUser = User(-1, "temp", "")

    init {
        viewModelScope.launch {
            users = dao.getAllUsers()
        }
    }

    fun logIn(username: String, password: String): Boolean{
        users.forEach {
            if (it.username == username && it.password == password){
                loggedUser = it
                return true
            }
        }
        return false
    }

    fun signUpUser(username: String, password: String): Boolean{
        users.forEach {
            if(it.username == username)
                return false
        }
        viewModelScope.launch{
            createQueue(username)
        }
        viewModelScope.launch {
            dao.insertUser(
                User(
                    pk = getNextPk(),
                    username = username,
                    password = password
                )
            )
            updateUsersList()
        }
        return true
    }

    private suspend fun createQueue(username: String){
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

    private fun updateUsersList(){
        viewModelScope.launch{
            users = dao.getAllUsers()
        }
    }

    private fun getNextPk(): Int{
        return if(users.isEmpty()) 0 else users.last().pk + 1
    }
}

class LoginVMFactory(private val dao: UserDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel(dao) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}