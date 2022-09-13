package br.com.whatsapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.whatsapp2.presentation.chat.ChatScreen
import br.com.whatsapp2.presentation.home.HomeScreen
import br.com.whatsapp2.presentation.login.LoginScreen
import br.com.whatsapp2.presentation.login.LoginVMFactory
import br.com.whatsapp2.presentation.login.LoginViewModel
import br.com.whatsapp2.presentation.newchat.NewChatScreen
import br.com.whatsapp2.presentation.newgroup.NewGroupScreen
import br.com.whatsapp2.presentation.login.signup.SignUpScreen
import br.com.whatsapp2.ui.theme.WhatsApp2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginViewModel: LoginViewModel by viewModels{
            LoginVMFactory(
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.userDao()
            )
        }

        setContent {
            WhatsApp2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    WhatsApp2(
                        loginViewModel
                    )
                }
            }
        }
    }
}


@Composable
fun WhatsApp2(
    loginViewModel: LoginViewModel
){
    val navController = rememberNavController()
    Scaffold() {
        NavHost(navController = navController, startDestination = "login"){
            composable(route = "login"){
                LoginScreen(
                    navController,
                    loginViewModel
                )
            }
            composable(route = "signup"){
                SignUpScreen(
                    navController,
                    loginViewModel
                )
            }
            composable(route = "home"){
                HomeScreen(
                    navController,
                    mutableListOf()
                )
            }
            composable(
                route = "newchat/{name}",
                arguments = listOf(
                    navArgument("name"){
                        defaultValue=""
                        type= NavType.StringType
                    }
                )
            ){
                NewChatScreen(
                    navController
                )
            }
            composable(
                route = "newgroup/{name}",
                arguments = listOf(
                    navArgument("name"){
                        defaultValue=""
                        type= NavType.StringType
                    }
                )
            ){
                NewGroupScreen(
                    navController
                )
            }
            composable(
                route = "chat/{id}",
                arguments = listOf(
                    navArgument("id"){
                        defaultValue=0
                        type= NavType.IntType
                    }
                )
            ){
                ChatScreen(nav = navController)
            }
        }
    }
}
