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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.whatsapp2.presentation.chat.ChatScreen
import br.com.whatsapp2.presentation.chat.ChatVMFactory
import br.com.whatsapp2.presentation.chat.ChatViewModel
import br.com.whatsapp2.presentation.group.GroupScreen
import br.com.whatsapp2.presentation.group.GroupVMFactory
import br.com.whatsapp2.presentation.group.GroupViewModel
import br.com.whatsapp2.presentation.home.*
import br.com.whatsapp2.presentation.login.LoginScreen
import br.com.whatsapp2.presentation.login.LoginVMFactory
import br.com.whatsapp2.presentation.login.LoginViewModel
import br.com.whatsapp2.presentation.newchat.NewChatScreen
import br.com.whatsapp2.presentation.newgroup.NewGroupScreen
import br.com.whatsapp2.presentation.login.signup.SignUpScreen
import br.com.whatsapp2.presentation.newchat.NewChatVMFactory
import br.com.whatsapp2.presentation.newchat.NewChatViewModel
import br.com.whatsapp2.presentation.newgroup.NewGroupVMFactory
import br.com.whatsapp2.presentation.newgroup.NewGroupViewModel
import br.com.whatsapp2.ui.theme.WhatsApp2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginViewModel: LoginViewModel by viewModels{
            LoginVMFactory(
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.userDao()
            )
        }

        val homeViewModel: HomeViewModel by viewModels{
            HomeVMFactory(
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.chatDao(),
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.groupDao()
            )
        }

        val newChatViewModel: NewChatViewModel by viewModels{
            NewChatVMFactory(
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.chatDao()
            )
        }

        val chatViewModel: ChatViewModel by viewModels{
            ChatVMFactory(
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.chatDao(),
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.messageDao()
            )
        }

        val groupViewModel: GroupViewModel by viewModels{
            GroupVMFactory(
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.groupDao(),
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.messageGroupDao()
            )
        }

        val consumeViewModel: ConsumeViewModel by viewModels{
            ConsumeVMFactory(
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.chatDao(),
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.groupDao(),
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.messageDao(),
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.messageGroupDao()
            )
        }

        val newGroupViewModel: NewGroupViewModel by viewModels{
            NewGroupVMFactory(
                (this.applicationContext as WhatsAppApplication).whatsAppDatabase.groupDao()
            )
        }

        consumeViewModel.startConsume()
        setContent {
            WhatsApp2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    WhatsApp2(
                        loginViewModel,
                        homeViewModel,
                        newChatViewModel,
                        chatViewModel,
                        consumeViewModel,
                        newGroupViewModel,
                        groupViewModel
                    )
                }
            }
        }
    }
}


@Composable
fun WhatsApp2(
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    newChatViewModel: NewChatViewModel,
    chatViewModel: ChatViewModel,
    consumeViewModel: ConsumeViewModel,
    newGroupViewModel: NewGroupViewModel,
    groupViewModel: GroupViewModel
){
    val navController = rememberNavController()
    val chats = consumeViewModel.chats.observeAsState()
    val groups = consumeViewModel.groups.observeAsState()
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
                homeViewModel.user = loginViewModel.loggedUser
                consumeViewModel.user = loginViewModel.loggedUser
                homeViewModel.getChats(loginViewModel.loggedUser.pk)
                HomeScreen(
                    navController,
                    homeViewModel
                )
            }
            composable(route = "newchat"){
                newChatViewModel.setUserConst(consumeViewModel.getChatLastIndex(), loginViewModel.loggedUser.pk)
                NewChatScreen(
                    navController,
                    newChatViewModel
                )
            }
            composable(route = "newgroup"){
                newGroupViewModel.setUserConst(consumeViewModel.getChatLastIndex(), loginViewModel.loggedUser)
                NewGroupScreen(
                    navController,
                    newGroupViewModel
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
                chatViewModel.userName = loginViewModel.loggedUser.username
                chatViewModel.getChatMessages(it.arguments?.getInt("id")?:0)
                ChatScreen(chatViewModel)
            }
            composable(
                route = "group/{id}",
                arguments = listOf(
                    navArgument("id"){
                        defaultValue=0
                        type= NavType.IntType
                    }
                )
            ){
                groupViewModel.userName = loginViewModel.loggedUser.username
                groupViewModel.getGroupMessages(it.arguments?.getInt("id")?:0)
                GroupScreen(groupViewModel)
            }
        }
    }
}
