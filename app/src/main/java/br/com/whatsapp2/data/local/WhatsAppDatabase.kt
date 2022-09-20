package br.com.whatsapp2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.whatsapp2.data.local.daos.*
import br.com.whatsapp2.data.local.entity.*


@Database(
    entities = [Chat::class, User::class, Message::class, Group::class, MessageGroup::class],
    version = 1,
    exportSchema = false
)
abstract class WhatsAppDatabase: RoomDatabase() {

    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun groupDao(): GroupDao
    abstract fun messageGroupDao(): MessageGroupDao

    companion object {

        @Volatile
        private var INSTANCE: WhatsAppDatabase? = null

        fun getInstance(context: Context): WhatsAppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    WhatsAppDatabase::class.java,
                    "whatsapp_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}