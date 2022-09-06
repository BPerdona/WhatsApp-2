package br.com.whatsapp2.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.whatsapp2.R


@Composable
fun LoginScreen(
    nav: NavController
){
    var username by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var passwordVisible by remember { mutableStateOf(false)}
    Scaffold() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.size(200.dp))
            Text(
                text = "Bem vindo ao WhatsApp 2",
                style = MaterialTheme.typography.h5.copy(
                    color = Color.Green,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.size(30.dp))
            OutlinedTextField(
                value = username,
                onValueChange = {username=it},
                label = { Text(text = "Username")},
                colors = TextFieldDefaults.textFieldColors(
                    focusedLabelColor = Color.Green,
                    focusedIndicatorColor = Color.Green
                )
            )
            Spacer(modifier = Modifier.size(5.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {password=it},
                label = { Text(text = "Password")},
                colors = TextFieldDefaults.textFieldColors(
                    focusedLabelColor = Color.Green,
                    focusedIndicatorColor = Color.Green
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if(passwordVisible)
                        painterResource(id = R.drawable.visibility_on)
                    else
                        painterResource(id = R.drawable.visible_off)
                    IconButton(onClick = {passwordVisible = !passwordVisible}) {
                        Icon(
                            painter = image,
                            "Visibility Icon",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                onClick = {
                          nav.navigate("home")
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Green,
                    contentColor = Color.Black
                )
            ){
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.body1
                        .copy(color = Color.Black, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}