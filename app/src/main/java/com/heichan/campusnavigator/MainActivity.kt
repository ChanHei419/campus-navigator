package com.heichan.campusnavigator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.heichan.campusnavigator.ui.NavigatorScreen
import com.heichan.campusnavigator.ui.theme.CampusNavigatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusNavigatorTheme {
                NavigatorScreen()
            }
        }
    }
}
