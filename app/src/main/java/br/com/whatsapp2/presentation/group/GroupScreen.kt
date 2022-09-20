package br.com.whatsapp2.presentation.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.whatsapp2.data.local.entity.Message
import br.com.whatsapp2.data.local.entity.MessageGroup

@Composable
fun GroupScreen(
    viewModel: GroupViewModel
){
    Column(Modifier.fillMaxSize()) {
        GroupLabel(viewModel)
        GroupMessageList(
            viewModel = viewModel,
            modifier = Modifier
                .background(Color(0xFF111b21))
                .weight(1f)
        )
        GroupMessageInput(viewModel)
    }

}

@Composable
fun GroupLabel(
    viewModel: GroupViewModel
){
    val groups = viewModel.groupWithMessage.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF202c33)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column() {
            Row(
                modifier = Modifier.padding(6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = groups.value?.group?.groupName?.get(0)?.uppercase() ?: "?",
                        style = MaterialTheme.typography.h4
                            .copy(color = Color.White, fontWeight = FontWeight.Normal)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = groups.value?.group?.groupName?.uppercase()?:"",
                        style = MaterialTheme.typography.h5
                            .copy(color = Color.White, fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun GroupMessageList(
    viewModel: GroupViewModel,
    modifier: Modifier
){
    var groups = viewModel.groupWithMessage.observeAsState()
    Box(
        modifier=modifier,
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            reverseLayout = true,
        ) {
            items(groups.value?.messages?.asReversed() ?: listOf()){
                MessageCard(msg = it, groups.value?.group?.groupName ?: "")
            }
        }
    }
}


@Composable
fun MessageCard(
    msg: MessageGroup,
    contact:String
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = when (msg.sender) {
            contact -> {
                Alignment.Start
            }
            else -> {
                Alignment.End
            }
        },
    ) {
        Card(
            modifier = Modifier.widthIn(max = 340.dp),
            shape = cardShapeForm(msg.sender!=contact),
            backgroundColor = when(msg.sender){
                contact -> {
                    Color(0xFF202c33)
                }
                else -> {
                    Color(0xFF005c4b)

                }
            }
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = msg.text,
                color = Color.White
            )
        }
    }
}

@Composable
fun GroupMessageInput(
    viewModel: GroupViewModel
){
    var groups = viewModel.groupWithMessage.observeAsState()
    var message by remember{
        mutableStateOf("")
    }
    Row(
        modifier = Modifier
            .background(Color(0xFF202c33))
            .padding(5.dp)
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = {message=it},
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFF2a3942),
                cursorColor = Color.White,
                textColor = Color.White,
                focusedIndicatorColor = Color(0xFF005c4b),
            ),
        )
        Button(
            modifier = Modifier
                .height(56.dp)
                .padding(start = 5.dp, end = 5.dp),
            onClick = {
                viewModel.sendMessage(message, groups.value?.group?.groupName ?: "Anon")
                message = ""
            },
            enabled = message.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF005c4b),
                disabledBackgroundColor = Color.DarkGray
            )
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send Message"
            )
        }
    }
}

@Composable
fun cardShapeForm(messageMine:Boolean): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return when {
        messageMine -> roundedCorners.copy(bottomEnd = CornerSize(0))
        else -> roundedCorners.copy(bottomStart = CornerSize(0))
    }
}