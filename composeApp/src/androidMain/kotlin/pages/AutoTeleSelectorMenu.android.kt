package pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.input.UndoState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.pop
import composables.InternetErrorAlert
import com.bumble.appyx.components.backstack.operation.pop
import com.bumble.appyx.components.backstack.operation.push
import defaultPrimaryVariant
import defaultSecondary
import exportScoutData
import getCurrentTheme
import nodes.*
import org.json.JSONException
import setTeam
import java.lang.Integer.parseInt
import java.util.EmptyStackException

//@RequiresApi(Build.VERSION_CODES.R)
@Composable
actual fun AutoTeleSelectorMenuTop(
    match: MutableState<String>,
    team: MutableIntState,
    robotStartPosition: MutableIntState,
    backStack: BackStack<AutoTeleSelectorNode.NavTarget>,
    mainMenuBackStack: BackStack<RootNode.NavTarget>
) {
    var positionName by remember { mutableStateOf("") }
    val context = LocalContext.current
    var teamNumAsText by remember { mutableStateOf(team.intValue.toString()) }

    when {
        openError.value -> {
            InternetErrorAlert {
                openError.value = false
                mainMenuBackStack.pop()
            }
        }
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
        HorizontalDivider(color = defaultPrimaryVariant, thickness = 4.dp)


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
                color = defaultPrimaryVariant,
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
                    val filteredText = value.filter { it.isDigit() }
                    teamNumAsText =
                        filteredText.slice(0..<filteredText.length.coerceAtMost(5))//WHY IS FILTER NOT FILTERING
                    if (teamNumAsText.isNotEmpty() || teamNumAsText.contains(','))
                        team.intValue = parseInt(teamNumAsText)
                    println(createOutput(team, robotStartPosition))
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
                    .padding(horizontal = 25.dp)
                    .width(125.dp),
                textStyle = TextStyle.Default.copy(fontSize = 31.sp),
                singleLine = true,
            )

            VerticalDivider(
                color = defaultPrimaryVariant,
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
                    val temp = value.filter { it.isDigit() }
                    match.value = temp.slice(0..<temp.length.coerceAtMost(5))
                    if (match.value != "") {
                        loadData(parseInt(nodes.match.value), team, robotStartPosition)
                        teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
                        exportScoutData(context)
                    }
                    try {
                        setTeam(team, nodes.match, robotStartPosition.intValue)
                    } catch (e: JSONException) {
                        openError.value = true
                    }
                    teamNumAsText = team.intValue.toString()
                    backStack.push(AutoTeleSelectorNode.NavTarget.AutoScouting)
                },
                modifier = Modifier.fillMaxWidth(),
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

        }
        HorizontalDivider(color = defaultPrimaryVariant, thickness = 3.dp)
    }
}











@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun AutoTeleSelectorMenuBottom(
    robotStartPosition: MutableIntState,
    team: MutableIntState,

    selectPage: MutableState<String>,
    backStack: BackStack<AutoTeleSelectorNode.NavTarget>,
    mainMenuBackStack: BackStack<RootNode.NavTarget>,
) {
    var pageName = mutableListOf("Auto","Tele","End","Next")
    var pageIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current
    var backgroundColor = remember { mutableStateOf(Color.Black) }
    var textColor = remember { mutableStateOf(Color.White) }
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

    fun bob() {
        mainMenuBackStack.pop()
        teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
        exportScoutData(context)
    }
//fun bob() {
//        mainMenuBackStack.pop()
//        teamDataArray[robotStartPosition.intValue]?.set(parseInt(match.value), createOutput(team, robotStartPosition))
//        exportScoutData(context)
//    }
    Row(Modifier.fillMaxWidth()) {
        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
            onClick = {
                val action : Array<Any> = try {
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
                        redoList.push(arrayOf(action[0], action[1], getNewState((action[1] as MutableState<ToggleableState>).value), action[3], action[4], action[5], action[6]))
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(1/5f)
        ) {
            Text(
                "Undo",
                color = Color.Yellow,
                fontSize = 24.sp
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
            onClick = {
                val action : Array<Any> = try {
                    redoList.pop()
                } catch (e: EmptyStackException) {
                    arrayOf("empty")
                }
                when ((action[0] as String).lowercase()) {
                    "number" -> {
                        (action[1] as MutableIntState).value = action[2] as Int
                        undoList.push(arrayOf(action[0], action[1], action[2] as Int-1))
                    }
                    "tristate" -> {
                        var temp = getOldState((action[1] as MutableState<ToggleableState>).value)
                        getColors(temp)
                        undoList.push(arrayOf("tristate", action[1], temp, action[3], backgroundColor.value, action[5], textColor.value))
                        (action[3] as MutableState<Color>).value = backgroundColor.value
                        (action[5] as MutableState<Color>).value = textColor.value

                    }
                }

            },
            modifier = Modifier.fillMaxWidth(1/4f)
        ) {
            Text(
                "Redo",
                color = Color.Yellow,
                fontSize = 24.sp
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
            onClick = {
                when (pageName[pageIndex]) {
                    "Auto" -> {
                        backStack.push(AutoTeleSelectorNode.NavTarget.TeleScouting)
                        pageIndex++
                    }

                    "Tele" -> {
                        backStack.push(AutoTeleSelectorNode.NavTarget.EndGameScouting)
                        pageIndex++
                    }

                    "Endgame" -> {
                        teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
                        println(teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)])
                        match.value = (parseInt(match.value) + 1).toString()
                        reset()
                        loadData(parseInt(match.value), team, robotStartPosition)
                        try {
                            setTeam(team, match, robotStartPosition.intValue)
                        } catch (e: JSONException) {
                            openError.value = true
                        }
                        backStack.push(AutoTeleSelectorNode.NavTarget.AutoScouting)
                        pageIndex = 0
                    }
                }

            },
            modifier = Modifier.fillMaxWidth(1/3f)
        ) {
            Text(
                text = pageName[pageIndex+1],
                color = Color.Yellow,
                fontSize = 24.sp
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
            onClick = {
                backStack.pop()

                if(pageIndex == 0){
                    teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
                    println(teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)])
                    match.value = (parseInt(match.value) + 1).toString()
                    reset()
                    backStack.push(AutoTeleSelectorNode.NavTarget.AutoScouting)
                    exportScoutData(context)
                    loadData(parseInt(match.value), team, robotStartPosition)
                    setTeam(team, match, robotStartPosition.intValue)
                }else{
                    pageIndex--
                }
            },
            modifier = Modifier.fillMaxWidth(1/2f)
        ) {
            Text(
                text = "Back",
                color = Color.Yellow,
                fontSize = 24.sp
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, color = Color.Yellow),
            shape = RoundedCornerShape(1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
            onClick = {
                bob()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Main",
                color = Color.Yellow,
                fontSize = 24.sp
            )
        }
    }
}
