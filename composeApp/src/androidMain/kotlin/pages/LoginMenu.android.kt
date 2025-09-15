package pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import blueAlliance
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.push
import com.google.gson.Gson
import com.google.gson.JsonObject
import compKey
import createTabletDataFile
import deleteAllTBAMatchData
import deleteAllTBATeamData
import deleteScoutMatchData
import deleteScoutPitsData
import deleteScoutStratData
import getCurrentTheme
import grabTabletDataFile
import initFileMaker
import nodes.*
import redAlliance
import writeTabletDataFile
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun LoginMenu(
    backStack: BackStack<RootNode.NavTarget>,
    comp: MutableState<String>,
    robotStartPosition: MutableIntState
) {
    val context = LocalContext.current

    initFileMaker(context)
    createTabletDataFile(context)
    
    val logo = File("Logo.png")
    var compDD by remember { mutableStateOf(false) }
    var typeDD by remember { mutableStateOf(false) }
    var deleteData by remember { mutableStateOf(false) }
    val tbaMatches = listOf(
        "2025wasno",
        "2025wabon",
        "2025waahs",
        "2025pncmp",
        "2025hop",
        "2025cc"
    )

    // Cannot get robotStartPosition variable in rootnode from FileMaker.kt, so doing some logic here:
    val gson = Gson()
    val tabletData = gson.fromJson(grabTabletDataFile(), JsonObject::class.java)
    if (tabletData != null && !tabletData.isEmpty && tabletData != JsonObject() && tabletData.has("robotStartPosition")) {
        if (robotStartPosition.intValue != 8) {
            robotStartPosition.intValue = tabletData.get("robotStartPosition").asInt
            println("loaded robot start position: ${robotStartPosition.intValue}")
        }
    } else {
        writeTabletDataFile(context, createTabletDataOutput(0))
    }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "Login",
            fontSize = 32.sp,
            color = getCurrentTheme().onPrimary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        )
        HorizontalDivider(
            color = getCurrentTheme().primaryVariant,
            thickness = 3.dp,
            modifier = Modifier.padding(8.dp)
        )
        Row {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.6f)
            ) {
                OutlinedButton(
                    onClick = { compDD = true },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(3.dp, color = getCurrentTheme().primaryVariant),
                    colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Competition: ${comp.value}",
                            color = getCurrentTheme().onPrimary,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Drop Down",
                            tint = getCurrentTheme().onPrimary,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }
                DropdownMenu(
                    expanded = compDD,
                    onDismissRequest = { compDD = false; },
                    modifier = Modifier.background(color = getCurrentTheme().onSurface)
                ) {
                    DropdownMenuItem(
                        onClick = {
                            if (comp.value != "Glacier Peak") {
                                comp.value = "Glacier Peak"
                                compKey = tbaMatches[0]
//                            teamData?.clear()
//                            matchData?.clear()
                            }
                            compDD = false
                        },
                        text = {
                            Text(
                                text = "Glacier Peak",
                                color = getCurrentTheme().onPrimary,
                                modifier = Modifier.background(color = getCurrentTheme().onSurface)
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            if (comp.value != "Bonney Lake") {
                                comp.value = "Bonney Lake"
                                compKey = tbaMatches[1]
//                            teamData?.clear()
//                            matchData?.clear()
                            }
                            compDD = false
                        },
                        text = {
                            Text(
                                text = "Bonney Lake",
                                color = getCurrentTheme().onPrimary,
                                modifier = Modifier.background(color = getCurrentTheme().onSurface)
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            if (comp.value != "Auburn") {
                                comp.value = "Auburn"
                                compKey = tbaMatches[2]
//                            teamData?.clear()
//                            matchData?.clear()
                            }
                            compDD = false
                        },
                        text = {
                            Text(
                                text = "Auburn",
                                color = getCurrentTheme().onPrimary,
                                modifier = Modifier.background(color = getCurrentTheme().onSurface)
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            if (comp.value != "Cheney") {
                                comp.value = "Cheney"
                                compKey = tbaMatches[3]
//                            teamData?.clear()
//                            matchData?.clear()
                            }
                            compDD = false
                        },
                        text = {
                            Text(
                                text = "DCMP",
                                color = getCurrentTheme().onPrimary,
                                modifier = Modifier.background(color = getCurrentTheme().onSurface)
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            if (comp.value != "Houston") {
                                comp.value = "Houston"
                                compKey = tbaMatches[4]
//                            teamData?.clear()
//                            matchData?.clear()
                            }
                            compDD = false

                        },
                        text = {
                            Text(
                                text = "Houston",
                                color = getCurrentTheme().onPrimary,
                                modifier = Modifier.background(color = getCurrentTheme().onSurface)
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            if (comp.value != "Chezy Champs") {
                                comp.value = "Chezy Champs"
                                compKey = tbaMatches[5]
//                            teamData?.clear()
//                            matchData?.clear()
                            }
                            compDD = false

                        },
                        text = {
                            Text(
                                text = "Chezy Champs",
                                color = getCurrentTheme().onPrimary,
                                modifier = Modifier.background(color = getCurrentTheme().onSurface)
                            )
                        }
                    )
                    OutlinedTextField(
                        value = comp.value,
                        onValueChange = {
                            comp.value = it
                            compKey = it
                        },
                        placeholder = { Text("TBA Competition Key") },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = getCurrentTheme().background,
                            unfocusedTextColor = getCurrentTheme().onPrimary,
                            focusedContainerColor = getCurrentTheme().background,
                            focusedTextColor = getCurrentTheme().onPrimary,
                            cursorColor = getCurrentTheme().onSecondary,
                            focusedBorderColor = Color.Cyan,
                            unfocusedBorderColor = getCurrentTheme().secondary
                        )
                    )
                }

            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { typeDD = true },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(3.dp, color = getCurrentTheme().primaryVariant),
                    colors = ButtonDefaults.buttonColors(containerColor = if (robotStartPosition.value < 3) redAlliance else if (robotStartPosition.value < 6) blueAlliance else if (robotStartPosition.value == 6) redAlliance else blueAlliance)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
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
                        Text(
                            text = textLabel,
                            color = getCurrentTheme().onPrimary,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Drop Down",
                            tint = getCurrentTheme().onPrimary,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }
                DropdownMenu(
                    expanded = typeDD,
                    onDismissRequest = { typeDD = false; },
                    modifier = Modifier.background(color = getCurrentTheme().onSurface)
                ) {
                    DropdownMenuItem(
                        onClick = {
                            robotStartPosition.value = 0
                            writeTabletDataFile(context, createTabletDataOutput(0))
                            typeDD = false
                        },
                        text = {
                            Text(
                                text = "Red 1",
                                color = getCurrentTheme().onPrimary
                            )
                        },
                        modifier = Modifier.background(color = redAlliance)
                    )
                    DropdownMenuItem(
                        onClick = {
                            robotStartPosition.value = 1
                            writeTabletDataFile(context, createTabletDataOutput(1))
                            typeDD = false
                        },
                        text = {
                            Text(
                                text = "Red 2",
                                color = getCurrentTheme().onPrimary
                            )
                        },
                        modifier = Modifier.background(color = redAlliance)
                    )
                    DropdownMenuItem(
                        onClick = {
                            robotStartPosition.value = 2
                            writeTabletDataFile(context, createTabletDataOutput(2))
                            typeDD = false
                        },
                        text = {
                            Text(
                                text = "Red 3",
                                color = getCurrentTheme().onPrimary
                            )
                        },
                        modifier = Modifier.background(color = redAlliance)
                    )
                    DropdownMenuItem(
                        onClick = {
                            robotStartPosition.value = 6
                            writeTabletDataFile(context, createTabletDataOutput(6))
                            typeDD = false
                        },
                        text = {
                            Text(
                                text = "Red Strat",
                                color = getCurrentTheme().onPrimary
                            )
                        },
                        modifier = Modifier.background(color = redAlliance)
                    )
                    HorizontalDivider(
                        color = getCurrentTheme().primaryVariant,
                        thickness = 3.dp,
                        modifier = Modifier.padding(8.dp)
                    )
                    DropdownMenuItem(
                        onClick = {
                            robotStartPosition.value = 3
                            writeTabletDataFile(context, createTabletDataOutput(3))
                            typeDD = false
                        },
                        text = {
                            Text(
                                text = "Blue 1",
                                color = getCurrentTheme().onPrimary
                            )
                        },
                        modifier = Modifier.background(color = blueAlliance)
                    )
                    DropdownMenuItem(
                        onClick = {
                            robotStartPosition.value = 4
                            writeTabletDataFile(context, createTabletDataOutput(4))
                            typeDD = false

                        },
                        text = {
                            Text(
                                text = "Blue 2",
                                color = getCurrentTheme().onPrimary
                            )
                        },
                        modifier = Modifier.background(color = blueAlliance)
                    )
                    DropdownMenuItem(
                        onClick = {
                            robotStartPosition.value = 5
                            writeTabletDataFile(context, createTabletDataOutput(5))
                            typeDD = false
                        },
                        text = {
                            Text(
                                text = "Blue 3",
                                color = getCurrentTheme().onPrimary
                            )
                        },
                        modifier = Modifier.background(color = blueAlliance)
                    )
                    DropdownMenuItem(
                        onClick = {
                            robotStartPosition.value = 7
                            writeTabletDataFile(context, createTabletDataOutput(7))
                            typeDD = false
                        },
                        text = {
                            Text(
                                text = "Blue Strat",
                                color = getCurrentTheme().onPrimary
                            )
                        },
                        modifier = Modifier.background(color = blueAlliance)
                    )
                    HorizontalDivider(
                        color = getCurrentTheme().primaryVariant,
                        thickness = 3.dp,
                        modifier = Modifier.padding(8.dp)
                    )
                    DropdownMenuItem(
                        onClick = {
                            robotStartPosition.value = 8
                            typeDD = false
                        },
                        text = {
                            Text(
                                text = "Pits",
                                color = getCurrentTheme().onPrimary
                            )
                        },
                        modifier = Modifier.background(color = getCurrentTheme().secondary)
                    )
                }

            }
        }
//        HorizontalDivider(
//            color = getCurrentTheme().primaryVariant,
//            thickness = 3.dp,
//            modifier = Modifier.padding(8.dp)
//        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            OutlinedButton(
                onClick = {
                    if (comp.value != "")
                        backStack.push(RootNode.NavTarget.MainMenu)
                },
                border = BorderStroke(color = getCurrentTheme().primaryVariant, width = 3.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = getCurrentTheme().secondary,
                    contentColor = getCurrentTheme().onPrimary,
                    disabledContainerColor = getCurrentTheme().primary,
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(8.dp),
                enabled = comp.value != ""

            ) {
                Text(
                    text = "Submit",
                    color = getCurrentTheme().onPrimary
                )
            }
            OutlinedButton(
                onClick = { deleteData = true },
                border = BorderStroke(color = getCurrentTheme().primaryVariant, width = 3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().secondary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Delete Data",
                    color = getCurrentTheme().onPrimary
                )
            }
            if (deleteData) {
                BasicAlertDialog(
                    onDismissRequest = { deleteData = false },
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(5.dp)
                        )
                        .border(BorderStroke(3.dp, getCurrentTheme().primaryVariant), RoundedCornerShape(5.dp))
                        .background(getCurrentTheme().secondary)
                ) {
                    var password by remember { mutableStateOf("") }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(8f / 10f)
                            .padding(5.dp)
                            .fillMaxHeight(2 / 8f)
                    ) {
                        Text(
                            text = "To delete all data, ask the scouting lead for the master password",
                            modifier = Modifier
                                .padding(5.dp)
                                .align(Alignment.TopCenter)
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("Password") },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = getCurrentTheme().background,
                                unfocusedTextColor = getCurrentTheme().onPrimary,
                                focusedContainerColor = getCurrentTheme().background,
                                focusedTextColor = getCurrentTheme().onPrimary,
                                cursorColor = getCurrentTheme().onSecondary,
                                focusedBorderColor = Color.Cyan,
                                unfocusedBorderColor = getCurrentTheme().secondary
                            ),
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                        androidx.compose.material.OutlinedButton(
                            onClick = {
                                if (password == "2046delete") {
                                    permPhotosList.clear()
                                    reset()
                                    stratReset()
                                    pitsReset()
                                    deleteAllTBAMatchData()
                                    deleteAllTBATeamData()
                                    deleteScoutMatchData()
                                    deleteScoutStratData()
                                    deleteScoutPitsData()
                                } else {
                                    password = ""
                                }
                                deleteData = false
                            },
                            border = BorderStroke(2.dp, getCurrentTheme().secondaryVariant),
                            colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                backgroundColor = getCurrentTheme().secondary,
                                contentColor = getCurrentTheme().onSecondary
                            ),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .alpha(if (password == "2046delete") 1f else .5f),
                            enabled = password == "2046delete",

                            ) {
                            Text(text = "Confirm", color = getCurrentTheme().error)
                        }
                        androidx.compose.material.OutlinedButton(
                            onClick = { deleteData = false },
                            border = BorderStroke(2.dp, getCurrentTheme().secondaryVariant),
                            colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                backgroundColor = getCurrentTheme().secondary,
                                contentColor = getCurrentTheme().onSecondary
                            ),
                            modifier = Modifier.align(Alignment.BottomStart)
                        ) {
                            Text(text = "Cancel", color = getCurrentTheme().error)
                        }
                    }
                }
            }
        }
    }
}