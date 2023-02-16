package com.example.chapter03

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
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
                    TextWithWarning1("", Modifier.background(Color.Blue)) {
                        Log.d("TextWithWarning1", "nullable한 parameter가 먼저 오게 되면, Composable 함수를 사용할 때 굳이 정의하지 않아도 되는 parameter값의 초기화를 강제받게 된다.")
                        // default값이 있어 null이 가능한 매개변수 name
                        // name 매개변수 없이 modifier를 정의할 수 없기 때문에 name값의 초기화가 무조건 진행되어야 함
                        // TextWithWarning1(Modifier.background(Color.Blue)) => 에러
                    }

                    //ColoredTextDemo("hi")
                    //ShortColoredTextDemo("hi")

                    // OrderDemo()

                    // drawCrossLine()
                    /*
                    Text(
                        text = "Hello Compose",
                        modifier = Modifier
                            .fillMaxSize()
                            .drawCrossLine(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displayLarge
                    )
                     */

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
fun TextWithWarning1(
    name: String = "Default",
    modifier: Modifier = Modifier,
    callback: () -> Unit
) {
    Text(text = "TextWithWarning1! $name", modifier = modifier
        .background(Color.Yellow)
        .clickable { callback.invoke() })
}
@Composable
// Warning 이유: Modifier parameter should be named modifier
fun TextWithWarning2(test: Modifier = Modifier, name: String = "", callback: () -> Unit) {
    Text(text = "TextWithWarning2 $name!", modifier = test
        .background(Color.Yellow)
        .clickable { callback.invoke() })
}

@Composable
// 부모 Composable 함수에서 NonNull인 Modifier를 맨 앞으로 가져오는 것이 경제적
fun TextWithoutWarning(
    modifier: Modifier = Modifier,
    buttonModifier: Modifier,
    name: String = "",
    callback: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "TextWithoutWarning $name!", modifier = modifier
            .padding(10.dp) // margin concept
            .background(Color.Yellow)
            .padding(10.dp) // real padding
            .clickable { callback.invoke() })

        val context = LocalContext.current
        Button(
            modifier = buttonModifier.clickable {
                Toast.makeText(context, "버튼에 clickable을 넣으면?", Toast.LENGTH_SHORT).show()
            },
            onClick = { Toast.makeText(context, "버튼 클릭됨", Toast.LENGTH_SHORT).show() }) {
            Text("버튼")
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

@Composable
fun OrderDemo() {
    var color by remember { mutableStateOf(Color.Blue) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            /*
            여기에 쓴다면 클릭 이벤트가 padding 영역에도 적용됨
            .clickable {
                color = if (color == Color.Blue)
                    Color.Red
                else
                    Color.Blue
            }
            */
            .padding(32.dp)
            .border(BorderStroke(width = 2.dp, color = color))
            //.padding(32.dp) // 여기에 쓴다면 border에는 padding이 적용되지 않고 background에는 padding이 적용
            .background(Color.LightGray)
            .clickable {
                color = if (color == Color.Blue)
                    Color.Red
                else
                    Color.Blue
            }
    )
}

fun Modifier.drawCrossLine() = then(
    object : DrawModifier {
        override fun ContentDrawScope.draw() {
            //drawContent() // 여기에 쓰면 text가 라인 아래에 위치
            drawLine(
                color = Color.Blue,
                start = Offset(0F, 0F),
                end = Offset(size.width - 1, size.height - 1),
                strokeWidth = 20F
            )
            drawLine(
                color = Color.Yellow,
                start = Offset(0F, size.height - 1),
                end = Offset(size.width - 1, 0F),
                strokeWidth = 10F
            )
            drawContent() // text가 라인 위에 위치
        }
    }
)
