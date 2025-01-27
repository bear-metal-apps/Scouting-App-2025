package pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.pop
import composables.Cage
import composables.CheckBox
import composables.Comments
import defaultSecondary
import exportScoutData
import keyboardAsState
import nodes.*
import setTeam
import java.lang.Integer.parseInt



@Composable
actual fun EndGameMenu(
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
        mainMenuBackStack.pop()
        matchScoutArray.putIfAbsent(robotStartPosition.intValue, HashMap())
        matchScoutArray[robotStartPosition.intValue]?.set(parseInt(match.value), createOutput(team, robotStartPosition))
        exportScoutData(context)
    }

    if(!isKeyboardOpen){
        isScrollEnabled.value = true
    }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(Modifier.padding(bottom = 50.dp, top = 20.dp).align(Alignment.CenterHorizontally)) {
                Cage("Center Barge", aClimb, aDeep, bClimb, cClimb, Modifier.fillMaxSize())
                Cage("Middle", bClimb, bDeep, aClimb, cClimb, Modifier.fillMaxSize())
                Cage("Outer Edge", cClimb, cDeep, aClimb, bClimb, Modifier.fillMaxSize())
            }
            Row(){
                Text(
                    text = "Played Defense:",
                    fontSize = 24.sp
                )
                Checkbox(
                    checked = playedD.value,
                    onCheckedChange ={
                        playedD.value = !playedD.value
                    },
                    modifier = Modifier,
                )
            }

            Comments(teleNotes)
            Spacer(Modifier.height(4.dp))
        }
}