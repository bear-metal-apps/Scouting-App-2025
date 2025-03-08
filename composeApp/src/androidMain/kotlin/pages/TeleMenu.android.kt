package pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.pop
import composables.CheckBox
import composables.EnumerableValue
import keyboardAsState
import nodes.*
import java.lang.Integer.parseInt

@Composable
actual fun TeleMenu(
    backStack: BackStack<AutoTeleSelectorNode.NavTarget>,
    mainMenuBackStack: BackStack<RootNode.NavTarget>,
    match: MutableState<String>,

    team: MutableIntState,
    robotStartPosition: MutableIntState
) {
    val scrollState = rememberScrollState(0)
    val isScrollEnabled = remember{ mutableStateOf(true) }
    val isKeyboardOpen by keyboardAsState()
    val context = LocalContext.current
    fun bob() {
        backStack.pop()
        teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
    }

    if (!isKeyboardOpen) {
        isScrollEnabled.value = true
    }

    Column(
    ){
        Row (
            modifier = Modifier
                .weight(10f)
                .fillMaxWidth()
        ) {
            Column (
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                ) {
                    EnumerableValue(
                        label = "Score L4",
                        value = teleLFour,
                        flashColor = Color.Green,
                        backgroundColor = Color(60, 0, 255),
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Miss L4",
                        value = teleLFourMissed,
                        flashColor = Color.Red,
                        backgroundColor = Color(60, 0, 255),
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ){
                    EnumerableValue(
                    label = "Score L3",
                    value = teleLThree,
                    flashColor = Color.Green,
                    backgroundColor = Color(55, 0, 236),
                    alignment = Alignment.BottomEnd,
                    miniMinus = miniMinus.value,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                    EnumerableValue(
                        label = "Miss L3",
                        value = teleLThreeMissed,
                        flashColor = Color.Red,
                        backgroundColor = Color(55, 0, 236),
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ){
                    EnumerableValue(
                        label = "Score L2",
                        value = teleLTwo,
                        flashColor = Color.Green,
                        backgroundColor = Color(50, 0, 215),
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Miss L2",
                        value = teleLTwoMissed,
                        flashColor = Color.Red,
                        backgroundColor = Color(50, 0, 215),
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ){
                    EnumerableValue(
                        label = "Score L1",
                        value = teleLOne,
                        flashColor = Color.Green,
                        backgroundColor = Color(45, 0, 193),
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Miss L1",
                        value = teleLOneMissed,
                        flashColor = Color.Red,
                        backgroundColor = Color(45, 0, 193),
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ){
                    EnumerableValue(
                        label = "Algae Removed",
                        value = teleRemoved,
                        flashColor = Color.Blue,
                        backgroundColor = Color(0, 131, 52),
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Algae Processed",
                        value = teleProcessed,
                        flashColor = Color.Blue,
                        backgroundColor = Color(0, 131, 52),
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
            }

            Column (
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
            ) {

                EnumerableValue(
                    label = "Net Miss",
                    value = teleNetMissed,
                    flashColor = Color.Red,
                    backgroundColor = Color(0, 131, 52),
                    alignment = Alignment.BottomEnd,
                    miniMinus = miniMinus.value,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                EnumerableValue(
                    label = "Net Score",
                    value = teleNet,
                    flashColor = Color.Green,
                    backgroundColor = Color(0, 131, 52),
                    alignment = Alignment.BottomEnd,
                    miniMinus = miniMinus.value,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                EnumerableValue(
                    label = "Penalty",
                    value = penalties,
                    flashColor = Color.Yellow,
                    backgroundColor = Color.Black,
                    alignment = Alignment.BottomEnd,
                    miniMinus = miniMinus.value,
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxWidth()
                )
                CheckBox(
                    label = "Lost Comms",
                    ifChecked = lostComms,
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxWidth()
                )
            }
        }
    }
}
