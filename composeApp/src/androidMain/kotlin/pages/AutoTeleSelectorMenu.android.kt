package pages

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.components.backstack.BackStack
import defaultPrimaryVariant
import getCurrentTheme
import nodes.*
import org.jetbrains.compose.resources.load
import java.lang.Integer.parseInt

@Composable
actual fun AutoTeleSelectorMenu(
    match: MutableState<String>,
    team: MutableIntState,
    robotStartPosition: MutableIntState,
    selectAuto: MutableState<Boolean>,
    backStack: BackStack<AutoTeleSelectorNode.NavTarget>,
    mainMenuBackStack: BackStack<RootNode.NavTarget>
) {

    var pageName by remember { mutableStateOf("Auto") }
    var positionName by remember { mutableStateOf("") }
    val context = LocalContext.current
    var teamNumAsText by remember { mutableStateOf(team.intValue.toString()) }

    val isTeamInteracted = remember {MutableInteractionSource()}
    val isMatchInteracted = remember {MutableInteractionSource()}

    var first by remember { mutableStateOf(true) }

    if(first) {
        tempTeam = team.intValue
        tempMatch = match.value
    }

    when {
//        openError.value -> {
//            InternetErrorAlert {
//                openError.value = false
//                mainMenuBackStack.pop()
//            }
//        }
    }

    when (robotStartPosition.intValue){
        0 -> {positionName = "R1"}
        1 -> {positionName = "R2"}
        2 -> {positionName = "R3"}
        3 -> {positionName = "B1"}
        4 -> {positionName = "B2"}
        5 -> {positionName = "B3"}
    }
    pageName = if (!selectAuto.value) {
        "Auto"
    } else {
        "Tele"
    }

    if(teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] != null) {
        teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
        loadData(parseInt(match.value), team, robotStartPosition)
        println("loaded")
    } else {
        teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
//        println("saved")
    }

    // Saves data before changing team or match number
//    if((isTeamInteracted.collectIsFocusedAsState().value || isMatchInteracted.collectIsFocusedAsState().value)) {
//        println("saving data")
//        println(match.value)
//        // This line below sets the data for match 1 to what the current variables are.
//        teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
//        println(teamDataArray)
//    }

    Column {
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
                    team.intValue = parseInt(teamNumAsText)
                    if (filteredText.isNotEmpty() && !filteredText.contains(','))
                        loadData(parseInt(match.value), team, robotStartPosition)
//                    println(createOutput(team, robotStartPosition))
                },
                interactionSource = isTeamInteracted,
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
                    teamDataArray[TeamMatchKey(parseInt(tempMatch), tempTeam)] = createOutput(
                        mutableIntStateOf(tempTeam), robotStartPosition)

                    val temp = value.filter { it.isDigit() }
                    if(temp.isNotEmpty()) {
                        match.value = temp.slice(0..<temp.length.coerceAtMost(5))
//                        teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
                        loadData(parseInt(match.value), team, robotStartPosition)
                        println("switched to ${match.value}")
                        println(teamDataArray)
                    }
                    tempMatch = match.value
//                    if (match.value != "") {
//                        loadData(parseInt(nodes.match.value), team, robotStartPosition)
//                        teamDataArray[TeamMatchKey(parseInt(match.value), team.intValue)] = createOutput(team, robotStartPosition)
//                        exportScoutData(context)
//                    }
//                    try {
//                        setTeam(team, nodes.match, robotStartPosition.intValue)
//                    } catch (e: JSONException) {
//                        openError.value = true
//                    }
                },
                interactionSource = isMatchInteracted,
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