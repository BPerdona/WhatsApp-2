package br.com.whatsapp2.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.whatsapp2.data.local.entity.Chat
import br.com.whatsapp2.data.local.entity.ChatWithMessage
import br.com.whatsapp2.data.local.entity.Message
import java.util.*

@Composable
fun HomeScreen(
    nav: NavController,
    viewModel: HomeViewModel
){
    viewModel.startConsume()
    val chats = viewModel.chats.observeAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFF111b21)),
    ) {
        Card(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth()
                .shadow(10.dp),
            backgroundColor = Color(0xFF005c4b),
        ) {
            Text(
                modifier = Modifier.padding(15.dp),
                textAlign = TextAlign.Center,
                text = "WhatsApp2",
                color = Color.White,
                style = MaterialTheme.typography.h5.copy(
                    Color.White, fontWeight = FontWeight.Bold
                )
            )
        }
    Menu(nav)
    HomeList(nav = nav, chats = chats.value ?: listOf())
    }
}

@Composable
fun HomeList(
    nav: NavController,
    chats: List<ChatWithMessage>
){
    LazyColumn(){
        items(chats){ chat ->
            ChatCard(
                chat = chat.Chat,
                lastMessage = if (chat.messages.isEmpty()) "Inicie a conversa!" else chat.messages.last().text,
                nav = nav
            )
        }
    }
}

@Composable
fun ChatCard(
    chat: Chat,
    lastMessage: String,
    nav: NavController
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable {
                nav.navigate("chat/${chat.pk}") //TODO
            },
        backgroundColor = Color(0xFF303c44),
    ) {
        Column() {
            Row(
                modifier = Modifier.padding(start = 6.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .padding(end = 4.dp)
                        .size(67.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = chat.contact[0].uppercase(),
                        style = MaterialTheme.typography.h4
                            .copy(color = Color.White, fontWeight = FontWeight.Normal)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 3.dp),
                        text = chat.contact,
                        style = MaterialTheme.typography.h5.copy(
                            Color.White, fontWeight = FontWeight.Bold
                        ),
                    )
                    Text(
                        modifier = Modifier.padding(top = 3.dp),
                        text = if(lastMessage.length>=35) lastMessage.substring(0,35)+"...." else lastMessage,
                        style = MaterialTheme.typography.subtitle2.copy(
                            Color.LightGray, fontWeight = FontWeight.Normal
                        ),
                    )
                }
            }
        }
    }
}


@Composable
fun Menu(nav: NavController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .size(60.dp)
            .clickable {
                nav.navigate("newchat") //TODO
            },
        backgroundColor = Color(0xFF303c44)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 15.dp)
        ){
            Text(
                modifier = Modifier.weight(1f),
                text = "Nova conversa",
                style = MaterialTheme.typography.h6.copy(
                    Color.White, fontWeight = FontWeight.Bold
                ),
            )
            Icon(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(32.dp),
                imageVector = Icons.Default.Add,
                contentDescription = "Start new chat",
                tint = Color(0xFF005c4b)
            )
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .size(60.dp)
            .clickable {
                nav.navigate("newgroup") //TODO
            },
        backgroundColor = Color(0xFF303c44)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 15.dp)
        ){
            Text(
                modifier = Modifier.weight(1f),
                text = "Entrar em um grupo",
                style = MaterialTheme.typography.h6.copy(
                    Color.White, fontWeight = FontWeight.Bold
                ),
            )
            Icon(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(32.dp),
                imageVector = Icons.Default.Add,
                contentDescription = "Start new chat",
                tint = Color(0xFF005c4b)
            )
        }
    }
}


private fun formatMessage(message: String): String{
    var formatedMessage = ""
    if(message.length>=35){
        formatedMessage = message.substring(0,35)+"..."
    }
    return formatedMessage
}