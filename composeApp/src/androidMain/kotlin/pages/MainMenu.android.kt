package pages

//import androidx.compose.material.*
import android.content.Context
import android.hardware.usb.UsbManager
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import blueAlliance
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.push
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import compKey
import createScoutMatchDataFolder
import defaultSecondary
import getCurrentTheme
import getLastSynced
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import matchData
import nodes.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.tahomarobotics.scouting.Client
import redAlliance
import sendDataUSB
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
        var selectedPlacement by remember { mutableStateOf(false) }
        var matchSyncedResource by remember { mutableStateOf(if (matchData == null) "crossmark.png" else "checkmark.png") }
        var teamSyncedResource by remember { mutableStateOf(if (teamData == null) "crossmark.png" else "checkmark.png") }
        var serverDialogOpen by remember { mutableStateOf(false) }

        var ipAddressErrorDialog by remember { mutableStateOf(false) }
        var deviceListOpen by remember { mutableStateOf(false) }
        val manager = context.getSystemService(Context.USB_SERVICE) as UsbManager

        var setEventCode by remember { mutableStateOf(false) }
        var tempCompKey by remember { mutableStateOf(compKey) }

        val deviceList = manager.deviceList

        var exportPopup by remember { mutableStateOf(false) }

        Column(modifier = Modifier.verticalScroll(ScrollState(0))) {
            DropdownMenu(expanded = deviceListOpen, onDismissRequest = { deviceListOpen = false }) {
                deviceList.forEach { (name, _) ->
                    Log.i("USB", name)
                    DropdownMenuItem(text = { Text(name) }, onClick = { sendDataUSB(context, name) })
                }
            }
            if (setEventCode) {
                Dialog(onDismissRequest = {
                    setEventCode = false
                    compKey = tempCompKey
                }) {
                    Column {
                        Text("Enter new event code")
                        TextField(tempCompKey, { tempCompKey = it })
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = {
                        backStack.push(RootNode.NavTarget.LoginPage)
                    }, modifier = Modifier
                        .scale(0.75f)
                        .align(Alignment.CenterStart)
                ) {
                    Text(text = "Back to Login", color = getCurrentTheme().onPrimary)
                }

                Text(
                    text = "Bear Metal Scout App", fontSize = 25.sp, modifier = Modifier.align(Alignment.Center)
                )

                OutlinedButton(
                    onClick = {
                        backStack.push(RootNode.NavTarget.Settings)
                    }, modifier = Modifier
                        .scale(0.75f)
                        .align(Alignment.CenterEnd)
                ) {
                    Text(text = "Settings", color = getCurrentTheme().onPrimary)
                }

            }
            HorizontalDivider(color = getCurrentTheme().onSurface, thickness = 2.dp)
            Row(modifier = Modifier.fillMaxWidth()) {
                var textLabel = ""
                when (robotStartPosition.value) {
                    0 -> textLabel = "Red 1"
                    1 -> textLabel = "Red 2"
                    2 -> textLabel = "Red 3"
                    3 -> textLabel = "Blue 1"
                    4 -> textLabel = "Blue 2"
                    5 -> textLabel = "Blue 3"
                    6 -> textLabel = "Red Strat"
                    7 -> textLabel = "Blue Strat"
                    8 -> textLabel = "Pits"
                }
                OutlinedButton(
                    content = { Text(textLabel, color = getCurrentTheme().onPrimary) },
                    onClick = { },
                    enabled = false,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, color = Color.Yellow),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = if (robotStartPosition.value < 3) redAlliance else if (robotStartPosition.value < 6) blueAlliance else if (robotStartPosition.value == 6) redAlliance else blueAlliance,
                        ),
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .padding(8.dp)
                        .height(80.dp)
                )
                androidx.compose.material.OutlinedTextField(
                    value = scoutName.value,
                    onValueChange = { scoutName.value = it },
                    label = { Text("Scout First and Last Name", color = getCurrentTheme().onPrimary) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Cyan,
                        unfocusedBorderColor = Color.Yellow,
                        cursorColor = getCurrentTheme().onPrimary,
                        textColor = getCurrentTheme().onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(80.dp)
                )
            }
            OutlinedButton(
                border = BorderStroke(3.dp, Color.Yellow),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
                contentPadding = PaddingValues(horizontal = 60.dp, vertical = 5.dp),
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
                    .padding(horizontal = 50.dp, vertical = 50.dp),
            ) {
                Text(
                    text = "Start Scouting", color = getCurrentTheme().onPrimary, fontSize = 35.sp
                )
            }

            OutlinedButton(
                border = BorderStroke(3.dp, Color.Yellow),
                shape = RoundedCornerShape(25.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
                onClick = {
                    val scope = CoroutineScope(Dispatchers.Default)
                    scope.launch {
                        sync(true, context)
                        teamSyncedResource = if (teamData != null) "checkmark.png" else "crossmark.png"
                        matchSyncedResource = if (matchData != null) "checkmark.png" else "crossmark.png"
                    }
//                    TBAInterface.getTBAData("/event/${compKey}/teams/keys")
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 50.dp, vertical = 50.dp),
            ) {
                Column {
                    Text(
                        text = "Sync",
                        color = getCurrentTheme().onPrimary,
                        fontSize = 35.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "Last synced ${getLastSynced()}",
                        fontSize = 12.sp,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(Modifier.fillMaxWidth(1f / 2f)) {
                        Text("Robot List")

                        Image(
                            painterResource(res = teamSyncedResource),
                            contentDescription = "status",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterEnd)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(Modifier.fillMaxWidth(1f / 2f)) {
                        Text("Match List")

                        Image(
                            painterResource(res = matchSyncedResource),
                            contentDescription = "status",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterEnd)
                        )
                    }
                }
            }

            OutlinedButton(
                border = BorderStroke(3.dp, Color.Yellow),
                shape = RoundedCornerShape(25.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
                onClick = {
                    exportPopup = true
                }) {
                Text("Export")
            }

            Box(modifier = Modifier.fillMaxSize()) {
                OutlinedButton(
                    border = BorderStroke(3.dp, Color.Yellow),
                    shape = RoundedCornerShape(25.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 15.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
                    onClick = {
                        setEventCode = true
                        teamSyncedResource = "crossmark.png"
                        matchSyncedResource = "crossmark.png"
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text("Set custom event key", fontSize = 9.sp)
                }
            }
            Text(tempCompKey)
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
                        .fillMaxWidth(8f / 10f)
                        .padding(5.dp)
                        .fillMaxHeight(2 / 8f)
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