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
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center,
                text = "Nova conversa",
                color = Color.White,
                style = MaterialTheme.typography.h5.copy(
                    Color.White, fontWeight = FontWeight.Bold
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
                        color = Color.White, fontWeight = FontWeight.Bold
                    )
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor = Color.LightGray,
                focusedIndicatorColor = Color(0xFF005c4b),
                unfocusedIndicatorColor = Color(0xFF005c4b),
                unfocusedLabelColor = Color.LightGray,
                backgroundColor = Color(0xFF2a3942),
                textColor = Color.White,
                cursorColor = Color(0xFF005c4b)
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
                backgroundColor = Color(0xFF005c4b),
            )
        ){
            Text(
                text = "Buscar e Conectar",
                style = MaterialTheme.typography.body1
                    .copy(color = Color.White, fontWeight = FontWeight.Bold)
            )
        }
    }
}