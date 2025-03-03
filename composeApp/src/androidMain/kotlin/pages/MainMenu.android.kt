package pages

//import androidx.compose.material.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.push
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import createScoutMatchDataFolder
import defaultPrimaryVariant
import defaultSecondary
import getCurrentTheme
import getLastSynced
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import matchData
import nodes.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.tahomarobotics.scouting.Client
import sendMatchData
import sendPitsData
import sendStratData
import sync
import teamData
import java.lang.Integer.parseInt

actual class MainMenu actual constructor(
    buildContext: BuildContext,
    private val backStack: BackStack<RootNode.NavTarget>,
    private val robotStartPosition: MutableIntState,
    private val scoutName: MutableState<String>,
    private val comp: MutableState<String>,
    private val team: MutableIntState
) : Node(buildContext = buildContext) {

    @OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
    @Composable
    actual override fun View(modifier: Modifier) {
        val context = LocalContext.current
        var matchSynced by remember { mutableStateOf(matchData != null) }
        var teamSynced by remember { mutableStateOf(teamData != null) }
        
        var exportPopup by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .verticalScroll(ScrollState(0))
                .padding(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    border = BorderStroke(3.dp, Color.Yellow),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
                    onClick = {
                        backStack.push(RootNode.NavTarget.LoginPage)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(8.dp, 0.dp, 8.dp, 0.dp)
                ) {
                    Icon(Icons.Rounded.ChevronLeft, "Back")
                }

                var textLabel = ""
                when (robotStartPosition.value) {
                    0 -> textLabel = "Red 1 Match"
                    1 -> textLabel = "Red 2 Match"
                    2 -> textLabel = "Red 3 Match"
                    3 -> textLabel = "Blue 1 Match"
                    4 -> textLabel = "Blue 2 Match"
                    5 -> textLabel = "Blue 3 Match"
                    6 -> textLabel = "Red Strat"
                    7 -> textLabel = "Blue Strat"
                    8 -> textLabel = "Pits"
                }
                Text(
                    text = textLabel,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                )

                OutlinedButton(
                    border = BorderStroke(3.dp, Color.Yellow),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
                    onClick = {
                        backStack.push(RootNode.NavTarget.Settings)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(8.dp, 0.dp, 8.dp, 0.dp)
                ) {
                    Text(text = "Settings", color = getCurrentTheme().onPrimary)
                }

            }

            HorizontalDivider(
                color = defaultPrimaryVariant,
                thickness = 3.dp,
                modifier = Modifier.padding(8.dp)
            )
            
            androidx.compose.material.OutlinedTextField(
                value = scoutName.value,
                onValueChange = { scoutName.value = it },
                label = { Text("Scout First and Last Name", color = getCurrentTheme().onPrimary) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Cyan,
                    unfocusedBorderColor = Color.Yellow,
                    cursorColor = getCurrentTheme().onPrimary,
                    textColor = getCurrentTheme().onPrimary,
                    backgroundColor = defaultSecondary,
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.6f)
                    .align(Alignment.CenterHorizontally)
            )
            
            OutlinedButton(
                border = BorderStroke(3.dp, Color.Yellow),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
                onClick = {
                    val scope = CoroutineScope(Dispatchers.Default)
                    scope.launch {
                        sync(true, context)
                        teamSynced = teamData != null
                        matchSynced = matchData != null
                    }
//                    TBAInterface.getTBAData("/event/${compKey}/teams/keys")
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp),
            ) {
                Column {
                    Text(
                        text = "Sync",
                        color = getCurrentTheme().onPrimary,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp)
                    )

                    Text(
                        text = "Last synced ${getLastSynced()}",
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        Modifier
                            .fillMaxWidth(1f / 2f)
                            .padding(0.dp, 8.dp, 0.dp, 0.dp)
                    ) {
                        Text("Robot List", modifier = Modifier.align(Alignment.CenterStart))

                        Icon(
                            if (teamSynced) Icons.Rounded.CheckCircleOutline else Icons.Rounded.ErrorOutline,
                            contentDescription = "team sync status",
                            tint = if (teamSynced) Color.Green else Color.Red,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterEnd)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        Modifier
                            .fillMaxWidth(1f / 2f)
                            .padding(0.dp, 0.dp, 0.dp, 8.dp)
                    ) {
                        Text("Match List", modifier = Modifier.align(Alignment.CenterStart))
                        
                        Icon(
                            if (matchSynced) Icons.Rounded.CheckCircleOutline else Icons.Rounded.ErrorOutline,
                            contentDescription = "match sync status",
                            tint = if (matchSynced) Color.Green else Color.Red,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterEnd)
                        )
                    }
                }
            }

            OutlinedButton(
                border = BorderStroke(3.dp, Color.Yellow),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
                onClick = {
                    if (robotStartPosition.value < 6) {
                        val scope = CoroutineScope(Dispatchers.Default)
                        scope.launch {
                            sync(false, context)
                        }

                        createScoutMatchDataFolder(context)

                        loadData(parseInt(match.value), team, robotStartPosition)
                        backStack.push(RootNode.NavTarget.MatchScouting)
                    } else if (robotStartPosition.value < 8) {
                        val redAlliance = robotStartPosition.value == 6
                        setContext(redAlliance)
                        backStack.push(RootNode.NavTarget.StratScreen)
                        loadStratData(stratMatch, redAlliance)
                    } else {
                        backStack.push(RootNode.NavTarget.PitsScouting)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
            ) {
                Text(
                    text = "Start Scouting", color = getCurrentTheme().onPrimary
                )
            }

            OutlinedButton(
                border = BorderStroke(3.dp, Color.Yellow),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
                onClick = {
                    exportPopup = true
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
            ) { Text(text = "Export Data", color = getCurrentTheme().onPrimary) }
        }

        if (exportPopup) {
            BasicAlertDialog(
                onDismissRequest = { exportPopup = false }, modifier = Modifier
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
                    .border(BorderStroke(3.dp, getCurrentTheme().primaryVariant), RoundedCornerShape(5.dp))
                    .background(getCurrentTheme().secondary)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(5.dp)
                        .fillMaxHeight(0.25f)
                ) {
                    Text(
                        text = "What do you want to export?",
                        modifier = Modifier
                            .padding(5.dp)
                            .align(Alignment.TopCenter)
                    )
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        androidx.compose.material.OutlinedButton(
                            onClick = {
                                val scope = CoroutineScope(Dispatchers.Default)
                                scope.launch {
                                    if (client == null) client = Client()

                                    if (!client!!.isConnected) {
                                        client!!.discoverAndConnect()
                                    }
                                }
                                scope.launch {
                                    while (client == null || client?.isConnected != true) {
//                                     println("client is null or not connected")
                                    }
                                    sendMatchData(
                                        context = context,
                                        client = client!!,
                                    )
                                    client!!.disconnect()
                                }
                                exportPopup = false
                            },
                            border = BorderStroke(2.dp, getCurrentTheme().secondaryVariant),
                            colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                backgroundColor = getCurrentTheme().secondary,
                                contentColor = getCurrentTheme().onSecondary
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Match", color = getCurrentTheme().error)
                        }
                        androidx.compose.material.OutlinedButton(
                            onClick = {
                                val scope = CoroutineScope(Dispatchers.Default)
                                scope.launch {
                                    if (client == null) client = Client()

                                    if (!client!!.isConnected) {
                                        client!!.discoverAndConnect()
                                    }
                                }
                                scope.launch {
                                    while (client == null || client?.isConnected != true) {
//                                     println("client is null or not connected")
                                    }
                                    sendStratData(
                                        context = context,
                                        client = client!!,
                                    )
                                }
                                exportPopup = false
                            },
                            border = BorderStroke(2.dp, getCurrentTheme().secondaryVariant),
                            colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                backgroundColor = getCurrentTheme().secondary,
                                contentColor = getCurrentTheme().onSecondary
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Strat", color = getCurrentTheme().error)
                        }

                        androidx.compose.material.OutlinedButton(
                            onClick = {
                                val scope = CoroutineScope(Dispatchers.Default)
                                scope.launch {
                                    if (client == null) client = Client()

                                    if (!client!!.isConnected) {
                                        client!!.discoverAndConnect()
                                    }
                                }
                                scope.launch {
                                    while (client == null || client?.isConnected != true) {
//                                     println("client is null or not connected")
                                    }
                                    sendPitsData(
                                        context = context,
                                        client = client!!,
                                    )
                                }
                                exportPopup = false
                            },
                            border = BorderStroke(2.dp, getCurrentTheme().secondaryVariant),
                            colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                backgroundColor = getCurrentTheme().secondary,
                                contentColor = getCurrentTheme().onSecondary
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Pits", color = getCurrentTheme().error)
                        }
                    }
                }
            }
        }
    }
}

val openError = mutableStateOf(false)