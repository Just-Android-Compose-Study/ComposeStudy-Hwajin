package com.example.chapter04

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    /* @없으면 에러가 나타남. -> 실험용 foundation API를 사용했기 때문에
    @ExperimentalComposeUiApi : 실험적 API. 모든 사용자가 명시적으로 사용하도록 선택.
    @OptIn(ExperimentalComposeUiApi::class) : 실험용 API를 사용하도록 선택.
     */
    // @ExperimentalComposeUiApi
    //@OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //(1) Constraint 적용 전
            //PredefinedLayoutsDemo()

            // (2) ConstraintLayoutDemo()
            CustomLayoutDemo()
        }
    }
}

// (1)
@Composable
@Preview
fun PredefinedLayoutsDemo() {
    val red = remember { mutableStateOf(true) }
    val green = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            //.fillMaxSize()
            .padding(16.dp)
            .background(Color.Cyan)
    ) {
//        Text(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Blue),
//            text = "이런"
//        )
        CheckboxWithLabel(
            label = stringResource(id = R.string.red),
            state = red
        )
        CheckboxWithLabel(
            label = stringResource(id = R.string.green),
            state = green
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .background(Color.Gray)
        ) {
            if (red.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.5f) // fillMaxSize의 기본은 1. 0.5f = 0.5 비율
                        .background(Color.Red)
                )
            }
            if (green.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.5f)
                        .padding(32.dp)
                        .background(Color.Green)
                )
            }
        }
    }
}

@Composable
fun CheckboxWithLabel(label: String, state: MutableState<Boolean>) { // MutableState<Boolean>: 값 변경 시 다른 컴포저블 함수를 재구성해야 함
    Row(
        modifier = Modifier.clickable {
            state.value = !state.value
        }, verticalAlignment = Alignment.CenterVertically // 없으면 checkbox와 text 라인이 맞지 않음
    ) {
        // Checkbox : 현재 상태 (checked)와 체크박스 선택 시 호출되는 람다 표현식(onCheckedChange)을 받음
        Checkbox(
            checked = state.value,
            onCheckedChange = {
                state.value = it
            }
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

//(2)
//@ExperimentalComposeUiApi // 사용하면 ConstraintLayoutDemo를 호출할 때 ExperimentalComposeUiApi를 호출 필수
@OptIn(ExperimentalComposeUiApi::class) // 사용하면 ConstraintLayoutDemo를 호출할 때 ExperimentalComposeUiApi를 호출은 선택
@Composable
@Preview
fun ConstraintLayoutDemo() {
    val red = remember { mutableStateOf(true) }
    val green = remember { mutableStateOf(true) }
    val blue = remember { mutableStateOf(true) }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (cbRed, cbGreen, cbBlue, boxRed, boxGreen, boxBlue) = createRefs()
        CheckboxWithLabel2(
            label = stringResource(id = R.string.red),
            state = red,
            modifier = Modifier.constrainAs(cbRed) {
                top.linkTo(parent.top)
            }
        )
        CheckboxWithLabel2(
            label = stringResource(id = R.string.green),
            state = green,
            modifier = Modifier.constrainAs(cbGreen) {
                top.linkTo(cbRed.bottom)
            }
        )
        CheckboxWithLabel2(
            label = stringResource(id = R.string.blue),
            state = blue,
            modifier = Modifier.constrainAs(cbBlue) {
                top.linkTo(cbGreen.bottom)
            }
        )
        if (red.value) {
            Box(
                modifier = Modifier
                    .background(Color.Red)
                    .constrainAs(boxRed) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(cbBlue.bottom, margin = 16.dp)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )
        }
        if (green.value) {
            Box(
                modifier = Modifier
                    .background(Color.Green)
                    .constrainAs(boxGreen) {
                        start.linkTo(parent.start, margin = 32.dp)
                        end.linkTo(parent.end, margin = 32.dp)
                        top.linkTo(cbBlue.bottom, margin = (16 + 32).dp)
                        bottom.linkTo(parent.bottom, margin = 32.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )
        }
        if (blue.value) {
            Box(
                modifier = Modifier
                    .background(Color.Blue)
                    .constrainAs(boxBlue) {
                        start.linkTo(parent.start, margin = 64.dp)
                        end.linkTo(parent.end, margin = 64.dp)
                        top.linkTo(cbBlue.bottom, margin = (16 + 64).dp)
                        bottom.linkTo(parent.bottom, margin = 64.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun CheckboxWithLabel2(
    label: String,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.clickable {
        state.value = !state.value
    }) {
        val (checkbox, text) = createRefs() // 자신과 관련된 참조를
        Checkbox(
            checked = state.value,
            onCheckedChange = {
                state.value = it
            },
            modifier = Modifier.constrainAs(checkbox) { // 제약 조건은 constrainAs() 변경자 사용
            }
        )
        Text(
            text = label,
            modifier = Modifier.constrainAs(text) {
                // 다른 컴포저블 위치와 연결되는 위치를 정의하는 앵커: start, top, bottom...
                start.linkTo(checkbox.end, margin = 8.dp)
                top.linkTo(checkbox.top)
                bottom.linkTo(checkbox.bottom)
            }
        )
    }
}

// (3) 커스텀 레이아웃
@Composable
@Preview
fun CustomLayoutDemo() {
    SimpleFlexBox {
        for (i in 0..42) {
            ColoredBox()
        }
    }
}

@Composable
fun ColoredBox() {
    Box(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color.Black
            )
            .background(randomColor())
            .width((40 * randomInt123()).dp)
            .height((10 * randomInt123()).dp)
    )
}

@Composable
fun SimpleFlexBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = simpleFlexboxMeasurePolicy()
    )
}

private fun simpleFlexboxMeasurePolicy(): MeasurePolicy =
    MeasurePolicy { measurables, constraints -> // measurables: 자식, constraints: 부모
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        layout(
            constraints.maxWidth,
            constraints.maxHeight
        ) {
            var yPos = 0
            var xPos = 0
            var maxY = 0
            placeables.forEach { placeable ->
                // 부모의 maxWidth보다 xPos + 자식 width가 더 넓으면
                // => 추가해야 하는 도형이 끝나는 x좌표가 화면 밖에 있다면
                if (xPos + placeable.width >
                    constraints.maxWidth // 부모의 maxWidth(화면 상의 너비)
                ) {
                    xPos = 0 // 다음 줄 시작에 추가해야 하니까 xPos은 0
                    yPos += maxY // yPos은 현재 줄의 최대 y값을 더 한 값으로
                    maxY = 0
                }
                placeable.placeRelative(
                    x = xPos,
                    y = yPos
                )
                xPos += placeable.width // 기존 값에서 자식 width을 더한 값이 xPos
                if (maxY < placeable.height) { // maxY값이 자식의 height값보다 작다면 => 현재 동일한 라인에 있는 box들 중에서 자식 height만큼 넓지 않다면
                    maxY = placeable.height // maxY값은 자식 height값으로 변경
                }
            }
        }
    }

private fun randomInt123() = Random.nextInt(1, 4)

private fun randomColor() = when (randomInt123()) {
    1 -> Color.Red
    2 -> Color.Green
    else -> Color.Blue
}
