package pages

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.pop
import com.bumble.appyx.components.backstack.operation.push
import createScoutMatchDataFile
import exportScoutData
import getCurrentTheme
import getTeamsOnAlliance
import kotlinx.coroutines.delay
import nodes.*
import org.jetbrains.compose.resources.load
import org.json.JSONException
import setTeam
import java.lang.Integer.parseInt
import java.util.*

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.R)
@Composable
actual fun MatchMenuTop(
    match: MutableState<String>,
    team: MutableIntState,
    robotStartPosition: MutableIntState
) {
    var positionName by remember { mutableStateOf("") }
    var teamColor by remember { mutableStateOf(Color.Black) }
    val context = LocalContext.current
    var teamNumAsText by remember { mutableStateOf(team.intValue.toString()) }
    var pageName = mutableListOf("A","T","E")

    var first by remember { mutableStateOf(true) }

    when (robotStartPosition.intValue) {
        0 -> {
            positionName = "R1"
            teamColor = Color.Red
            isRedAliance.value = true
            tempRobotStart.value = 0
        }

        1 -> {
            positionName = "R2"
            teamColor = Color.Red
            isRedAliance.value = true
            tempRobotStart.value = 1
        }

        2 -> {
            positionName = "R3"
            teamColor = Color.Red
            isRedAliance.value = true
            tempRobotStart.value = 2
        }

        3 -> {
            positionName = "B1"
            teamColor = Color.Blue
            isRedAliance.value = false
//            tempRobotStart.value -= 3
            tempRobotStart.value = 0
        }

        4 -> {
            positionName = "B2"
            teamColor = Color.Blue
            isRedAliance.value = false
//            tempRobotStart.value -= 3
            tempRobotStart.value = 1
        }

        5 -> {
            positionName = "B3"
            teamColor = Color.Blue
            isRedAliance.value = false
//            tempRobotStart.value -= 3
            tempRobotStart.value = 2
        }
    }
//    tempRobotStart = robotStartPosition
    if (positionName == "R1" || positionName == "R2" || positionName == "R3"){
        isRedAliance.value = true
    }else{
        isRedAliance.value = false
//        tempRobotStart.value -= 3
    }

    if(first) {
        println("first")
        try{
            team.intValue = getTeamsOnAlliance(match.value.betterParseInt(), isRedAliance.value)[tempRobotStart.value].number
        }catch (e: Exception){}

        stringMatch = remember { mutableStateOf(match.value) }
        stringTeam = remember { mutableStateOf(team.intValue.toString()) }

        if(teamDataArray[TeamMatchStartKey(match.value.betterParseInt(), team.intValue, robotStartPosition.intValue)] == null) {
            saveData.value = false
        } else {
            saveData.value = true
        }

        createJson(team, robotStartPosition)
        loadData(
            parseInt(match.value), team, robotStartPosition
        )

        first = false
    }

    Column() {
        HorizontalDivider(color = getCurrentTheme().primaryVariant, thickness = 4.dp)

        Row(
            Modifier
                .align(Alignment.CenterHorizontally)
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .background(teamColor)
                    .align(Alignment.CenterVertically)
                    .fillMaxHeight()
            ) {
                Text(
                    text = positionName,
                    modifier = Modifier
                        .scale(1.2f)
                        .padding(horizontal = 25.dp)
                        .align(Alignment.Center),
                    fontSize = 28.sp
                )
            }

            VerticalDivider(
                color = getCurrentTheme().primaryVariant,
                thickness = 3.dp
            )

            TextField(
                value = stringTeam.value,
                onValueChange = { value ->
                    if(saveData.value) {
                        teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(mutableIntStateOf(team.intValue), robotStartPosition)
                        createScoutMatchDataFile(context, match.value, team.intValue, createOutput(mutableIntStateOf(team.intValue), robotStartPosition))
                    }

                    if(value.isNotEmpty()) {
                        val filteredText = value.filter { it.isDigit() }
                        stringTeam.value = filteredText.slice(0..<filteredText.length.coerceAtMost(5))
                    } else {
                        stringTeam.value = ""
                    }
                    team.intValue = stringTeam.value.betterParseInt()

                    if(teamDataArray[TeamMatchStartKey(match.value.betterParseInt(), team.intValue, robotStartPosition.intValue)] == null) {
                        saveData.value = false
                    } else {
                        saveData.value = true
                    }

                    if(value != "")
                        loadData(parseInt(match.value), team, robotStartPosition)

                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = getCurrentTheme().background,
                    unfocusedTextColor = getCurrentTheme().onPrimary,
                    focusedContainerColor = getCurrentTheme().background,
                    focusedTextColor = getCurrentTheme().onPrimary,
                    cursorColor = getCurrentTheme().onSecondary
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
//                    .padding(horizontal = 25.dp)
                    .width(125.dp),
                textStyle = TextStyle.Default.copy(fontSize = 31.sp),
                singleLine = true,
            )

            VerticalDivider(
                color = getCurrentTheme().primaryVariant,
                thickness = 3.dp
            )

            Text(
                text = "Match",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 25.dp),
                fontSize = 28.sp
            )

            TextField(
                value = stringMatch.value,
                onValueChange = { value ->
                    if(saveData.value) {
                        teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(mutableIntStateOf(team.intValue), robotStartPosition)
                        createScoutMatchDataFile(context, match.value, team.intValue, createOutput(mutableIntStateOf(team.intValue), robotStartPosition))
                    }

                    if(value.isNotEmpty()) {
                        val filteredText = value.filter { it.isDigit() }
                        stringMatch.value = filteredText.slice(0..<filteredText.length.coerceAtMost(5))
                    } else {
                        stringMatch.value = ""
                    }
                    match.value = stringMatch.value.betterParseInt().toString()
                    println(match.value)

                    try {
                        setTeam(team, nodes.match, robotStartPosition.intValue)
                    } catch (e: JSONException) {
                        openError.value = true
                    }
                    stringTeam.value = team.intValue.toString()

                    println(team.value)

                    if(teamDataArray[TeamMatchStartKey(match.value.betterParseInt(), team.intValue, robotStartPosition.intValue)] == null) {
                        saveData.value = false
                    } else {
                        saveData.value = true
                    }

                    if(value != "")
                        loadData(parseInt(match.value), team, robotStartPosition)

                },
                modifier = Modifier.fillMaxWidth(1/2f),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = getCurrentTheme().background,
                    unfocusedTextColor = getCurrentTheme().onPrimary,
                    focusedContainerColor = getCurrentTheme().background,
                    focusedTextColor = getCurrentTheme().onPrimary,
                    cursorColor = getCurrentTheme().onSecondary
                ),
                singleLine = true,
                textStyle = TextStyle.Default.copy(fontSize = 28.sp)
            )
            VerticalDivider(
                color = getCurrentTheme().primaryVariant,
                thickness = 3.dp
            )
            Box(
                modifier = Modifier.fillMaxHeight().background(if(pageIndex.value == 0) Color.Green.copy(alpha = 0.5f) else if(pageIndex.value == 1) Color.Yellow.copy(alpha = 0.5f) else Color.Red.copy(alpha = 0.5f))
            ) {
                Text(
                    text = pageName[pageIndex.value],
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(Alignment.Center),
                    fontSize = 28.sp
                )
            }
        }
        HorizontalDivider(color = getCurrentTheme().primaryVariant, thickness = 3.dp)
    }
}











@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
actual fun MatchMenuBottom(
    robotStartPosition: MutableIntState,
    team: MutableIntState,
    pageIndex: MutableIntState,
    backStack: BackStack<AutoTeleSelectorNode.NavTarget>,
    mainMenuBackStack: BackStack<RootNode.NavTarget>,
    mainMenuDialog: MutableState<Boolean>
) {
    var backgroundColor = remember { mutableStateOf(Color.Black) }
    var textColor = remember { mutableStateOf(Color.White) }

    var startTimer = if(totalAutoCoralAttempts.intValue > 0 && pageIndex.intValue == 0) true else false
    var teleColor = remember { mutableStateOf(getCurrentTheme().secondary) }
    var teleTextColor = remember { mutableStateOf(Color.Yellow) }

    totalAutoCoralAttempts.intValue = autoCoralLevel4Scored.intValue+ autoCoralLevel3Scored.intValue+
            autoCoralLevel2Scored.intValue+ autoCoralLevel1Scored.intValue+ autoCoralLevel4Missed.intValue+
            autoCoralLevel3Missed.intValue+ autoCoralLevel2Missed.intValue+ autoCoralLevel1Missed.intValue

    LaunchedEffect(startTimer) {
        while (startTimer) {
            delay(15000)
            teleFlash.value = true
            startTimer = false
        }
    }

    LaunchedEffect(teleFlash.value) {
        while (teleFlash.value) {
            teleColor.value = Color.Green.copy(alpha = 0.8f)
            teleTextColor.value = Color.White
            delay(200)
            teleTextColor.value = Color.Yellow
            teleColor.value = getCurrentTheme().secondary
            delay(200)
        }
    }

    val context = LocalContext.current

    fun getColors(state: ToggleableState) = when(state){
        ToggleableState.On ->{
            backgroundColor.value = Color(0, 204, 102)
            textColor.value = Color.White
        }
        ToggleableState.Indeterminate -> {
            backgroundColor.value = Color.Yellow
            textColor.value = Color.Black
        }
        ToggleableState.Off ->{
            backgroundColor.value = Color.Black
            textColor.value = Color.White
        }
    }

    fun getNewState(state: ToggleableState) = when (state) {
        ToggleableState.Off -> ToggleableState.On
        ToggleableState.Indeterminate -> ToggleableState.Off
        ToggleableState.On -> ToggleableState.Indeterminate
    }
    fun getOldState(state: ToggleableState) = when (state) {
        ToggleableState.Off -> ToggleableState.Indeterminate
        ToggleableState.Indeterminate -> ToggleableState.On
        ToggleableState.On -> ToggleableState.Off
    }
    Row(Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(2 / 8f)){
        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().secondary),
            onClick = {
                val action: Array<Any> = try {
                    undoList.pop()
                } catch (e: EmptyStackException) {
                    arrayOf("empty")
                }
                when ((action[0] as String).lowercase()) {
                    "number" -> {
                        (action[1] as MutableIntState).value = action[2] as Int
                        redoList.push(arrayOf(action[0], action[1], action[2] as Int + 1))
                    }

                    "tristate" -> {
                        (action[1] as MutableState<ToggleableState>).value = action[2] as ToggleableState
                        (action[3] as MutableState<Color>).value = action[4] as Color
                        (action[5] as MutableState<Color>).value = action[6] as Color
                        redoList.push(
                            arrayOf(
                                action[0],
                                action[1],
                                getNewState((action[1] as MutableState<ToggleableState>).value),
                                action[3],
                                action[4],
                                action[5],
                                action[6]
                            )
                        )
                    }
                }

                if(saveData.value) {
                    teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
                }

            },
            modifier = Modifier.fillMaxWidth(1 / 2f)
        ) {
            Text(
                "U",
                color = Color.Yellow,
                fontSize = 23.sp
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().secondary),
            onClick = {
                val action: Array<Any> = try {
                    redoList.pop()
                } catch (e: EmptyStackException) {
                    arrayOf("empty")
                }
                when ((action[0] as String).lowercase()) {
                    "number" -> {
                        (action[1] as MutableIntState).value = action[2] as Int
                        undoList.push(arrayOf(action[0], action[1], action[2] as Int - 1))
                    }

                    "tristate" -> {
                        var temp = getOldState((action[1] as MutableState<ToggleableState>).value)
                        getColors(temp)
                        undoList.push(
                            arrayOf(
                                "tristate",
                                action[1],
                                temp,
                                action[3],
                                backgroundColor.value,
                                action[5],
                                textColor.value
                            )
                        )
                        (action[3] as MutableState<Color>).value = backgroundColor.value
                        (action[5] as MutableState<Color>).value = textColor.value

                    }
                }

                if(saveData.value) {
                    teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "R",
                color = Color.Yellow,
                fontSize = 23.sp
            )
        }
    }
        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (pageIndex.value == 0) Color.Green.copy(alpha = 0.5f) else getCurrentTheme().secondary
            ),
            onClick = {
                totalAutoCoralAttempts.value = 0
                backStack.push(AutoTeleSelectorNode.NavTarget.AutoScouting)
                pageIndex.value = 0
                if (saveData.value) {
                    teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
                }
            },
            modifier = Modifier.fillMaxWidth(1 / 4f)
        ) {
            Text(
                text = "Auto",
                color = if (pageIndex.value == 0) Color.White else if(pageIndex.value != 1) Color.Yellow else Color(122, 122, 0),
                fontSize = 23.sp
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (nodes.pageIndex.intValue == 1 && !teleFlash.value) Color.Yellow.copy(alpha = 0.5f) else if (!teleFlash.value) getCurrentTheme().secondary else teleColor.value
            ),
            onClick = {
                teleFlash.value = false
                backStack.push(AutoTeleSelectorNode.NavTarget.TeleScouting)
                pageIndex.value = 1
                if(saveData.value) {
                    teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
                }
            },
            modifier = Modifier.fillMaxWidth(1/3f)
        ) {
            Text(
                text = "Tele",
                color = if (nodes.pageIndex.intValue == 1 && !teleFlash.value) Color.White else if (!teleFlash.value) Color.Yellow else teleTextColor.value,
                        fontSize = 23.sp
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (pageIndex.value == 2) Color.Red.copy(alpha = 0.5f) else getCurrentTheme().secondary
            ),
            onClick = {
                totalAutoCoralAttempts.value = 0
                backStack.push(AutoTeleSelectorNode.NavTarget.EndGameScouting)
                pageIndex.value = 2
                if(saveData.value) {
                    teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
                }
            },
            modifier = Modifier.fillMaxWidth(8/16f)
        ) {
            Text(
                text = "End",
                color = if (pageIndex.value == 2) Color.White else if(pageIndex.value != 0) Color.Yellow else Color(122, 122, 0),
                fontSize = 23.sp
            )
        }

        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().secondary),
            onClick = {
                mainMenuDialog.value = true
                saveDataSit.value = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Main",
                maxLines = 1,
                color = Color.Yellow,
                fontSize = 23.sp
            )
        }
    }

    if(saveDataPopup.value) {
        BasicAlertDialog(
            onDismissRequest = { saveDataPopup.value = false },
            modifier = Modifier
                .clip(
                    RoundedCornerShape(5.dp)
                )
                .border(BorderStroke(3.dp, getCurrentTheme().primaryVariant), RoundedCornerShape(5.dp))
                .background(getCurrentTheme().secondary)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth(8f / 10f)
                .padding(5.dp)
                .fillMaxHeight(1 / 8f)) {
                Text(text = "Save data for team ${team.intValue}, match ${match.value}?",
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.TopCenter)
                )
                androidx.compose.material.OutlinedButton(
                    onClick = {
                        if(saveDataSit.value) {
                            teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
                            createScoutMatchDataFile(context, match.value, team.intValue, createOutput(team, robotStartPosition))
                            println(teamDataArray)
                            mainMenuBackStack.pop()

                            saveData.value = true
                        } else {
                            teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
                            createScoutMatchDataFile(context, match.value, team.intValue, createOutput(team, robotStartPosition))
                            match.value = (parseInt(match.value) + 1).toString()
                            stringMatch.value = match.value
                            reset()
                            backStack.push(AutoTeleSelectorNode.NavTarget.AutoScouting)

                            try{
                                team.intValue = getTeamsOnAlliance(match.value.betterParseInt(), isRedAliance.value)[tempRobotStart.value].number
                            }catch (e: Exception){}
                            stringTeam.value = team.intValue.toString()

                            loadData(parseInt(match.value), team, robotStartPosition)

                            saveData.value = false
                        }
                        saveDataPopup.value = false
                        teleFlash.value = false
                    },
                    border = BorderStroke(2.dp, getCurrentTheme().secondaryVariant),
                    colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(backgroundColor = getCurrentTheme().secondary, contentColor = getCurrentTheme().onSecondary),
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Text(text = "Yes", color = getCurrentTheme().error)
                }
                androidx.compose.material.OutlinedButton(
                    onClick = {
                        if(saveDataSit.value) {
                            println(teamDataArray)
                            mainMenuBackStack.pop()
                        } else {
                            match.value = (parseInt(match.value) + 1).toString()
                            stringMatch.value = match.value
                            reset()
                            backStack.push(AutoTeleSelectorNode.NavTarget.AutoScouting)

                            try{
                                team.intValue = getTeamsOnAlliance(match.value.betterParseInt(), isRedAliance.value)[tempRobotStart.value].number
                            }catch (e: Exception){}
                            stringTeam.value = team.intValue.toString()

                            loadData(parseInt(match.value), team, robotStartPosition)

                        }
                        saveDataPopup.value = false
                        saveData.value = false
                        teleFlash.value = false
                    },
                    border = BorderStroke(2.dp, getCurrentTheme().secondaryVariant),
                    colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(backgroundColor = getCurrentTheme().secondary, contentColor = getCurrentTheme().onSecondary),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Text(text = "No", color = getCurrentTheme().error)
                }
            }
        }
    }

//    if(saveData.value) {
//        teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
//    }

}
var tempRobotStart : MutableState<Int> = mutableStateOf(0)
var isRedAliance = mutableStateOf(false)
var teams = getTeamsOnAlliance(match.value.betterParseInt(),isRedAliance.value)