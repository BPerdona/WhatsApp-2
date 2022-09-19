package br.com.whatsapp2.presentation.newgroup

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
fun NewGroupScreen(
    nav: NavController,
    viewModel: NewGroupViewModel
){
    var value by remember{ mutableStateOf("") }
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
                text = "Novo Grupo",
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
                    text = "Nome do Grupo",
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
            onClick = {
                viewModel.createEntryGroup(value)
                nav.navigate("home")
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF005c4b),
            )
        ){
            Text(
                text = "Entrar no grupo",
                style = MaterialTheme.typography.body1
                    .copy(color = Color.White, fontWeight = FontWeight.Bold)
            )
        }
    }
}