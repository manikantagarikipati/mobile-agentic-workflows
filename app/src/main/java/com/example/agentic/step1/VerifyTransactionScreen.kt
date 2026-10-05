package com.example.agentic.step1

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agentic.R

// Colors pulled straight from Figma fills
private val BgCream = Color(0xFFFBFBF9)
private val SurfaceBeige = Color(0xFFF5F4ED)
private val TextDark = Color(0xFF1E1C1C)
private val TextMuted = Color(0xFF706464)
private val BorderLight = Color(0xFFE3E2D6)
private val BorderStrong = Color(0xFFD0CDC3)
private val DangerRed = Color(0xFFD23F04)
private val SkyStroke = Color(0xFF9FC4E8)

@Composable
fun VerifyTransactionScreen(
    merchantName: String = "{Merchant name}",
    cardLastFour: String = "1234",
    amount: String = "1.500,00 €",
    countdown: String = "05:00",
    onClose: () -> Unit = {},
    onSave: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onDecline: () -> Unit = {},
) {
    Scaffold(
        containerColor = BgCream,
        topBar = { TopHeaderBar(onClose = onClose, onSave = onSave) },
        bottomBar = {
            BottomActions(
                onConfirm = onConfirm,
                onDecline = onDecline,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            AmountHeaderWithClock(amount = amount, countdown = countdown)
            TransactionDetailsCard(merchantName = merchantName, cardLastFour = cardLastFour)
        }
    }
}

@Composable
private fun TopHeaderBar(onClose: () -> Unit, onSave: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgCream)
            .statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .background(SurfaceBeige, CircleShape)
                    .padding(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = TextDark,
                    modifier = Modifier.size(24.dp),
                )
            }
            Text(
                text = "Verify transaction",
                color = TextDark,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            // "Save" action: opacity 0 in design, intentionally not rendered
        }
    }
}

@Composable
private fun AmountHeaderWithClock(amount: String, countdown: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(199.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceBeige, RoundedCornerShape(24.dp))
                .padding(top = 65.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Amount",
                color = TextDark,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = amount,
                color = TextDark,
                fontSize = 32.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
            )
        }
        // Circular countdown ticker: sky-colored ring stroke, empty/beige inside
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(100.dp)
                .clip(CircleShape)
                .background(SurfaceBeige)
                .border(BorderStroke(3.dp, SkyStroke), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = countdown,
                color = TextDark,
                fontSize = 24.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun TransactionDetailsCard(merchantName: String, cardLastFour: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BgCream)
            .border(BorderStroke(2.dp, BorderLight), RoundedCornerShape(16.dp)),
    ) {
        // Top item: generic card icon + merchant name / "Online payment"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.CreditCard,
                contentDescription = null,
                tint = TextDark,
                modifier = Modifier.size(24.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 16.dp),
            ) {
                Text(text = merchantName, color = TextDark, fontSize = 16.sp, lineHeight = 22.sp)
                Text(
                    text = "Online payment",
                    color = TextMuted,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
                )
            }
        }
        // Thin divider confined to the text column only (not under the icon)
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(16.dp + 24.dp + 16.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(0.5.dp)
                    .background(BorderLight)
                    .padding(end = 16.dp),
            )
        }
        // Bottom item: Mastercard-style payment icon + "Card ending in XXXX"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.CreditCard,
                contentDescription = null,
                tint = TextDark,
                modifier = Modifier.size(24.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 16.dp),
            ) {
                Text(text = "Card ending in $cardLastFour", color = TextDark, fontSize = 16.sp, lineHeight = 22.sp)
            }
        }
    }
}

@Composable
private fun BottomActions(onConfirm: () -> Unit, onDecline: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgCream)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        FraudBanner()
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TextDark, contentColor = BgCream),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentPadding = PaddingValues(12.dp),
            ) {
                Text(text = "Confirm", fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.Medium)
            }
            OutlinedButton(
                onClick = onDecline,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderStrong),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextDark, containerColor = BgCream),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentPadding = PaddingValues(12.dp),
            ) {
                Text(text = "Decline", fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun FraudBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DangerRed, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "This call is not from SumUp",
                color = BgCream,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = "If the caller claims to be from SumUp, hang up immediately.",
                color = BgCream,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_scam_call_24),
            contentDescription = null,
            tint = BgCream,
            modifier = Modifier.size(48.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun VerifyTransactionScreenPreview() {
    VerifyTransactionScreen()
}
