package br.com.whatsapp2.presentation.newchat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NewChatScreen(
    nav: NavController,
    viewModel: NewChatViewModel
){
    var value by remember{ mutableStateOf("")}
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
                text = "Nova conversa",
                color = Color.Black,
                style = MaterialTheme.typography.h5.copy(
                    Color.Black, fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.size(40.dp))
        OutlinedTextField(
            value = value,
            onValueChange = {value=it},
            label = {
                Text(
                    text = "Nome do Contato",
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.Black, fontWeight = FontWeight.Bold
                    )
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.DarkGray,
                focusedIndicatorColor = Color.DarkGray,
                focusedLabelColor = Color.DarkGray,
                unfocusedLabelColor = Color.DarkGray,
                unfocusedIndicatorColor = Color.DarkGray,
                backgroundColor = Color.White,
                textColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            enabled = value.isNotBlank(),
            onClick = {
                viewModel.createChat(value)
                nav.navigate("home")
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Green,
                contentColor = Color.Black
            )
        ){
            Text(
                text = "Buscar e Conectar",
                style = MaterialTheme.typography.body1
                    .copy(color = Color.Black, fontWeight = FontWeight.Bold)
            )
        }
    }
}