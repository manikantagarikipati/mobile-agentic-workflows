package com.example.agentic.screenshot

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.agentic.figmacomposeui.CardChallengeScreen
import com.example.agentic.figmacomposeui.CardChallengeUiState
import com.example.agentic.ui.theme.AgenticWorkflowsTheme
import org.junit.Rule
import org.junit.Test

class CardChallengeScreenTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5
    )

    @Test
    fun cardChallengeScreen_default() {
        paparazzi.snapshot {
            AgenticWorkflowsTheme {
                CardChallengeScreen(
                    uiState = CardChallengeUiState(merchantName = "Zoo Barcelona"),
                    onClose = {},
                    onConfirm = {},
                    onDecline = {}
                )
            }
        }
    }
}
