package pages

//import composables.AutoCheckboxesHorizontal
//import composables.AutoCheckboxesVertical
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.pop
import composables.CheckBox
import composables.EnumerableValueAuto
import composables.TriStateCheckBox
import keyboardAsState
import nodes.AutoTeleSelectorNode
import nodes.RootNode
import nodes.TeamMatchStartKey
import nodes.algaeProcessed
import nodes.algaeRemoved
import nodes.autoCoralLevel1Missed
import nodes.autoCoralLevel1Scored
import nodes.autoCoralLevel2Missed
import nodes.autoCoralLevel2Scored
import nodes.autoCoralLevel3Missed
import nodes.autoCoralLevel3Scored
import nodes.autoCoralLevel4Missed
import nodes.autoCoralLevel4Scored
import nodes.autoFeederCollection
import nodes.autoNetMissed
import nodes.autoNetScored
import nodes.autoStop
import nodes.createOutput
import nodes.groundCollectionAlgae
import nodes.groundCollectionCoral
import nodes.saveData
import nodes.teamDataArray
import java.lang.Integer.parseInt

@SuppressLint("UnrememberedMutableState")
@Composable
actual fun AutoMenu(
    backStack: BackStack<AutoTeleSelectorNode.NavTarget>,
    mainMenuBackStack: BackStack<RootNode.NavTarget>,
    match: MutableState<String>,
    team: MutableIntState,
    robotStartPosition: MutableIntState
) {

    val context = LocalContext.current
    fun bob() {
        mainMenuBackStack.pop()
        teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
    }

    val isScrollEnabled = remember { mutableStateOf(true) }
    val isKeyboardOpen by keyboardAsState()
    if (!isKeyboardOpen) {
        isScrollEnabled.value = true
    }

//    val flippingAuto = remember { mutableStateOf(false)}
//    val rotateAuto = remember { mutableStateOf(false)}

    Column(
    ){
        Row (
            modifier = Modifier
                .weight(10f)
                .fillMaxWidth()
        ) {

            Column (
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight()
            ) {

                EnumerableValueAuto(
                    label = "Feeder",
                    value = autoFeederCollection,
                    flashColor = androidx.compose.ui.graphics.Color.Green,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                TriStateCheckBox(
                    label = "Ground Coral",
                    ifChecked = groundCollectionCoral,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                TriStateCheckBox(
                    label = "Ground Algae",
                    ifChecked = groundCollectionAlgae,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

            }

            Column (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {

                EnumerableValueAuto(
                    label = "Score L4",
                    value = autoCoralLevel4Scored,
                    flashColor = Color.Green,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                EnumerableValueAuto(
                    label = "Score L3",
                    value = autoCoralLevel3Scored,
                    flashColor = Color.Green,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                EnumerableValueAuto(
                    label = "Score L2",
                    value = autoCoralLevel2Scored,
                    flashColor = Color.Green,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                EnumerableValueAuto(
                    label = "Score L1",
                    value = autoCoralLevel1Scored,
                    flashColor = Color.Green,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                EnumerableValueAuto(
                    label = "Algae Removed",
                    value = algaeRemoved,
                    flashColor = Color.Green,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

            }

            Column (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                EnumerableValueAuto(
                    label = "Miss L4",
                    value = autoCoralLevel4Missed,
                    flashColor = Color.Red,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                EnumerableValueAuto(
                    label = "Miss L3",
                    value = autoCoralLevel3Missed,
                    flashColor = Color.Red,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                EnumerableValueAuto(
                    label = "Miss L2",
                    value = autoCoralLevel2Missed,
                    flashColor = Color.Red,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                EnumerableValueAuto(
                    label = "Miss L1",
                    value = autoCoralLevel1Missed,
                    flashColor = Color.Red,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                EnumerableValueAuto(
                    label = "Algae Processed",
                    value = algaeProcessed,
                    flashColor = Color.Green,
                    alignment = Alignment.Center,
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

                EnumerableValueAuto(
                    label = "Net Missed",
                    value = autoNetMissed,
                    flashColor = Color.Green,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                EnumerableValueAuto(
                    label = "Net Scored",
                    value = autoNetScored,
                    flashColor = Color.Green,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                CheckBox(
                    label = "A-stop",
                    ifChecked = autoStop,
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                )
                if(autoStop.value != 0) {
                    saveData.value = true
                }
            }

        }
    }
}