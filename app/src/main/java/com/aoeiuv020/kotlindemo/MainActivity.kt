package com.aoeiuv020.kotlindemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                    Greeting(
                        name = AndroidLibraryClass.javaLibraryBean.name + textFromAssets,
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
        text = "${BuildConfig.AUTHOR_NAME}: $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinDemoTheme {
        Greeting("Android")
    }
}