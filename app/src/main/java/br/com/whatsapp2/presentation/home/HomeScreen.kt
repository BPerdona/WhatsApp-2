package br.com.whatsapp2.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.whatsapp2.data.local.entity.Chat
import br.com.whatsapp2.data.local.entity.ChatWithMessage

@Composable
fun HomeScreen(
    nav: NavController,
    viewModel: HomeViewModel
){
    val chats = viewModel.chats.observeAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.LightGray),
    ) {
        Card(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth()
                .shadow(10.dp),
            backgroundColor = Color(0xFFa5cfaa),
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center,
                text = "WhatsApp2",
                color = Color.Black,
                style = MaterialTheme.typography.h5.copy(
                    Color.Black, fontWeight = FontWeight.Bold
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
                lastMessage = if (chat.messages.isEmpty()) "Comece a conversa!" else chat.messages.last().text,
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
            .size(60.dp)
            .clickable {
                nav.navigate("chat/${chat.pk}") //TODO
            },
        backgroundColor = Color.White,
    ) {
        Row(
            modifier = Modifier.padding(start = 15.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = chat.contact,
                    style = MaterialTheme.typography.h6.copy(
                        Color.Black, fontWeight = FontWeight.Bold
                    ),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = lastMessage,
                    style = MaterialTheme.typography.subtitle2.copy(
                        Color.LightGray, fontWeight = FontWeight.Bold
                    ),
                )
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
        backgroundColor = Color.White
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
                    Color.Black, fontWeight = FontWeight.Bold
                ),
            )
            Icon(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(32.dp),
                imageVector = Icons.Default.Add,
                contentDescription = "Start new chat",
                tint = Color(0xFFa5cfaa)
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
        backgroundColor = Color.White
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
                    Color.Black, fontWeight = FontWeight.Bold
                ),
            )
            Icon(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(32.dp),
                imageVector = Icons.Default.Add,
                contentDescription = "Start new chat",
                tint = Color(0xFFa5cfaa)
            )
        }
    }
}