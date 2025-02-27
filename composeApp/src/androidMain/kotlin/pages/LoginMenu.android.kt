package pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.push
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import compKey
import createScoutMatchDataFolder
import createScoutPitsDataFolder
import createScoutStratDataFolder
import defaultError
import defaultOnPrimary
import defaultPrimaryVariant
import deleteFile
import deleteScoutMatchData
import deleteScoutPitsData
import deleteScoutStratData
import getCurrentTheme
import loadMatchDataFiles
import loadPitsDataFiles
import loadStratDataFiles
import matchData
import nodes.RootNode
import nodes.betterParseInt
import nodes.permPhotosList
import nodes.pitsReset
import nodes.teamDataArray
import nodes.reset
import teamData
import nodes.stratReset
import java.io.File
import java.lang.Integer.parseInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun LoginMenu(
    backStack: BackStack<RootNode.NavTarget>,
    scoutName: MutableState<String>,
    comp: MutableState<String>,
    numOfPitsPeople: MutableIntState
) {
    val logo = File("Logo.png")
    var compDD by remember { mutableStateOf(false) }
    var deleteData by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val tbaMatches = listOf(
        "2025wasno",
        "2025wabon",
        "2025waahs",
        "2025pncmp",
        "2025hop"

    )

    var first by remember { mutableStateOf(true) }

    if(first) {
        createScoutMatchDataFolder(context)
        loadMatchDataFiles(context)

        createScoutPitsDataFolder(context)
        loadPitsDataFiles(context)

        createScoutStratDataFolder(context)
        loadStratDataFiles(context)

        first = false
    }

    Column {
//        AsyncImage(
//            model = logo,//turn into bitmap
//            contentDescription = "Logo"
//        )
        Text(
            text = "Login",
            fontSize = 45.sp,
            color = getCurrentTheme().onPrimary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        HorizontalDivider(
            color = defaultPrimaryVariant,
            thickness = 3.dp
        )
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Name", color = defaultOnPrimary)
            OutlinedTextField(
                value = scoutName.value,
                onValueChange = {scoutName.value = it},
                placeholder = { Text("First, Last Name") },
                shape = RoundedCornerShape(15.dp),
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
        Box(modifier = Modifier.padding(15.dp).fillMaxWidth()) {
            OutlinedButton(
                onClick = { compDD = true },
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(3.dp, color = defaultPrimaryVariant),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Competition: ${comp.value}",
                        color = getCurrentTheme().onPrimary,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Text(
                        text = "V",
                        color = getCurrentTheme().onPrimary,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            DropdownMenu(expanded = compDD, onDismissRequest = { compDD = false; },modifier= Modifier.background(color = getCurrentTheme().onSurface)) {
                DropdownMenuItem(
                    onClick = {
                        if(comp.value != "Glacier Peak"){
                            comp.value = "Glacier Peak"
                            compKey = tbaMatches[0]
//                            teamData?.clear()
//                            matchData?.clear()
                        }
                        compDD = false
                              },
                    text = { Text(text = "Glacier Peak", color = getCurrentTheme().onPrimary,modifier= Modifier.background(color = getCurrentTheme().onSurface)) }
                )
                DropdownMenuItem(
                    onClick = {
                        if(comp.value != "Bonney Lake"){
                            comp.value = "Bonney Lake"
                            compKey = tbaMatches[1]
//                            teamData?.clear()
//                            matchData?.clear()
                        }
                        compDD = false
                    },
                    text = { Text(text = "Bonney Lake", color = getCurrentTheme().onPrimary,modifier= Modifier.background(color = getCurrentTheme().onSurface)) }
                )
                DropdownMenuItem(
                    onClick = {
                        if(comp.value != "Auburn"){
                            comp.value = "Auburn"
                            compKey = tbaMatches[2]
//                            teamData?.clear()
//                            matchData?.clear()
                        }
                        compDD = false
                    },
                    text ={ Text(text = "Auburn", color = getCurrentTheme().onPrimary,modifier= Modifier.background(color = getCurrentTheme().onSurface)) }
                )
                DropdownMenuItem(
                    onClick = {
                        if(comp.value != "Cheney"){
                            comp.value = "Cheney"
                            compKey = tbaMatches[3]
//                            teamData?.clear()
//                            matchData?.clear()
                        }
                        compDD = false
                    },
                    text = { Text(text = "DCMP", color = getCurrentTheme().onPrimary, modifier = Modifier.background(color = getCurrentTheme().onSurface)) }
                )
                DropdownMenuItem(
                    onClick = {
                        if(comp.value != "Houston"){
                            comp.value = "Houston"
                            compKey = tbaMatches[4]
//                            teamData?.clear()
//                            matchData?.clear()
                        }
                        compDD = false

                              },
                    text ={ Text(text = "Houston", color = getCurrentTheme().onPrimary,modifier= Modifier.background(color = getCurrentTheme().onSurface)) }
                )
                OutlinedTextField(
                    value = comp.value,
                    onValueChange = { comp.value = it
                        compKey = it},
                    placeholder = { Text("Custom Competition Key") },
                    shape = RoundedCornerShape(15.dp),
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
        HorizontalDivider(
            color = defaultPrimaryVariant,
        )

        Box(modifier = Modifier.fillMaxWidth(9f/10f).align(Alignment.CenterHorizontally)) {
            OutlinedButton(
                onClick = {
                    if (comp.value != "" && scoutName.value != "")
                        backStack.push(RootNode.NavTarget.MainMenu)
                },
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text(
                    text = "Submit",
                    color = getCurrentTheme().onPrimary
                )
            }
            OutlinedButton(
                onClick = { deleteData = true },
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = "Delete Data",
                    color = getCurrentTheme().onPrimary
                )
            }
            if (deleteData) {
                BasicAlertDialog(
                    onDismissRequest = { deleteData = false },
                    modifier = Modifier.clip(
                        RoundedCornerShape(5.dp)
                    ).border(BorderStroke(3.dp, defaultPrimaryVariant), RoundedCornerShape(5.dp))

                ) {
                    Column {
                        Text(text = "Are you sure?")
                        Box(modifier = Modifier.fillMaxWidth(8f / 10f)) {
                            Button(
                                onClick = {
                                    permPhotosList.clear()
                                    reset()
                                    stratReset()
                                    pitsReset()
                                    deleteFile(context)
                                    deleteScoutMatchData()
                                    deleteScoutStratData()
                                    deleteScoutPitsData()
                                    deleteData = false
                                },
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                Text(text = "Yes", color = defaultError)
                            }

                            Button(
                                onClick = { deleteData = false },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                Text(text = "No", color = defaultError)
                            }
                        }
                    }
                }
            }
        }
    }
}