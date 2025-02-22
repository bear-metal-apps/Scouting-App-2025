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
import defaultError
import defaultPrimaryVariant
import defaultSecondary
import exportScoutData
import getCurrentTheme
import nodes.*
import org.json.JSONException
import setTeam
import java.lang.Integer.parseInt
import java.util.EmptyStackException

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.R)
@Composable
actual fun AutoTeleSelectorMenuTop(
    match: MutableState<String>,
    team: MutableIntState,
    robotStartPosition: MutableIntState,
    pageIndex : MutableIntState
) {
    var positionName by remember { mutableStateOf("") }
    val context = LocalContext.current
    var teamNumAsText by remember { mutableStateOf(team.intValue.toString()) }
    var pageName = mutableListOf("A","T","E")

    var first by remember { mutableStateOf(true) }

    // When the user first opens the app, the tempTeam and tempMatch variables are assigned to the current match and team so they can be saved when the user changes the match or team!
    if(first) {
        tempTeam = team.intValue
        tempMatch = match.value

        saveData.value = false

        first = false
    }

    when {
//        openError.value -> {
//            InternetErrorAlert {
//                openError.value = false
//                mainMenuBackStack.pop()
//            }
//        }
    }

    when (robotStartPosition.intValue) {
        0 -> {
            positionName = "R1"
        }

        1 -> {
            positionName = "R2"
        }

        2 -> {
            positionName = "R3"
        }

        3 -> {
            positionName = "B1"
        }

        4 -> {
            positionName = "B2"
        }

        5 -> {
            positionName = "B3"
        }
    }
    Column() {
        HorizontalDivider(color = getCurrentTheme().primaryVariant, thickness = 4.dp)

        Row(
            Modifier
                .align(Alignment.CenterHorizontally)
                .height(IntrinsicSize.Min)
        ) {
            Text(
                text = positionName,
                modifier = Modifier
                    .scale(1.2f)
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 25.dp),
                fontSize = 28.sp
            )

            VerticalDivider(
                color = getCurrentTheme().primaryVariant,
                thickness = 3.dp
            )
            val textColor = if (positionName.lowercase().contains("b")) {
                Color(red = 0.1f, green = Color.Cyan.green - 0.4f, blue = Color.Cyan.blue - 0.2f)
            } else {
                Color.Red
            }

            TextField(
                value = team.intValue.toString(),
                onValueChange = { value ->
                    if(saveData.value) {
                        teamDataArray[TeamMatchStartKey(parseInt(tempMatch), tempTeam, robotStartPosition.intValue)] = createOutput(mutableIntStateOf(tempTeam), robotStartPosition)
                        createScoutMatchDataFile(context, tempMatch, tempTeam, createOutput(mutableIntStateOf(tempTeam), robotStartPosition))
                    }

                    saveData.value = false

                    val filteredText = value.filter { it.isDigit() }
                    if (filteredText.isNotEmpty() && !filteredText.contains(','))
                        teamNumAsText =
                            filteredText.slice(0..<filteredText.length.coerceAtMost(5))//WHY IS FILTER NOT FILTERING
                        team.intValue = parseInt(teamNumAsText)
                        loadData(parseInt(match.value), team, robotStartPosition)

                    tempTeam = team.intValue

                    println(robotStartPosition.intValue.toString())

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
                value = match.value,
                onValueChange = { value ->
                    if(saveData.value) {
                        teamDataArray[TeamMatchStartKey(parseInt(tempMatch), tempTeam, robotStartPosition.intValue)] = createOutput(mutableIntStateOf(tempTeam), robotStartPosition)
                        createScoutMatchDataFile(context, tempMatch, tempTeam, createOutput(mutableIntStateOf(tempTeam), robotStartPosition))
                    }

                    saveData.value = false

                    val temp = value.filter { it.isDigit() }
                    if(temp.isNotEmpty()) {
                        match.value = temp.slice(0..<temp.length.coerceAtMost(5))
                        loadData(parseInt(match.value), team, robotStartPosition)
//                        teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
                        exportScoutData(context) // Does nothing

                        try {
                            setTeam(team, nodes.match, robotStartPosition.intValue)
                        } catch (e: JSONException) {
                            openError.value = true
                        }

                    }

                    tempMatch = match.value

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
            Text(
                text = pageName[pageIndex.value],
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 25.dp),
                fontSize = 28.sp
            )
        }
        HorizontalDivider(color = getCurrentTheme().primaryVariant, thickness = 3.dp)
    }
}











@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
actual fun AutoTeleSelectorMenuBottom(
    robotStartPosition: MutableIntState,
    team: MutableIntState,
    pageIndex: MutableIntState,
    backStack: BackStack<AutoTeleSelectorNode.NavTarget>,
    mainMenuBackStack: BackStack<RootNode.NavTarget>,
    mainMenuDialog: MutableState<Boolean>
) {
    var backgroundColor = remember { mutableStateOf(Color.Black) }
    var textColor = remember { mutableStateOf(Color.White) }

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
            colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
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
            colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
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
            colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
            onClick = {
                backStack.push(AutoTeleSelectorNode.NavTarget.AutoScouting)
                pageIndex.value = 0
                if(saveData.value) {
                    teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
                }
            },
            modifier = Modifier.fillMaxWidth(1/4f)
        ) {
            Text(
                text = "Auto",
                color = Color.Yellow,
                fontSize = 23.sp
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
            onClick = {
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
                color = Color.Yellow,
                fontSize = 23.sp
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
            onClick = {
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
                color = Color.Yellow,
                fontSize = 23.sp
            )
        }

        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
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
            modifier = Modifier.clip(
                RoundedCornerShape(5.dp)
            ).border(BorderStroke(3.dp, getCurrentTheme().primaryVariant), RoundedCornerShape(5.dp))
                .background(getCurrentTheme().secondary)
        ) {
            Box(modifier = Modifier.fillMaxWidth(8f / 10f).padding(5.dp).fillMaxHeight(1/8f)) {
                Text(text = "Save data for team ${team.intValue}, match ${match.value}?",
                    modifier = Modifier.padding(5.dp).align(Alignment.TopCenter)
                )
                androidx.compose.material.OutlinedButton(
                    onClick = {
                        if(saveDataSit.value) {
                            teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
                            createScoutMatchDataFile(context, match.value, team.intValue, createOutput(team, robotStartPosition))
                            println(teamDataArray)
                            mainMenuBackStack.pop()
                        } else {
                            teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
                            createScoutMatchDataFile(context, match.value, team.intValue, createOutput(team, robotStartPosition))
                            match.value = (parseInt(match.value) + 1).toString()
                            reset()
                            backStack.push(AutoTeleSelectorNode.NavTarget.AutoScouting)
                            println(teamDataArray)
                        }
                        saveDataPopup.value = false
                        saveData.value = true
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
                            reset()
                            backStack.push(AutoTeleSelectorNode.NavTarget.AutoScouting)
                            println(teamDataArray)
                        }
                        saveDataPopup.value = false
                        saveData.value = false
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

    if(teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] != null) {
        teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
        loadData(parseInt(match.value), team, robotStartPosition)
//        if(first) {
//            loadData(parseInt(match.value), team, robotStartPosition)
//            first = false
//        } else {
//            teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
////            loadData(parseInt(match.value), team, robotStartPosition)
//        }
    } else {
        if(saveData.value) {
            teamDataArray[TeamMatchStartKey(parseInt(match.value), team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
        }
    }

}