package com.example.chapter09

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import com.example.chapter09.databinding.LayoutBinding

const val KEY = "key"
class MainActivity : ComponentActivity() {
    private lateinit var binding: LayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: Chapter9ViewModel by viewModels()
        viewModel.setSliderValue(intent.getFloatExtra(KEY, 0F))
        binding = LayoutBinding.inflate(layoutInflater) // 컴포넌트 트리의 루트를 나타내는 LayoutBinding.root의 인스턴스를 반환
        setContentView(binding.root)
        viewModel.sliderValue.observe(this) {
            // sliderValue에 저장된 값이 변경되면 호출
            binding.slider.value = it // 갱신 시 슬라이더 핸들 위치가 변경
        }
        // 슬라이더 핸들을 드래그하면 호출
        binding.slider.addOnChangeListener { _, value, _ -> viewModel.setSliderValue(value) } // ViewModel을 갱신
        // ComposeView
        // : 클래식 레이아웃에서 컴포저블을 사용할 수 있도록
        // AbstractComposeView 확장. 부모 뷰로 ViewGroup을 가짐
        binding.composeView.run {
            // setViewCompositionStrategy
            // : 뷰 내부 구성 처리를 관리하는 방법 구성
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow) // 뷰가 윈도우에서 분리될 때마다 구성 처리
            setContent { // 뷰의 콘텐츠 설정
                val sliderValue = viewModel.sliderValue.observeAsState()
                sliderValue.value?.let {
                    ComposeDemo(it) {
                        val i = Intent(
                            context,
                            ComposeActivity::class.java
                        )
                        i.putExtra(KEY, it)
                        startActivity(i)
                    }
                }
            }
        }
    }
}

@Composable
fun ComposeDemo(value: Float, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value.toString()
            )
        }
        Button(
            onClick = onClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "ComposeActivity")
        }
    }
}