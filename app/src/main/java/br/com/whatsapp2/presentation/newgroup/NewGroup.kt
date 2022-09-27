package br.com.whatsapp2.presentation.newgroup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.whatsapp2.presentation.home.ConsumeViewModel

@Composable
fun NewGroupScreen(
    nav: NavController,
    viewModel: NewGroupViewModel,
    consume: (String) -> Unit
){
    val search by viewModel.filter.observeAsState()
    val exchenges by viewModel.exchangeList.observeAsState()
    val group by viewModel.groupList.observeAsState()

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
        Spacer(modifier = Modifier.size(4.dp))
        SearchFilter(word = search?:"", onFilterChange = viewModel::updateFilter)
        Spacer(modifier = Modifier.size(4.dp))
        LazyColumn(){
            items(exchenges?: listOf()){
                GroupCard(
                    group = it.name,
                    nav = nav,
                    addChat = viewModel::entryGroup,
                    consume = consume
                )
            }
        }
        if(exchenges?.isEmpty() == true)
            newGroup(nav, viewModel, search?:"|")
    }
}

@Composable
fun SearchFilter(
    word: String,
    onFilterChange: (String) -> Unit
){
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp, top = 3.dp, start = 8.dp, end = 8.dp),
        label = {
            Row(){
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                Text("Search", style = MaterialTheme.typography.body1)
            }
        },
        value = word,
        onValueChange = onFilterChange,
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = Color(0xFFFFFFFF),
            focusedIndicatorColor = Color(0xFF005c4b),
            unfocusedLabelColor = Color(0xFFFFFFFF)
        )
    )
}

@Composable
fun GroupCard(
    group: String,
    nav: NavController,
    addChat :(String)->Unit,
    consume: (String)->Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .size(60.dp)
            .clickable {
                addChat(group)
                consume(group)
                nav.navigate("home")
            },
        backgroundColor = Color(0xFF303c44)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp)
        ){
            Text(
                modifier = Modifier.weight(1f),
                text = group.substring(1),
                style = MaterialTheme.typography.h6.copy(
                    Color.White, fontWeight = FontWeight.Bold
                ),
            )
            Icon(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(32.dp),
                imageVector = Icons.Default.Add,
                contentDescription = "Start new chat",
                tint = Color(0xFF005c4b)
            )
        }
    }
}

@Composable
fun newGroup(
    nav: NavController,
    viewModel: NewGroupViewModel,
    name: String
){
    if(name.isNotEmpty())
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .size(60.dp)
                .clickable {
                    viewModel.createGroup(name)
                    nav.navigate("home")
                },
            backgroundColor = Color(0xFF303c44)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 15.dp)
            ){
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Criar grupo -> $name",
                    style = MaterialTheme.typography.h6.copy(
                        Color.White, fontWeight = FontWeight.Bold
                    ),
                )
                Icon(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(32.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Start new chat",
                    tint = Color(0xFF005c4b)
                )
            }
        }
}