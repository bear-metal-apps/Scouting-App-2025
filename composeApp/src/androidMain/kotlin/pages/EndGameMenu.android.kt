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
import com.bumble.appyx.components.backstack.operation.push
import composables.Cage
import composables.CheckBox
import composables.Comments
import defaultSecondary
import keyboardAsState
import nodes.*
import java.lang.Integer.parseInt



@Composable
actual fun EndGameMenu(
    backStack: BackStack<AutoTeleSelectorNode.NavTarget>,
    mainMenuBackStack: BackStack<RootNode.NavTarget>,
    match: MutableState<String>,
    team: MutableIntState,
    robotStartPosition: MutableIntState
) {
    rememberScrollState(0)
    val isScrollEnabled = remember{ mutableStateOf(true) }
    val isKeyboardOpen by keyboardAsState()
    val context = LocalContext.current

    fun bob() {
        mainMenuBackStack.pop()
        teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
    }

    if(!isKeyboardOpen){
        isScrollEnabled.value = true
    }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(20.dp))
            Column (
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Row(){
                    Text(
                        text = "Park:",
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = park.value,
                        onCheckedChange ={
                            park.value = !park.value
                        },
                        modifier = Modifier.align(Alignment.CenterVertically),
                    )
                }
                Row(){
                    Text(
                        text = "Deep:",
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = deep.value,
                        onCheckedChange ={
                            deep.value = !deep.value
                        },
                        modifier = Modifier.align(Alignment.CenterVertically),
                    )
                }
                Row(){
                    Text(
                        text = "Shallow:",
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = shallow.value,
                        onCheckedChange ={
                            shallow.value = !shallow.value
                        },
                        modifier = Modifier.align(Alignment.CenterVertically),
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Row(){
                Text(
                    text = "Played Defense:",
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Checkbox(
                    checked = playedDefense.value,
                    onCheckedChange ={
                        playedDefense.value = !playedDefense.value
                    },
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
            }
            Comments(notes)
            Spacer(Modifier.height(20.dp))
            OutlinedButton(
                border = BorderStroke(3.dp, Color.Yellow),
                shape = RoundedCornerShape(25.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
                onClick = {
                    teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
                    println(teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)])
                    match.value = (parseInt(match.value) + 1).toString()
                    reset()
                    backStack.push(AutoTeleSelectorNode.NavTarget.AutoScouting)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp)
            ) {
                Text("Next Match", fontSize = 20.sp)
            }
        }
}