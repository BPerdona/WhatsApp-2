package br.com.whatsapp2.data.remote

import br.com.whatsapp2.domain.model.Message

interface MessageService {

    suspend fun getAllMessages(): List<Message>

    companion object{
        const val BASE_URL = "https://localhost:8000"
    }

    sealed class Endpoints(val url: String){
        object GetAllMessages: Endpoints("$BASE_URL/messages???")
    }
}