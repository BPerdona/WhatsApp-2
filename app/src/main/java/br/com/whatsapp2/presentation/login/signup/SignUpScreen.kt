package br.com.whatsapp2.presentation.login.signup

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.whatsapp2.R
import br.com.whatsapp2.presentation.login.LoginViewModel

@Composable
fun SignUpScreen(
    nav: NavController,
    viewModel: LoginViewModel
){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var signError by remember { mutableStateOf(true) }

    Scaffold() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF111b21)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.size(50.dp))
            Text(
                text = "Bem vindo ao WhatsApp 2",
                style = MaterialTheme.typography.h5.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.size(80.dp))
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.h5.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.size(10.dp))
            if(!signError)
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 67.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Error! Try Again.",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Start
                    )
                }
            OutlinedTextField(
                value = username,
                onValueChange = {username=it},
                singleLine = true,
                label = { Text(text = "Username") },
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
            Spacer(modifier = Modifier.size(5.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {password=it},
                singleLine = true,
                label = { Text(text = "Password") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedLabelColor = Color.LightGray,
                    focusedIndicatorColor = Color(0xFF005c4b),
                    unfocusedIndicatorColor = Color(0xFF005c4b),
                    unfocusedLabelColor = Color.LightGray,
                    backgroundColor = Color(0xFF2a3942),
                    textColor = Color.White,
                    cursorColor = Color(0xFF005c4b)
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
                            tint = Color.LightGray
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.size(30.dp))
            Button(
                enabled = username.isNotBlank() && password.isNotBlank(),
                onClick = {
                    signError = viewModel.signUpUser(username, password)
                    if(signError){
                        nav.popBackStack()
                        nav.navigate("login")
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF005c4b)
                )
            ){
                Text(
                    text = "Confirm",
                    style = MaterialTheme.typography.body1
                        .copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}