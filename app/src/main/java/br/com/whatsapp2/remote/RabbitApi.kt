package br.com.whatsapp2.remote

import br.com.whatsapp2.remote.models.SourceExchenge
import br.com.whatsapp2.remote.models.SourceQueue
import br.com.whatsapp2.util.RabbitMQ
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://shrimp.rmq.cloudamqp.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val client = OkHttpClient.Builder()
    .addInterceptor(BasicAuthInterceptor(RabbitMQ.username, RabbitMQ.password))
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface RabbitApiService{

    @GET("api/queues")
    suspend fun getQueues(): List<SourceQueue>

    @GET("api/exchanges")
    suspend fun getExchengs(): List<SourceExchenge>
}

object RabbitApi {
    val retrofitService: RabbitApiService by lazy {
        retrofit.create(RabbitApiService::class.java)
    }
}