package pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.pop
import com.bumble.appyx.components.backstack.operation.push
import compKey
import composables.Comments
import composables.EndGameCheckBox
import createScoutMatchDataFile
import defaultSecondary
import getTeamsOnAlliance
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
        teamDataArray.get(compKey)?.get(match.value.betterParseInt())?.set(robotStartPosition.intValue, createOutput(team, robotStartPosition))
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
                EndGameCheckBox("Park: ", park, shallow, deep, modifier = Modifier)
                EndGameCheckBox("Shallow: ", shallow, park, deep, modifier = Modifier)
                EndGameCheckBox("Deep: ", deep, shallow, park, modifier = Modifier)
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

                        saveData.value = true
                        teamDataArray.get(compKey)?.get(match.value.betterParseInt())?.set(robotStartPosition.intValue, createOutput(team, robotStartPosition))
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
                    if(saveData.value) {
                        if (scoutingRanks.get(scoutName.value)?.get(match.value.betterParseInt()) == null) {
                            totalScoutXp.value += xpPerMatch
                            updateScoutXP(totalScoutXp, updatedXP)
                            scoutingRanks.get(scoutName.value)?.set(match.value.betterParseInt(), xpPerMatch)
                        }

                        //Save temp data
                        teamDataArray.get(compKey)?.get(match.value.betterParseInt())?.set(robotStartPosition.intValue, createOutput(team, robotStartPosition))

                        //Save permanent data
                        createScoutMatchDataFile(compKey, match.value, team.intValue, createOutput(team, robotStartPosition))
                        match.value = (parseInt(match.value) + 1).toString()
                        reset()
                        matchFirst.value = true
                        backStack.push(AutoTeleSelectorNode.NavTarget.AutoScouting)
                        //Grab team from TBA for next match
                        try{
                            team.intValue = getTeamsOnAlliance(match.value.betterParseInt(), isRedAliance.value)[tempRobotStart.value].number
                        }catch (e: Exception){}
                        stringTeam.value = team.intValue.toString()

//                        loadData(parseInt(match.value), team, robotStartPosition)

                    } else {
                        saveDataPopup.value = true
                        saveDataSit.value = false
                    }
                    teleFlash.value = false
                },
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp)
            ) {
                Text("Next Match", fontSize = 20.sp)
            }
        }
}