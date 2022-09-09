package br.com.whatsapp2.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.whatsapp2.domain.model.Chat
import br.com.whatsapp2.domain.model.Message
import br.com.whatsapp2.domain.model.User
import br.com.whatsapp2.presentation.home.HomeList
import br.com.whatsapp2.presentation.home.Menu

@Composable
fun ChatScreen(
    nav: NavController,
    chat: Chat = Chat(
        1,
        mutableListOf(),
        User(1,"Bruno"),
        User(2, "Acacio"),
        lastMessage = "12:02"
    )
) {
    var message by remember{
        mutableStateOf("")
    }
    var currentMessages by remember {
        mutableStateOf(mutableListOf(Message("a", "12:02", "Acacio")))
    }


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
                text = chat.contact.username,
                color = Color.Black,
                style = MaterialTheme.typography.h5.copy(
                    Color.Black, fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        //ChatMessages(currentMessages)

        Row(
            modifier = Modifier.padding(5.dp)
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = message,
                onValueChange = {message=it},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.LightGray,
                    cursorColor = Color.DarkGray,
                    unfocusedLabelColor = Color.DarkGray,
                    focusedLabelColor = Color.DarkGray,
                    textColor = Color.Black,
                    focusedIndicatorColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.DarkGray
                ),
            )
            Button(
                modifier = Modifier
                    .height(56.dp)
                    .padding(start = 5.dp, end = 5.dp),
                onClick = {},
                enabled = message.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFa5cfaa)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Message"
                )
            }
        }
    }

}

@Composable
fun MessageList(
    messages: List<Message>
){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        reverseLayout = true
    ){
        items(messages){
            MessageCard(it)
        }
    }
}

@Composable
fun MessageCard(
    msg: Message
){
}