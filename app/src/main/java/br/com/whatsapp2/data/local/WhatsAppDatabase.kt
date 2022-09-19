package br.com.whatsapp2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.whatsapp2.data.local.daos.ChatDao
import br.com.whatsapp2.data.local.daos.GroupDao
import br.com.whatsapp2.data.local.daos.MessageDao
import br.com.whatsapp2.data.local.daos.UserDao
import br.com.whatsapp2.data.local.entity.Chat
import br.com.whatsapp2.data.local.entity.Group
import br.com.whatsapp2.data.local.entity.Message
import br.com.whatsapp2.data.local.entity.User


@Database(
    entities = [Chat::class, User::class, Message::class, Group::class],
    version = 1,
    exportSchema = false
)
abstract class WhatsAppDatabase: RoomDatabase() {

    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun groupDao(): GroupDao

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