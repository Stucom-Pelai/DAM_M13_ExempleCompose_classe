package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()

        }
    }
}
@Preview()
@Composable
fun PreviewApp(){
    App()
}
@Composable
fun App(){
    var whatShow  by remember {
        mutableStateOf<String>("greeting")
    }
    Column(modifier = Modifier.fillMaxWidth(1f)) {
        Row(modifier=Modifier.fillMaxWidth(1f)) {
//            Button(onClick = {
//                whatShow="greeting"
//            }) {
//                Text(text = "SHOW GREETING")
//            }
            Button(onClick = {
                whatShow="botones"
            }){
                Text(text = "SHOW BOTONES")
            }
        }
        if(whatShow=="greeting"){
            Greeting(
                name = "Android", modifier = Modifier.fillMaxWidth(1f)
            )
        }else{
            Botones(modifier=Modifier.fillMaxWidth(1f), {
                whatShow="greeting"
            })
        }
    }
}
@Composable
fun Botones(modifier: Modifier=Modifier, funPadre:()->Unit){

    Column(modifier=modifier) {
        var mostraImatges by remember { mutableStateOf<Boolean>(false)}
        Button(onClick = {
            mostraImatges=!mostraImatges
            Log.d("exemple","mostraImatges $mostraImatges")
        }
           ) {
            Text(text = stringResource(id = R.string.boto1),
                modifier = Modifier.background(color = colorResource(id = R.color.button1)))
        }
        Button(onClick = {
            funPadre()
        }) {
            Text(text = "SHOW GREETING")
        }

//        if(mostraImatges) {
//            for (k in 0..10) {
//                Image(
//                    painter = painterResource(id = R.drawable.kotlin_logo),
//                    contentDescription = "logo kotlin"
//                )
//            }
//        }
    var llistaImatges:List<String> = List(10){"imatge1"}
    LazyColumn {
        items(llistaImatges){
            item->
            ImatgeColumna()
        }
    }
    }

}
@Composable
fun ImatgeColumna(){
    Image(
                    painter = painterResource(id = R.drawable.kotlin_logo),
                    contentDescription = "logo kotlin"
                )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var textInput by remember {
        mutableStateOf<String>("")
    }
    Column(modifier=modifier) {
        TextField(value = textInput, onValueChange = {
            textInput=it
        })
        Text(
            text = "Hello $name!",
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .background(color = Color.Yellow)
                .fillMaxWidth()
        )
        Text(text = "Segon text", color = Color.Magenta,
            modifier = Modifier
                .background(color = Color.Green)
                .fillMaxWidth()
                .fillMaxHeight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}