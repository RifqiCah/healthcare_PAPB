package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderSection()
        Spacer(Modifier.height(16.dp))
        HeroSection()
        Spacer(Modifier.height(24.dp))
        StepperSection(activeStep = 4)
        Spacer(Modifier.height(32.dp))

        Text("Artikel terkait:", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(16.dp))

        repeat(2) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Artikel ${index + 1}", fontWeight = FontWeight.Bold)
                    Text("Deskripsi singkat artikel ${index + 1}", fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onBack, shape = CircleShape) { Text("Kembali") }
            Button(
                onClick = onNext,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF2196F3))
            ) { Text("Selesai", color = androidx.compose.ui.graphics.Color.White) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDetailScreen() {
    DetailScreen(onNext = {}, onBack = {})
}
