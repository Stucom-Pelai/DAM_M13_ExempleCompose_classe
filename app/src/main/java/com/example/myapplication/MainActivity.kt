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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class InfoUiState(val textInput:String="", )
class AppViewModel:ViewModel(){
    private val _uiState = MutableStateFlow(InfoUiState())
    val uiState: StateFlow<InfoUiState> get()=_uiState.asStateFlow()
    init {
        _uiState.value=InfoUiState("text escrit")
    }
    fun updateTextInput(textInput:String){
        _uiState.update { it.copy(textInput = textInput) }
    }
    fun getTextInput():String{
        return _uiState.value.textInput
    }
}

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
    val viewModel:AppViewModel=AppViewModel()
     val remoteViewModel:RemoteViewModel = RemoteViewModel()
    var whatShow  by remember {
        mutableStateOf<String>("greeting")
    }
    val navController = rememberNavController()
    Column(modifier = Modifier.fillMaxWidth(1f)) {
        Row(modifier=Modifier.fillMaxWidth(1f)) {
//            Button(onClick = {
//                whatShow="greeting"
//            }) {
//                Text(text = "SHOW GREETING")
//            }
            Button(onClick = {
                navController.navigate("botones")
//                whatShow="botones"
            }){  Text(text = "SHOW BOTONES")            }
            Button(onClick = {navController.navigate("greeting")}){
                Text(text="GREETING")
            }
        }
        NavHost(navController = navController, startDestination = "botones" ){
            composable(route="botones") {
                            Botones(viewModel,modifier=Modifier.fillMaxWidth(1f), remoteViewModel = remoteViewModel) {
                whatShow = "greeting"
            }
            }
            composable(route="greeting") {
                Greeting(
                    name = "Android", modifier = Modifier.fillMaxWidth(1f)
                    , viewModel
                )
            }
        }
//        if(whatShow=="greeting"){
            Greeting(
                name = "Android", modifier = Modifier.fillMaxWidth(1f)
                , viewModel
            )
//        }else{
//            Botones(viewModel,modifier=Modifier.fillMaxWidth(1f)) {
//                whatShow = "greeting"
//            }
//        }
    }
}
@Composable
fun Botones(viewModel: AppViewModel, remoteViewModel: RemoteViewModel, modifier: Modifier = Modifier, funPadre: () -> Unit){
    val viewModel:AppViewModel=viewModel
    val remoteViewModel:RemoteViewModel = remoteViewModel

    Column(modifier=modifier) {
        var mostraImatges by remember { mutableStateOf<Boolean>(false)}
        Text(text = viewModel.getTextInput())
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
        Button(onClick = {
            remoteViewModel.getRemoteNurse()
        }){Text(text="SEND GET")}
        val remoteMessageUiState = remoteViewModel.remoteMessageUiState
        when(remoteMessageUiState){
            is RemoteMessageUiState.Error-> Text("error ")
            is RemoteMessageUiState.Cargant-> Text(" fent la petició")
            is RemoteMessageUiState.Success->{
                Text(text = remoteMessageUiState.remoteMessage.nom)
            }
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
data class Nurse(val nom:String="", val edat:Number)
sealed interface RemoteMessageUiState{
    data class Success(val remoteMessage:Nurse):RemoteMessageUiState
    object Error: RemoteMessageUiState
    object Cargant: RemoteMessageUiState
}
interface RemoteNurseInterface{
    @GET("nurse")
    suspend fun getRemoteNurse():Nurse
}
class RemoteViewModel:ViewModel(){
    var remoteMessageUiState:RemoteMessageUiState by mutableStateOf(RemoteMessageUiState.Cargant)
    private set
    fun getRemoteNurse(){
        viewModelScope.launch {
            remoteMessageUiState=RemoteMessageUiState.Cargant
            try {
                val connexio=Retrofit.Builder().baseUrl("http://10.0.2.2:8080")
                    .addConverterFactory(GsonConverterFactory.create()).build()
                val endPoints = connexio.create(RemoteNurseInterface::class.java)
                val infoNurse= endPoints.getRemoteNurse()
                Log.d("exemple","RESPOSTA ${infoNurse.nom} ${infoNurse.edat}")
                remoteMessageUiState = RemoteMessageUiState.Success(infoNurse)
            }catch (e:Exception){
                Log.d("exemple","ERROR GET ${e.message} ${e.printStackTrace()}")
                remoteMessageUiState=RemoteMessageUiState.Error
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
fun Greeting(name: String, modifier: Modifier = Modifier, viewModel: AppViewModel) {
//    var textInput by remember {
//        mutableStateOf<String>("")
//    }
    val viewModel:AppViewModel=viewModel
    var textInput= viewModel.uiState.collectAsState().value.textInput
    Column(modifier=modifier) {
        TextField(value = viewModel.uiState.collectAsState().value.textInput,
            onValueChange = {
           // textInput=it
            viewModel.updateTextInput(it)
        })
        Text(
            text = "Hello $name! $textInput",
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
    val viewModel:AppViewModel=AppViewModel()
    MyApplicationTheme {
        Greeting("Android", viewModel = viewModel)
    }
}