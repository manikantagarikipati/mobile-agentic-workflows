package com.example.agentic.figmacomposeui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.agentic.R
import com.example.agentic.ui.theme.AgenticWorkflowsTheme

// Figma source: https://www.figma.com/design/efbFnzvB9qzxfggMvw3dvq/-EU--Card-management-screen?node-id=44901-18379
// "Challenge prompt - Call not from SumUp"

/**
 * UI state for [CardChallengeScreen]. Merchant name is a dynamic field from the Figma design
 * (shown as "{Merchant name}" placeholder), everything else has a Figma-derived default.
 */
data class CardChallengeUiState(
    val amountText: String = "1.500,00 \u20AC",
    val merchantName: String,
    val cardLastFour: String = "1234",
    val remainingTime: String = "05:00",
)

// Design tokens pulled directly from Figma fills (project has no matching Material3 custom
// color scheme or DemoSpaces spacing scale registered, see Blueprint.md deviations).
private val ScreenBackground = Color(0xFFFBFBF9)
private val CardBackground = Color(0xFFF5F4ED)
private val PrimaryText = Color(0xFF1E1C1C)
private val BorderColor = Color(0xFFE3E2D6)
private val MutedText = Color(0xFF706464)
private val FraudBannerBackground = Color(0xFFD23F04)
private val DeclineBorder = Color(0xFFD0CDC3)
private val OnDark = Color(0xFFFBFBF9)

@Composable
fun CardChallengeScreen(
    uiState: CardChallengeUiState,
    onClose: () -> Unit,
    onConfirm: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier.fillMaxWidth().fillMaxHeight(), color = ScreenBackground) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            CardChallengeHeader(onClose = onClose)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                AmountHeaderWithTicker(
                    amountText = uiState.amountText,
                    remainingTime = uiState.remainingTime,
                )

                MerchantAndCardList(
                    merchantName = uiState.merchantName,
                    cardLastFour = uiState.cardLastFour,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                FraudBanner()

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryText,
                            contentColor = OnDark,
                        ),
                    ) {
                        Text(
                            text = "Confirm",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    OutlinedButton(
                        onClick = onDecline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DeclineBorder),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryText),
                    ) {
                        Text(
                            text = "Decline",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CardChallengeHeader(onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(CardBackground),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = PrimaryText,
                modifier = Modifier.size(20.dp),
            )
        }

        Text(
            text = "Verify transaction",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = PrimaryText,
        )
    }
}

/**
 * Header card + overlapping countdown "ticker" ring. The ring stroke is drawn with a
 * [ShaderBrush] sourced from the sky-texture bitmap (clock_sky_texture.png, Figma node
 * 44962:2183) so the ring renders the actual sky/cloud image rather than a solid color
 * or hand-drawn gradient approximation. The exact Figma stroke mask (44962:2182) failed to
 * export, so the ring geometry itself (a simple circular stroke) is an approximation —
 * documented as a deviation in Blueprint.md.
 */
@Composable
private fun AmountHeaderWithTicker(amountText: String, remainingTime: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(199.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardBackground,
            shape = RoundedCornerShape(24.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 65.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Amount",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryText,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = amountText,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = PrimaryText,
                    textAlign = TextAlign.Center,
                )
            }
        }

        CountdownTicker(
            remainingTime = remainingTime,
            modifier = Modifier
                .size(100.dp)
                .offset(y = 0.dp)
                .align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun CountdownTicker(remainingTime: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val skyTextureBitmap = remember {
        android.graphics.BitmapFactory
            .decodeResource(context.resources, R.drawable.clock_sky_texture)
            .asImageBitmap()
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(100.dp)) {
            // Background circle (Figma: Ellipse "Background", #F5F4ED)
            drawCircle(color = CardBackground)

            // Ring stroke textured with the sky image (Figma: Group "Stroke" clipped to
            // the sky-texture rectangle via a mask shape). Approximated here as a plain
            // circular stroke using the bitmap as a shader brush for pixel-sourced texture.
            val strokeWidthPx = 10.dp.toPx()
            drawCircle(
                brush = ShaderBrush(ImageShader(skyTextureBitmap)),
                radius = (size.minDimension - strokeWidthPx) / 2f,
                style = Stroke(width = strokeWidthPx),
            )
        }

        Text(
            text = remainingTime,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = PrimaryText,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun MerchantAndCardList(merchantName: String, cardLastFour: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = BorderColor, shape = RoundedCornerShape(16.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ScreenBackground, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                tint = PrimaryText,
                modifier = Modifier.size(24.dp),
            )
            Column {
                Text(
                    text = merchantName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = PrimaryText,
                )
                Text(
                    text = "Online payment",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedText,
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(BorderColor),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    ScreenBackground,
                    RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                )
                .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Payment,
                contentDescription = null,
                tint = PrimaryText,
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = "Card ending in $cardLastFour",
                style = MaterialTheme.typography.bodyLarge,
                color = PrimaryText,
            )
        }
    }
}

@Composable
private fun FraudBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(FraudBannerBackground, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "This call is not from SumUp",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = OnDark,
            )
            Text(
                text = "If the caller claims to be from SumUp, hang up immediately.",
                style = MaterialTheme.typography.bodySmall,
                color = OnDark,
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_scam_call_24),
            contentDescription = null,
            tint = OnDark,
            modifier = Modifier.size(48.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun CardChallengeScreenPreview() {
    AgenticWorkflowsTheme {
        CardChallengeScreen(
            uiState = CardChallengeUiState(merchantName = "Zoo Barcelona"),
            onClose = {},
            onConfirm = {},
            onDecline = {},
        )
    }
}
