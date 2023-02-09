package com.example.chapter01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hello()
        }
    }
}

@Composable
fun Welcome() {
    Text(
        text = stringResource(id = R.string.welcome),
        style = MaterialTheme.typography.subtitle1
    )
}

@Composable
fun Greeting(name: String) {
    Text(
        text = stringResource(id = R.string.hello, name), // name에 실제 문구로 교체
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.subtitle1
    )
}

@Composable
fun TextAndButton(name: MutableState<String>, nameEntered: MutableState<Boolean>) { // MutableState: 객체를 변경할 수 있는 값
    Row(modifier = Modifier.padding(top = 8.dp)) {
        TextField( // edittext
            value = name.value,
            onValueChange = {// 텍스트 변경이 발생한다면
                name.value = it
            },
            placeholder = { // hint
                Text(text = stringResource(id = R.string.hint))
            },
            modifier = Modifier
                .alignByBaseline()
                .weight(1.0F),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false, // 자동 수정 기능 - 오타나면 바뀜 hellk -> hello
                capitalization = KeyboardCapitalization.Words // 대문자 여부
            ),
            keyboardActions = KeyboardActions(onAny = {
                nameEntered.value = true
            })
        )
        Button(
            modifier = Modifier
                .alignByBaseline() // 버튼의 기준선을 텍스트 입력 필드와 맞추기 위함
                .padding(8.dp),
            onClick = {
                nameEntered.value = true
            }
        ) {
            Text(text = stringResource(id = R.string.done))
        }
    }
}

@Composable
fun Hello() {
    val name = remember { mutableStateOf("") }
    val nameEntered = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            // .background(Color.Blue) // 영역 확인을 위해 추가
            .fillMaxSize() // 세로 영역이 화면 최대로 늘어남
            .padding(16.dp),
        contentAlignment = Alignment.Center // 세로 중앙정렬
    ) {
        if (nameEntered.value) {
            Greeting(name.value)
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Welcome()
                TextAndButton(name, nameEntered)
            }
        }
    }
}
