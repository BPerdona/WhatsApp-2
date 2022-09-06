package br.com.whatsapp2.domain.model

data class User(
    val pk: Int,
    val username: String,
    val password: String = "empty"
)