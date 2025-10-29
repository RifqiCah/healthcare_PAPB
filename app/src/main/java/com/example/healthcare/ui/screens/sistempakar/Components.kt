package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcare.R

@Composable
fun HeaderSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // ganti dengan asset logo
            contentDescription = "Logo",
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text("Healthcare Alomany", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HeroSection() {
    Image(
        painter = painterResource(id = R.drawable.foto_fitur_sistem_pakar), // ganti dengan asset hero
        contentDescription = "Hero Banner",
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    )
}

@Composable
fun StepperSection(activeStep: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StepCircle(1, "Info", activeStep == 1)
        StepCircle(2, "Gejala", activeStep == 2)
        StepCircle(3, "Kondisi", activeStep == 3)
        StepCircle(4, "Detail", activeStep == 4)
    }
}

@Composable
fun StepCircle(number: Int, label: String, active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (active) Color(0xFF2196F3) else Color.Gray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(number.toString(), color = Color.White)
        }
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp)
    }
}