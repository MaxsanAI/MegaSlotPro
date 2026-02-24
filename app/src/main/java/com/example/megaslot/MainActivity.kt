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
            // Glavna tema sa tamnom pozadinom
            Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF0F0C29)) {
                SlotMachineGame()
            }
        }
    }
}

@Composable
fun SlotMachineGame() {
    val symbols = listOf("💰", "💎", "7️⃣", "🍒", "🔔", "⭐", "🍀", "🔥")
    var balance by remember { mutableStateOf(5000) }
    var currentBet by remember { mutableStateOf(100) }
    var isSpinning by remember { mutableStateOf(false) }
    val reels = remember { mutableStateListOf("🎰", "🎰", "🎰", "🎰", "🎰") }
    val scope = rememberCoroutineScope()

    // Gradijent za pozadinu reels-a
    val reelGradient = Brush.verticalGradient(
        colors = listOf(Color.Black, Color(0xFF1A1A1A), Color.Black)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- HEADER ---
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "MEGA SEGA SLOT",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Color.Cyan
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$$balance",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow
            )
        }

        // --- 5x REELS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(reelGradient)
                .border(2.dp, Color.Cyan, RoundedCornerShape(16.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            reels.forEach { symbol ->
                AnimatedContent(
                    targetState = symbol,
                    transitionSpec = {
                        slideInVertically { it } + fadeIn() with slideOutVertically { -it } + fadeOut()
                    }, label = ""
                ) { targetSymbol ->
                    Text(targetSymbol, fontSize = 45.sp)
                }
            }
        }

        // --- BET CONTROLS ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { if (currentBet > 100) currentBet -= 100 }) { Text("-") }
            Text(
                "BET: $currentBet",
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = { currentBet += 100 }) { Text("+") }
        }

        // --- REWARDED AD PLACEHOLDER ---
        Button(
            onClick = { 
                // Simulacija gledanja reklame
                balance += 500 
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("📺 FREE CREDITS (ADS)", fontWeight = FontWeight.Bold)
        }

        // --- MAIN SPIN BUTTON ---
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(if (isSpinning) Color.Gray else Color.Red)
                .border(4.dp, Color.White, CircleShape)
                .clickable(enabled = !isSpinning && balance >= currentBet) {
                    isSpinning = true
                    balance -= currentBet
                    scope.launch {
                        // Simulacija vrtenja
                        repeat(20) {
                            for (i in 0..4) {
                                reels[i] = symbols.random()
                            }
                            delay(60)
                        }
                        isSpinning = false
                        
                        // LOGIKA DOBITKA (3 ili više ista)
                        val distinctSymbols = reels.distinct().size
                        if (distinctSymbols <= 2) {
                            balance += currentBet * 10 // MEGA WIN
                        } else if (distinctSymbols <= 3) {
                            balance += currentBet * 3  // WIN
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "SPIN",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )
        }
    }
}
