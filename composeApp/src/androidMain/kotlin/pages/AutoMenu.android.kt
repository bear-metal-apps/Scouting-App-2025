package pages

//import composables.AutoCheckboxesHorizontal
//import composables.AutoCheckboxesVertical
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.pop
import composables.CheckBox
import composables.EnumerableValue
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
import nodes.autoNetMissed
import nodes.autoNetScored
import nodes.autoStop
import nodes.collectCoral
import nodes.createOutput
import nodes.groundCollectionAlgae
import nodes.miniMinus
import nodes.pageIndex
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

    pageIndex.intValue = 0

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
                        value = autoCoralLevel4Scored,
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
                        value = autoCoralLevel4Missed,
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
                        value = autoCoralLevel3Scored,
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
                        value = autoCoralLevel3Missed,
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
                        value = autoCoralLevel2Scored,
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
                        value = autoCoralLevel2Missed,
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
                        value = autoCoralLevel1Scored,
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
                        value = autoCoralLevel1Missed,
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
                ) {
                    EnumerableValue(
                        label = "Collect coral",
                        value = collectCoral,
                        flashColor = Color.Green,
                        backgroundColor = Color(60, 0, 255),
                        alignment = Alignment.CenterEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    TriStateCheckBox(
                        label = "Ground Algae",
                        color = Color(0, 131, 52),
                        ifChecked = groundCollectionAlgae,
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
                        value = algaeRemoved,
                        flashColor = Color.Green,
                        backgroundColor = Color(0, 131, 52),
                        alignment = Alignment.BottomEnd,
                        miniMinus = miniMinus.value,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    EnumerableValue(
                        label = "Algae Processed",
                        value = algaeProcessed,
                        flashColor = Color.Green,
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
                    value = autoNetMissed,
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
                    value = autoNetScored,
                    flashColor = Color.Green,
                    backgroundColor = Color(0, 131, 52),
                    alignment = Alignment.BottomEnd,
                    miniMinus = miniMinus.value,
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