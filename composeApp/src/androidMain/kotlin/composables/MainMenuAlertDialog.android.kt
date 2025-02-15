@file:OptIn(ExperimentalMaterial3Api::class)

package composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import exportScoutData
import getCurrentTheme

@Composable
actual fun MainMenuAlertDialog(active: MutableState<Boolean>, bob: () -> Unit) {
    val context = LocalContext.current
    if (active.value) {
        BasicAlertDialog(
            onDismissRequest = { active.value = false },
            modifier = Modifier.clip(
                RoundedCornerShape(5.dp)
            ).border(BorderStroke(3.dp, getCurrentTheme().primaryVariant), RoundedCornerShape(5.dp))
            .background(getCurrentTheme().secondary)
        ) {
            Box(modifier = Modifier.fillMaxWidth(8f / 10f).padding(5.dp).fillMaxHeight(1/8f)) {
                Text(text = "Return to main menu??",
                    modifier = Modifier.padding(5.dp).align(Alignment.TopCenter)
                )
                OutlinedButton(
                    onClick = {
                        active.value = false
                        bob.invoke()
                        exportScoutData(context) },
                    border = BorderStroke(2.dp, getCurrentTheme().secondaryVariant),
                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor = getCurrentTheme().secondary, contentColor = getCurrentTheme().onSecondary) ,
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Text(text = "Yes", color = getCurrentTheme().error)
                }
                OutlinedButton(
                    onClick = { active.value = false },
                    border = BorderStroke(2.dp, getCurrentTheme().secondaryVariant),
                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor = getCurrentTheme().secondary, contentColor = getCurrentTheme().onSecondary),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Text(text = "No", color = getCurrentTheme().error)
                }
            }
        }
    }
}