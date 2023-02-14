package com.example.chapter03

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.chapter03.ui.theme.Chapter03Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Chapter03Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //ColoredTextDemo("hi")
                    //ShortColoredTextDemo("hi")

                    // BoxWithConstraints: Box의 기능을 모두 포함하면서 Layout의 Constraint(최대, 최소 크기값)에 접근할 수 있도록 만들어진 layout
                    BoxWithConstraints(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            /*
                                슬라이더의 너비를 최대 400밀도 독립 픽셀만큼 설정
                                화면이 설정한 밀도보다 작다면 (600으로 설정) 그 너비(화면 너비)를 대신 사용 => 너비가 화면 크기로 꽉참
                             */
                            modifier = Modifier.width(min(600.dp, maxWidth)), // BoxWithConstraints를 사용했기 때문에 maxWidth 사용 가능
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // mutableStateOf 안 Color값이 초기 색상
                            val color = remember { mutableStateOf(Color.Cyan) } // 여기서 상태(시간이 지남에 따라 변하는 앱 데이터 - 색상) 생성
                            ColorPicker(color)
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color.value),
                                text = "#${color.value.toArgb().toUInt().toString(16)}",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineMedium.merge(
                                    TextStyle(
                                        color = color.value.complementary()
                                    )
                                )
                                /* 기존에는 MaterialTheme.typography.h4
                                    M3부터는 이름이 다름
                                    h1 -> displayLarge
                                    h2 -> displayMedium
                                    h3 -> displaySmall
                                    N/A -> headlineLarge
                                    h4 -> headlenMedium
                                    h5 -> headlineSmall
                                    그 외 바뀐 내용은 아래 링크에서 확인
                                    https://developer.android.com/jetpack/compose/themes/material2-material3?hl=ko
                                 */
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColoredTextDemo(
    text: String = "",
    color: Color = Color.Black
) {
    Text(
        text = text,
        style = TextStyle(color = color)
    )
}

@Composable
fun ShortColoredTextDemo(
    text: String = "",
    color: Color = Color.Black
) = Text(
    text = text,
    style = TextStyle(color = color)
)

@Composable
// MutableState<Color>라는 매개변수를 통해 텍스트에게 변화를 알려줌
fun ColorPicker(color: MutableState<Color>) {
    // 색 변수들은 slider의 이동에 따라 0F와 1F 사이가 반환 <- ColorSpaces.Srgb가 기본값이라
    val red = color.value.red
    val green = color.value.green
    val blue = color.value.blue
    Log.d("ColorPicker", red.toString())
    Column {
        Slider(
            value = red,
            // onValueChange: 슬라이더를 드래그하거나 선을 클릭하면 호출 <- 선을 클릭하면 해당 위치로 슬라이더가 이동
            onValueChange = { color.value = Color(it, green, blue)}, // it을 통해 현재 슬라이더의 새로운 색상을 얻음
            /*
                valueRange로 대체 범위를 제공할 수 있음
                0F ~ 1F 사이가 아니라면 에러가 나타남
                0.5f를 최소값으로 설정했기 때문에 슬라이더 가장 왼쪽으로 이동해도 값이 0이 아닌 0.5를 리턴
             */
            valueRange = 0.5f..1f
        )
        Slider(
            value = green,
            onValueChange = { color.value = Color(red, it, blue)}
        )
        Slider(
            value = blue,
            onValueChange = { color.value = Color(red, green, it)}
        )
    }
}

// 보색을 계산해 주는 함수
fun Color.complementary() = Color(
    red = 1F - red,
    green = 1F - green,
    blue = 1F - blue
)