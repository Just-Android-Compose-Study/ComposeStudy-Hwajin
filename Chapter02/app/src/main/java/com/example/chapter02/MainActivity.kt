package com.example.chapter02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Factorial2()
        }
    }
}

// 컴포저블 함수는 주로 content 매개변수를 전달받는데, 이는 다른 컴포저블 함수
// 보통 content를 생략
@Composable
fun Factorial2() {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(factorialAsString(0)) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = {
            Column {
                Text(
                    modifier = Modifier.clickable {
                        expanded = true
                    },
                    text = text,
                    style = MaterialTheme.typography.h2
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }) {
                    for (n in 0 until 10) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            text = factorialAsString(n)
                        }) {
                            Text("${n.toString()}!")
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun Factorial() {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(factorialAsString(0)) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.clickable {
                    expanded = true // 드롭다운 메뉴가 화면에 즉시 나타남. false하면 클릭해도 아무런 일이 없음
                },
                text = text,
                style = MaterialTheme.typography.h2
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { // 아무것도 선택하지 않고 닫으면
                    expanded = false // true면 드롭박스 이외를 클릭해도 화면이 계속 나타남
                }
            ) {
                for (n in 0 until 10) {
                    DropdownMenuItem(onClick = {
                        expanded = false
                        text = factorialAsString(n)
                    }) {
                        Text("$n!")
                    }
                }
            }
            ButtonDemo()
        }
    }
}

fun factorialAsString(n: Int): String {
    var result = 1L
    for (i in 1..n) {
        result *= i
    }
    return "$n! = $result"
}

@Composable
@Preview
fun ButtonDemo() {
    Box {
        Button(
            onClick = {
                println("눌림")
            },
            // enabled = false
        ) {
            Text("여기를 누르세요")
        }
    }
}
