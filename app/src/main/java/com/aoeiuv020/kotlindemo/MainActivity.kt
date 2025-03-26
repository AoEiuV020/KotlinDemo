package com.aoeiuv020.kotlindemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aoeiuv020.androidlibrary.AndroidLibraryClass
import com.aoeiuv020.kotlindemo.ui.theme.KotlinDemoTheme
import org.slf4j.LoggerFactory

class MainActivity : ComponentActivity() {
    private val logger = LoggerFactory.getLogger(MainActivity::class.java)
    private val androidLibraryClass = AndroidLibraryClass()
    override fun onCreate(savedInstanceState: Bundle?) {
        logger.info("onCreate")
        logger.debug("debug")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        androidLibraryClass.init()
        val textFromAssets = assets.open("name.txt").reader().readText()
        setContent {
            KotlinDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        val context = LocalContext.current
                        Button(
                            onClick = {
                                context.startActivity(Intent(context, ListActivity::class.java))
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "Show Traditional RecyclerView")
                        }
                        NumberList()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "${BuildConfig.AUTHOR_NAME}: $name!",
        modifier = modifier
    )
}

@Composable
fun NumberList(modifier: Modifier = Modifier) {
    val numbers = (1..100).toList()
    LazyColumn(modifier = modifier) {
        items(numbers) { number ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    // 点击事件处理
                }
            ) {
                Text(
                    text = number.toString(),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NumberListPreview() {
    KotlinDemoTheme {
        NumberList()
    }
}