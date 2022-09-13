package br.com.whatsapp2.presentation.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
fun SignUpScreen(
    nav: NavController
){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    Scaffold() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.size(30.dp))
            Text(
                text = "Bem vindo ao WhatsApp 2",
                style = MaterialTheme.typography.h5.copy(
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.size(150.dp))
            Text(
                text = "Sign Up:",
                style = MaterialTheme.typography.h5.copy(
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.size(30.dp))
            OutlinedTextField(
                value = username,
                onValueChange = {username=it},
                singleLine = true,
                label = { Text(text = "Username") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedLabelColor = Color.DarkGray,
                    focusedIndicatorColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.DarkGray,
                    unfocusedLabelColor = Color.DarkGray,
                    backgroundColor = Color.White,
                    textColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.size(5.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {password=it},
                singleLine = true,
                label = { Text(text = "Password") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedLabelColor = Color.DarkGray,
                    focusedIndicatorColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.DarkGray,
                    unfocusedLabelColor = Color.DarkGray,
                    backgroundColor = Color.White,
                    textColor = Color.Black
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if(passwordVisible)
                        painterResource(id = R.drawable.visible_off)
                    else
                        painterResource(id = R.drawable.visibility_on)
                    IconButton(onClick = {passwordVisible = !passwordVisible}) {
                        Icon(
                            painter = image,
                            "Visibility Icon",
                            modifier = Modifier.size(25.dp),
                            tint = Color.DarkGray
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.size(30.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Green,
                    contentColor = Color.Black
                )
            ){
                Text(
                    text = "Confirm",
                    style = MaterialTheme.typography.body1
                        .copy(color = Color.Black, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}