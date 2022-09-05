package br.com.whatsapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.whatsapp2.presentation.home.HomeScreen
import br.com.whatsapp2.presentation.login.LoginScreen
import br.com.whatsapp2.ui.theme.WhatsApp2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatsApp2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    WhatsApp2()
                }
            }
        }
    }
}


@Composable
fun WhatsApp2(){
    val navController = rememberNavController()

    Scaffold() {
        NavHost(navController = navController, startDestination = "login"){
            composable(route = "login"){
                LoginScreen(
                    navController
                )
            }
            composable(route = "home"){
                HomeScreen(
                    navController
                )
            }
        }
    }
}
