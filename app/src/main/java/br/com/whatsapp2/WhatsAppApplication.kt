package br.com.whatsapp2

import android.app.Application
import br.com.whatsapp2.data.local.WhatsAppDatabase

class WhatsAppApplication(): Application() {

    val whatsAppDatabase: WhatsAppDatabase by lazy {
        WhatsAppDatabase.getInstance(this)
    }
}