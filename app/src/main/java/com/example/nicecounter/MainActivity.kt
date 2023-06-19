package com.example.nicecounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nicecounter.datastore.StoreCounterValue
import com.example.nicecounter.ui.theme.NiceCounterTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NiceCounterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppPreview()
                }
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    NiceCounterTheme {
        ButtonThing()
    }
}

@Composable
fun ButtonThing() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreCounterValue(context)
    val savedCounter = dataStore.getCounterValue.collectAsState(initial = 0)

    var incrementAmount by remember { mutableStateOf(1) }
    var counterValue by remember { mutableStateOf(0) }
    counterValue = savedCounter.value!!

    fun setCounterValue(value: Int) {
        counterValue = value
        scope.launch {
            dataStore.saveCounterValue(value)
        }
    }

    Box(
        modifier = Modifier
            .padding(12.dp)
    ) {
        Button(
            onClick = { setCounterValue(counterValue + incrementAmount) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(36.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = counterValue.toString(),
                fontSize = 150.sp
            )
        }
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row {
                val selectedColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                val button1Color = remember { Animatable(Color.Transparent) }
                val button2Color = remember { Animatable(Color.Transparent) }

                suspend fun animateButton1(targetColor: Color) {
                    button1Color.animateTo(targetColor, animationSpec = tween(200, easing = LinearEasing))
                }
                suspend fun animateButton2(targetColor: Color) {
                    button2Color.animateTo(targetColor, animationSpec = tween(200, easing = LinearEasing))
                }

                Button(
                    onClick = {
                        if (incrementAmount != 5) {
                            incrementAmount = 5
                            scope.launch { animateButton1(selectedColor) }
                        }
                        else {
                            incrementAmount = 1
                            scope.launch { animateButton1(Color.Transparent) }
                        }
                        scope.launch { animateButton2(Color.Transparent) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = button1Color.value,
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(36.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                ) {
                    Text(text = "5", fontSize = dpToSp(50.dp))
                }
                Button(
                    onClick = {
                        if (incrementAmount != 10) {
                            incrementAmount = 10
                            scope.launch { animateButton2(selectedColor) }
                        }
                        else {
                            incrementAmount = 1
                            scope.launch { animateButton2(Color.Transparent) }
                        }
                        scope.launch { animateButton1(Color.Transparent) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = button2Color.value,
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(36.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                ) {
                    Text(text = "10", fontSize = dpToSp(50.dp))
                }
            }
        }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row {
                Button(
                    onClick = { setCounterValue(0) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.tertiary
                    ),
                    shape = RoundedCornerShape(36.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                ) {
                    Icon(painter = painterResource(id = R.drawable.restart), contentDescription = null)
                }
                Button(
                    onClick = { setCounterValue(counterValue - incrementAmount) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(36.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                ) {
                    Icon(painter = painterResource(id = R.drawable.remove), contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }
