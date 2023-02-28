package com.example.chapter05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chapter5.R
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // FlowOfEventsDemo()
            ViewModelDemo()
        }
    }
}

class MyViewModel : ViewModel() {

    private val _text: MutableLiveData<String> =
        MutableLiveData<String>("안녕3")

    val text: LiveData<String>
        get() = _text

    fun setText(value: String) {
        _text.value = value
    }
}

/**
    remember: 내부 상태를 생성. remember{}를 포함하는 컴포저블 함수는 상태를 가짐
 */
@Composable
@Preview
fun SimpleStateDemo1() {
    val num = remember { mutableStateOf(Random.nextInt(0, 10)) }
    Text(text = num.value.toString()) // num은 val이지만 변경할 수 있는 값 홀더의 참조를 갖고 있기 때문에 값 변경 가능
}

@Composable
@Preview
fun SimpleStateDemo2() {
    val num by remember { mutableStateOf(Random.nextInt(0, 10)) } // by를 사용해 상태 자신을 num에 할당하지 않고 값을 저장
    Text(text = num.toString())
}

// (2)
@Composable
@Preview
fun FlowOfEventsDemo() {
    val strCelsius = stringResource(id = R.string.celsius)
    val strFahrenheit = stringResource(id = R.string.fahrenheit)
    val temperature = remember { mutableStateOf("") }
    val scale = remember { mutableStateOf(R.string.celsius) }
    var convertedTemperature by remember { mutableStateOf(Float.NaN) }
    val calc = {
        val temp = temperature.value.toFloat()
        convertedTemperature = if (scale.value == R.string.celsius)
            (temp * 1.8F) + 32F
        else
            (temp - 32F) / 1.8F
    }
    val result = remember(convertedTemperature) { // convertedTemperature가 변경되면 result 재평가
        if (convertedTemperature.isNaN()) // Returns true if the specified number is a Not-a-Number (NaN) value, false otherwise.
            ""
        else
            "${convertedTemperature}${
                if (scale.value == R.string.celsius)
                    strFahrenheit
                else strCelsius
            }"
    }
    val enabled = temperature.value.isNotBlank()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TemperatureTextField(
            temperature = temperature,
            modifier = Modifier.padding(bottom = 16.dp),
            callback = calc
        )
        TemperatureScaleButtonGroup(
            selected = scale,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = calc,
            enabled = enabled
        ) {
            Text(text = stringResource(id = R.string.convert))
        }
        if (result.isNotEmpty()) {
            Text(
                text = result,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureTextField(
    temperature: MutableState<String>,
    modifier: Modifier = Modifier,
    callback: () -> Unit
) {
    TextField(
        value = temperature.value,
        onValueChange = {
            temperature.value = it // 텍스트의 변경사항을 MutableState<String>에 저장
        },
        placeholder = {
            Text(text = stringResource(id = R.string.placeholder))
        },
        modifier = modifier,
        keyboardActions = KeyboardActions(onAny = { // onAny: do when ANY of ime actions is emitted
            callback()
        }),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}

@Composable
fun TemperatureScaleButtonGroup( // 라디오버튼 클릭 동작이 TemperatureRadioButton이 아닌 부모로 전달 = 버블업
    selected: MutableState<Int>, // resId를 변경할 수 있는 상태 타입
    modifier: Modifier = Modifier
) {
    val sel = selected.value
    val onClick = { resId: Int -> selected.value = resId }
    Row(modifier = modifier) {
        TemperatureRadioButton(
            selected = sel == R.string.celsius,
            resId = R.string.celsius,
            onClick = onClick
        )
        TemperatureRadioButton(
            selected = sel == R.string.fahrenheit,
            resId = R.string.fahrenheit,
            onClick = onClick,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun TemperatureRadioButton( // 섭씨, 화씨 온도 버튼
    selected: Boolean,
    resId: Int,
    onClick: (Int) -> Unit, // onClick 매개변수로 람다 표현식을 전달받음
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        RadioButton(
            selected = selected,
            onClick = {
                onClick(resId)
            }
        )
        Text(
            text = stringResource(resId),
            modifier = Modifier
                .padding(start = 8.dp)
        )
    }
}

//(3)
@Composable
@Preview
fun ViewModelDemo() {
    val viewModel: MyViewModel = viewModel() // viewModel 컴포저블 호출
    val state1 = remember { // 회전 시 값 유지 x
        mutableStateOf("안녕1")
    }
    val state2 = rememberSaveable { // 회전 시 값 유지
        mutableStateOf("안녕2")
    }
    // 회전 시 값 유지
    val state3 = viewModel.text.observeAsState() // livedata를 상태로 변환해 사용
    state3.value?.let {
        Column(modifier = Modifier.fillMaxWidth()) {
            MyTextField(state1) { state1.value = it }
            MyTextField(state2) { state2.value = it }
            MyTextField(state3) {
                viewModel.setText(it) // viewModel 클래스에 있는 상태에 변경 사항을 반영
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    value: State<String?>, // ?인 이유는 observeAsState()의 반환 타입이 State<T?>라서
    onValueChange: (String) -> Unit
) {
    value.value?.let {
        TextField(
            value = it,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}