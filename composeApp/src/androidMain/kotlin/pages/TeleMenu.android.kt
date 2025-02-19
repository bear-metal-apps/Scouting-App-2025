package pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                        alignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Miss L4",
                        value = teleLFourMissed,
                        flashColor = Color.Red,
                        alignment = Alignment.CenterEnd,
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
                        label = "Algae L3",
                        value = teleLThreeAlgae,
                        flashColor = Color.Green,
                        alignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                    label = "Score L3",
                    value = teleLThree,
                        flashColor = Color.Green,
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                    EnumerableValue(
                        label = "Miss L3",
                        value = teleLThreeMissed,
                        flashColor = Color.Red,
                        alignment = Alignment.CenterEnd,
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
                        label = "Algae L2",
                        value = teleLTwoAlgae,
                        flashColor = Color.Green,
                        alignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Score L2",
                        value = teleLTwo,
                        flashColor = Color.Green,
                        alignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Miss L2",
                        value = teleLTwoMissed,
                        flashColor = Color.Red,
                        alignment = Alignment.CenterEnd,
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
                        alignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Miss L1",
                        value = teleLOneMissed,
                        flashColor = Color.Red,
                        alignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
                EnumerableValue(
                    label = "Algae Processed",
                    value = teleProcessed,
                    flashColor = Color.Green,
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
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
                    alignment = Alignment.BottomEnd,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                EnumerableValue(
                    label = "Net Score",
                    value = teleNet,
                    flashColor = Color.Green,
                    alignment = Alignment.BottomEnd,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                CheckBox(
                    label = "lost Comms",
                    ifChecked = lostComms,
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxWidth()
                )
                if(lostComms.value != 0) {
                    saveData.value = true
                }
            }
        }
    }
}
