package pages

//import androidx.compose.material.*
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
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
import androidx.core.content.ContextCompat.startActivity
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.push
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.google.gson.Gson
import com.google.gson.JsonObject
import compKey
import createScoutMatchDataFolder
import createScoutPitsDataFolder
import createScoutStratDataFolder
import defaultPrimaryVariant
import defaultSecondary
import getCurrentTheme
import isTBAMTeamDataSynced
import isTBAMatchDataOld
import isTBAMatchDataSynced
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import loadMatchDataFiles
import loadPitsDataFiles
import loadStratDataFiles
import nodes.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.json.JSONException
import org.tahomarobotics.scouting.Client
import sendMatchData
import sendPitsData
import sendStratData
import setTeam
import syncMatches
import syncTeams
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
        val activity = context as ComponentActivity

        var matchSynced by remember { mutableStateOf(isTBAMatchDataSynced(compKey)) }
        var matchOutOfDate by remember { mutableStateOf(isTBAMatchDataOld(compKey)) }
        var teamSynced by remember { mutableStateOf(isTBAMTeamDataSynced(compKey)) }

        var exportPopup by remember { mutableStateOf(false) }

        var isInternetAvailable by remember { mutableStateOf(isInternetAvailable(context)) }
        LaunchedEffect(Unit) {
            while (true) {
                isInternetAvailable = isInternetAvailable(context)
                delay(1000) // Check every second
            }
        }

        var first by remember { mutableStateOf(true) }

        if (first) {
            createScoutMatchDataFolder(context)
            loadMatchDataFiles()

            createScoutPitsDataFolder(context)
            loadPitsDataFiles()

            createScoutStratDataFolder(context)
            loadStratDataFiles()

            first = false
        }

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
                        syncTeams(context)
                        teamSynced = isTBAMTeamDataSynced(compKey)
                        syncMatches(context)
                        matchOutOfDate = isTBAMatchDataOld(compKey)
                        matchSynced = isTBAMatchDataSynced(compKey)
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
                            if (matchSynced) Icons.Rounded.CheckCircleOutline else if (matchOutOfDate) Icons.AutoMirrored.Rounded.HelpOutline else Icons.Rounded.ErrorOutline,
                            contentDescription = "match sync status",
                            tint = if (matchSynced) Color.Green else if (matchOutOfDate) Color.Yellow else Color.Red,
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
                    if (scoutName.value != "") {
                        if (robotStartPosition.value < 6) {
                            val scope = CoroutineScope(Dispatchers.Default)
                            scope.launch {
                                syncTeams(context)
                                syncMatches(context)
                            }

                            createScoutMatchDataFolder(context)

                            loadData(parseInt(match.value), team, robotStartPosition)
                            try {
                                setTeam(team, nodes.match, robotStartPosition.intValue)
                            } catch (e: JSONException) {
                                openError.value = true
                            }
                            stringTeam.value = team.value.toString()

                            backStack.push(RootNode.NavTarget.MatchScouting)
                        } else if (robotStartPosition.value < 8) {
                            val redAlliance = robotStartPosition.value == 6
                            setContext(redAlliance)
                            backStack.push(RootNode.NavTarget.StratScreen)
                            loadStratData(stratMatch, redAlliance)
                        } else {
                            backStack.push(RootNode.NavTarget.PitsScouting)
                        }
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
                    val array = teamDataArray.getOrPut(compKey) { hashMapOf() }
                    for ((key, value) in array.entries) {
                        for ((key1, value1) in value.entries) {
                            println(value1)
                        }
                    }
                    // Step 1: Turn on WiFi if not on already
                    if (!isInternetAvailable(context)) {
                        val panelIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                        startActivity(context, panelIntent, null)
                    }
                    // Step 2: Connect to the server
                    exportPopup = true
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
            ) { Text(text = "Export Data", color = getCurrentTheme().onPrimary) }
        }

        if (exportPopup) {
            BasicAlertDialog(
                onDismissRequest = { }, modifier = Modifier
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
                    .border(BorderStroke(3.dp, getCurrentTheme().primaryVariant), RoundedCornerShape(5.dp))
                    .background(getCurrentTheme().secondary)
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        Icon(
                            if (isInternetAvailable) Icons.Rounded.CheckCircleOutline else Icons.Rounded.ErrorOutline,
                            contentDescription = "team sync status",
                            tint = if (isInternetAvailable) Color.Green else Color.Red,
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Text("WiFi Connection", modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (!isInternetAvailable(context)) {
                            androidx.compose.material.OutlinedButton(
                                onClick = {
                                    val panelIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                                    startActivity(context, panelIntent, null)
                                },
                                border = BorderStroke(3.dp, getCurrentTheme().secondaryVariant),
                                shape = RoundedCornerShape(12.dp),
                                colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = defaultSecondary,
                                    contentColor = getCurrentTheme().onPrimary
                                ),
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(8.dp)

                            ) {
                                Text(text = "Settings", color = getCurrentTheme().onPrimary)
                            }
                        }

                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (isInternetAvailable) {
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
                                        if (robotStartPosition.value < 6) {
                                            sendMatchData(
                                                client = client!!,
                                            )
                                            client!!.disconnect()

                                        } else if (robotStartPosition.value < 8) {
                                            sendStratData(
                                                client = client!!,
                                            )
                                        } else {
                                            sendPitsData(
                                                client = client!!,
                                            )
                                        }
                                    }
                                },
                                border = BorderStroke(2.dp, getCurrentTheme().secondaryVariant),
                                colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = getCurrentTheme().secondary,
                                    contentColor = getCurrentTheme().onSecondary
                                ),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Text(text = "Export", color = getCurrentTheme().error)
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        androidx.compose.material.OutlinedButton(
                            onClick = {
                                if (isInternetAvailable(context)) {
                                    val panelIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                                    startActivity(context, panelIntent, null)
                                }
                                exportPopup = false
                            },
                            border = BorderStroke(2.dp, getCurrentTheme().secondaryVariant),
                            colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                backgroundColor = getCurrentTheme().secondary,
                                contentColor = getCurrentTheme().onSecondary
                            ),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(text = "Done", color = getCurrentTheme().error)
                        }
                    }
                }

            }
        }
    }
}

val openError = mutableStateOf(false)

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}