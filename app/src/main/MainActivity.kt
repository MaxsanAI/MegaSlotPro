package com.example.megaslot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SlotMachineApp()
        }
    }
}

@Composable
fun SlotMachineApp() {
    val symbols = listOf("💰", "💎", "7️⃣", "🍒", "🔔", "🍋", "⭐", "🍀")
    var balance by remember { mutableStateOf(5000) }
    var bet by remember { mutableStateOf(100) }
    var isSpinning by remember { mutableStateOf(false) }
    var reels = remember { mutableStateListOf("❓", "❓", "❓", "❓", "❓") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E))))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // --- LOGO / NASLOV ---
        Text(
            text = "MEGA SEGA SLOT",
            fontSize = 42.sp,
            fontWeight = FontWeight.Black,
            color = Color.Cyan,
            modifier = Modifier.shadow(10.dp, CircleShape)
        )

        // --- BALANCE PANEL ---
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, Color.Yellow)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("CASH", color = Color.White, fontSize = 14.sp)
                Text("$$balance", color = Color(0xFF00FF00), fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
        }

        // --- 5x REELS (MAIN MACHINE) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                .border(3.dp, Color.Cyan, RoundedCornerShape(12.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            reels.forEach { symbol ->
                AnimatedContent(
                    targetState = symbol,
                    transitionSpec = { fadeIn() with fadeOut() }
                ) { targetSymbol ->
                    Text(targetSymbol, fontSize = 45.sp)
                }
            }
        }

        // --- KONTROLE ZA BET ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (bet > 100) bet -= 100 }) {
                Text("-", color = Color.White, fontSize = 30.sp)
            }
            Text("BET: $bet", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { bet += 100 }) {
                Text("+", color = Color.White, fontSize = 30.sp)
            }
        }

        // --- DUGME ZA REKLAMU (REWARDED AD) ---
        Button(
            onClick = { /* Ovde ide AdMob Rewarded kôd */ balance += 1000 },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("📺 WATCH AD FOR $1000", fontWeight = FontWeight.Bold)
        }

        // --- GLAVNO SPIN DUGME ---
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(if (isSpinning) Color.Gray else Color.Red)
                .clickable(enabled = !isSpinning && balance >= bet) {
                    isSpinning = true
                    balance -= bet
                    scope.launch {
                        // Animacija brzog menjanja simbola
                        for (i in 1..20) {
                            for (j in 0..4) {
                                reels[j] = symbols.random()
                            }
                            delay(50)
                        }
                        isSpinning = false
                        // Provera dobitka (jednostavna: ako imaš bar 3 ista)
                        if (reels.distinct().size <= 3) {
                            balance += bet * 10 // BIG WIN!
                        }
                    }
                }
                .border(5.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("SPIN", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}
