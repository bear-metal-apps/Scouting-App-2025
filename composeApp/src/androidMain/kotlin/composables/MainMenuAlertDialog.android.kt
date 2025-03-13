@file:OptIn(ExperimentalMaterial3Api::class)

package composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import compKey
import createScoutMatchDataFile
import getCurrentTheme
import nodes.*
import nodes.TeamMatchStartKey
import nodes.betterParseInt
import nodes.createOutput
import nodes.match
import nodes.saveData
import nodes.saveDataPopup
import nodes.teamDataArray
import nodes.teleFlash
import java.lang.Integer.parseInt

@Composable
actual fun MainMenuAlertDialog(active: MutableState<Boolean>, bob: () -> Unit, team: Int, robotStartPosition: Int) {
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

                        if(!saveData.value) {
                            saveDataPopup.value = true
                        } else {
                            active.value = false

                            teamDataArray.get(compKey)?.get(match.value.betterParseInt())?.set(robotStartPosition, createOutput(mutableIntStateOf(team), mutableIntStateOf(robotStartPosition)))
                            createScoutMatchDataFile(compKey, match.value, team, createOutput(mutableIntStateOf(team), mutableIntStateOf(robotStartPosition))) // permanent save

                            bob.invoke()
                        }

                        teleFlash.value = false

                    },
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