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
import androidx.compose.ui.unit.sp
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
    val group = viewModel.groupWithMessage.observeAsState()
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
                        text = group.value?.group?.groupName?.get(1)?.uppercase() ?: "?",
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
                        text = group.value?.group?.groupName?.substring(1)?:"",
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
    val user = viewModel.userName
    Box(
        modifier=modifier,
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            reverseLayout = true,
        ) {
            items(groups.value?.messages?.asReversed() ?: listOf()){
                MessageCard(msg = it,  user)
            }
        }
    }
}


@Composable
fun MessageCard(
    msg: MessageGroup,
    user: String
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = when (msg.sender) {
            user -> {
                Alignment.End
            }
            else -> {
                Alignment.Start
            }
        },
    ) {
        Card(
            modifier = Modifier.widthIn(max = 340.dp),
            shape = cardShapeForm(msg.sender==user),
            backgroundColor = when(msg.sender){
                user -> {
                    Color(0xFF005c4b)
                }
                else -> {
                    Color(0xFF202c33)

                }
            }
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = msg.text,
                color = Color.White
            )
        }
        if(msg.sender != user)
            Text(
                text = msg.sender,
                fontSize = 14.sp
            )
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