package com.example.megaslot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF0F0C29)) {
                SlotMachine()
            }
        }
    }
}

@Composable
fun SlotMachine() {
    val symbols = listOf("💰", "💎", "7️⃣", "🍒", "🔔", "⭐")
    var balance by remember { mutableStateOf(1000) }
    var reels = remember { mutableStateListOf("🎰", "🎰", "🎰", "🎰", "🎰") }
    var isSpinning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text("$$balance", fontSize = 48.sp, color = Color.Yellow, fontWeight = FontWeight.Bold)

        Row(
            modifier = Modifier.fillMaxWidth().background(Color.Black, RoundedCornerShape(16.dp)).padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            reels.forEach { Text(it, fontSize = 40.sp) }
        }

        Button(
            onClick = {
                if (!isSpinning && balance >= 50) {
                    isSpinning = true
                    balance -= 50
                    scope.launch {
                        repeat(15) {
                            for (i in 0..4) reels[i] = symbols.random()
                            delay(80)
                        }
                        isSpinning = false
                        if (reels[0] == reels[1] && reels[1] == reels[2]) balance += 500
                    }
                }
            },
            modifier = Modifier.size(120.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("SPIN", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // Dugme za AdMob Rewarded Ads
        OutlinedButton(onClick = { balance += 200 }) {
            Text("📺 +$200 (WATCH AD)", color = Color.Cyan)
        }
    }
}
