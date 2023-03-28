package com.example.chapter09

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import com.example.chapter09.databinding.CustomBinding
import androidx.compose.ui.viewinterop.AndroidViewBinding

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: Chapter9ViewModel by viewModels()
        viewModel.setSliderValue(intent.getFloatExtra(KEY, 0F))
        setContent {
            // ViewIntegrationDemo: 최상위 컴포저블
            // ViewModel과 람다 표현식을 인자로 받음
            ViewIntegrationDemo(viewModel) { // viewModel: 컴포즈와 뷰 계층 구조 사이에 데이터를 공유하는 데 사용
                val i = Intent(
                    this,
                    MainActivity::class.java
                )
                i.putExtra(KEY, viewModel.sliderValue.value) // viewModel의 값 전달
                startActivity(i) // MainActivity 시작
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewIntegrationDemo(viewModel: Chapter9ViewModel, onClick: () -> Unit) {
    val sliderValueState = viewModel.sliderValue.observeAsState()
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title =
            {
                Text("ComposeActivity")
            })
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Slider( // ViewModel에서 현재 값을 얻으며 변경 사항을 다시 ViewModel로 전파
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    viewModel.setSliderValue(it)
                },
                value = sliderValueState.value ?: 0F // 컴포저블 내부에서 판독
            )
            AndroidViewBinding( // AndroidView()와 유사
                modifier = Modifier.fillMaxWidth(),
                // factory: 구성될 뷰 계층 구조를 생성
                factory = CustomBinding::inflate // custom.XML 파일에서 CustomBinding으로 표현되는 레이아웃을 인플레이트한 다음 CustomBinding 타입의 인스턴스를 반환
            ) {
                // update 블록
                // : 레이아웃이 인플레이트된 후 바로 호출
                // : 사야ㅛㅇ 중인 값이 변경될 때에도 호출
                // update는 infalte에 의해 반환된 인스턴스 타입의 확장 함수. ex) CustomBinding
                textView.text = sliderValueState.value.toString()
                button.setOnClickListener {
                    onClick()
                }
            }
        }
    }
}