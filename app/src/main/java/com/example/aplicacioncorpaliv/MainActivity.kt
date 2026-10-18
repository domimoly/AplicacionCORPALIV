package com.example.aplicacioncorpaliv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.aplicacioncorpaliv.ui.componentes.NuevoMenu
import com.example.aplicacioncorpaliv.ui.theme.AplicacionCORPALIVTheme
import com.example.aplicacioncorpaliv.ui.theme.*

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplicacionCORPALIVTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar={
                        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Green,
                            titleContentColor = Negro
                        ),title={ Text(text="CORPALIV") },
                            actions= {
                                var isMenuOpened by remember { mutableStateOf(false) }
                                IconButton(onClick = { isMenuOpened = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Opciones Menu"
                                    )
                                    NuevoMenu(isExpanded = isMenuOpened, onItemClick = {}) {
                                        isMenuOpened=false
                                    }
                                }
                            })
                    }) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AplicacionCORPALIVTheme {
        Greeting("Android")
    }
}