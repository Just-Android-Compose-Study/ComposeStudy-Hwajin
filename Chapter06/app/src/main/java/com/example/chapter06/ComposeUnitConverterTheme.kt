package com.example.chapter06

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

val AndroidGreen = Color(0xFF3DDC84)
val AndroidGreenDark = Color(0xFF20B261)
val AndroidGreenLight = Color(0xFFBCF5A9)
val Orange = Color(0xFFFFA500)
val OrangeDark = Color(0xFFCC8400)

private val DarkColorPalette = darkColorScheme(
    primary = AndroidGreenDark,
    secondary = OrangeDark,
)

private val LightColorPalette = lightColorScheme(
    primary = AndroidGreen,
    secondary = Orange,
    surface = AndroidGreen, // top app bar 색상
    secondaryContainer = AndroidGreenLight // bottom navigation bar 색상
)

@Composable
fun ComposeUnitConverterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // 다크모드인지 검사
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }
    val view = LocalView.current

    if (!view.isInEditMode) {
        /* getting the current window by tapping into the Activity */
        val currentWindow = (view.context as? Activity)?.window
            ?: throw Exception("Not in an activity - unable to get Window reference")

        SideEffect {
            currentWindow.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars =
                darkTheme
        }
    }

    MaterialTheme( // 머티리얼 테마의 메인 진입점
        // 커스텀 색상, 모양, 텍스트 스타일을 매개변수로 받음
        colorScheme = colorScheme, // material2에서는 colors
//        typography = Typography(button = TextStyle(fontSize = 24.sp)),
//        shapes = Shapes(small = CutCornerShape(8.dp)),
        content = content
    )
}

@Composable
@Preview
fun MaterialThemeDemo() {
    MaterialTheme(
        typography = Typography(
            displayLarge = TextStyle(color = Color.Red)
        )
    ) {
        Row {
            Text(
                text = "Hello",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.width(2.dp))
            MaterialTheme( // 부모 테마 재정의
                typography = Typography(
                    displayLarge = TextStyle(color = Color.Blue)
                )
            ) {
                Text(
                    text = "Compose",
                    style = MaterialTheme.typography.displayLarge
                )
            }
        }
    }
}