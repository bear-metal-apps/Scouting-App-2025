package pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.pop
import com.bumble.appyx.components.backstack.operation.push
import composables.CheckBox
import composables.EnumerableValue
import composables.EnumerableValue
import defaultSecondary
import exportScoutData
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
        teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
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
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Miss L4",
                        value = teleLFourMissed,
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
                    alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                    EnumerableValue(
                        label = "Miss L3",
                        value = teleLThreeMissed,
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
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Miss L2",
                        value = teleLTwoMissed,
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
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Miss L1",
                        value = teleLOneMissed,
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
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Algae Processed",
                        value = teleProcessed,
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
                    alignment = Alignment.BottomEnd,
                    miniMinus = miniMinus.value,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                EnumerableValue(
                    label = "Net Score",
                    value = teleNet,
                    alignment = Alignment.BottomEnd,
                    miniMinus = miniMinus.value,
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
            }
        }
    }
}
