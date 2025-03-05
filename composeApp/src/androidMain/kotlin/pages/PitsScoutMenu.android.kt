@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)

package pages

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.push
import composables.downloadPitsPhotos
import createScoutPitsDataFile
import defaultError
import defaultOnPrimary
import defaultPrimaryVariant
import getCurrentTheme
import getFileName
import imageBasesFolder
import nodes.RootNode
import nodes.algaeBarge
import nodes.algaeProcess
import nodes.algaeRemoval
import nodes.bargePreferred
import nodes.collectPreference
import nodes.comments
import nodes.coralHigh
import nodes.coralLow
import nodes.createPitsOutput
import nodes.cycleTime
import nodes.defensePreferred
import nodes.driveType
import nodes.jsonObject
import nodes.l1
import nodes.l2
import nodes.l3
import nodes.l4
import nodes.motorType
import nodes.permPhotosList
import nodes.photoArray
import nodes.pitsReset
import nodes.pitsTeamDataArray
import nodes.processPreferred
import nodes.rigidity
import nodes.scoutedTeamName
import nodes.scoutedTeamNumber
import nodes.weight
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.tahomarobotics.scouting.ComposeFileProvider
import java.io.File
import java.lang.Integer.parseInt

@SuppressLint("NewApi", "Recycle")
@OptIn(ExperimentalResourceApi::class)
@Suppress("VARIABLE_WITH_REDUNDANT_INITIALIZER")
@Composable
actual fun PitsScoutMenu(
    backStack: BackStack<RootNode.NavTarget>,
    pitsPerson: MutableState<String>,
    scoutName: MutableState<String>,
    numOfPitsPeople: MutableIntState
) {
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _: Boolean ->

        }
        var hasImage by remember { mutableStateOf(false) }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { hasImage = it }
        )
        var array : SnapshotStateList<Uri> = SnapshotStateList()
        var downloadActive by remember { mutableStateOf(false) }

        var teamNumberPopup by remember { mutableStateOf(false) }

        var pitsPersonDD by remember { mutableStateOf(false) }
        var teamNumRequirement by remember { mutableStateOf(false) }

        var photoAmount by remember { mutableIntStateOf(0) }
        var scrollState = rememberScrollState(0)
//        val listState = rememberLazyListState()
//        val coroutineScope = rememberCoroutineScope()
        val isScrollEnabled by remember { mutableStateOf(true) }
        var robotCard by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val trashCan = ImageRequest.Builder(context).data(File("trash.png")).build()
        val cam = ImageRequest.Builder(context).data(File("KillCam.png")).build()

        var dropDownExpanded by remember { mutableStateOf(false) }
        var dropDown2Expanded by remember { mutableStateOf(false) }
        var collectPrefDD by remember { mutableStateOf(false) }

//        LazyColumn(
//            state = listState,
//            modifier = Modifier.height(0.dp)
//        ) {
//
//        }
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState, enabled = isScrollEnabled)
                .padding(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .offset(20.dp, 15.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Pits",
                    fontSize = 50.sp,
                    //color = defaultOnPrimary,
                )
            }
            Column {
                Text(
                    text = "Team Name: ",
                    fontSize = 20.sp,
                    color = defaultOnPrimary
                )
                OutlinedTextField(
                    value = scoutedTeamName.value,
                    onValueChange = { scoutedTeamName.value = it },
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(6, 9, 13),
                        unfocusedContainerColor = Color(6, 9, 13),
                        focusedTextColor = defaultOnPrimary,
                        unfocusedTextColor = defaultOnPrimary,
                        cursorColor = Color.Yellow
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.size(300.dp, 60.dp)
                )
                Text(
                    text = "Team Number: ",
                    fontSize = 20.sp,
                    color = defaultOnPrimary,
                )
                OutlinedTextField(
                    value = scoutedTeamNumber.value,
                    onValueChange = { value ->
                        val filteredText = value.filter { it.isDigit() }
                        if (filteredText.isNotEmpty() && !filteredText.contains(','))
                            scoutedTeamNumber.value = filteredText.slice(0..<filteredText.length.coerceAtMost(5))
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(6, 9, 13),
                        unfocusedContainerColor = Color(6, 9, 13),
                        focusedTextColor = defaultOnPrimary,
                        unfocusedTextColor = defaultOnPrimary,
                        cursorColor = Color.Yellow
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.size(120.dp, 60.dp)
                )
            }
            Spacer(modifier = Modifier.height(7.5.dp))
            HorizontalDivider(color = Color.Yellow, thickness = 2.dp)
            Spacer(modifier = Modifier.height(7.5.dp))

            OutlinedButton(
                border = BorderStroke(2.dp, color = Color.Yellow),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(5.dp).padding(start = 5.dp),
                onClick = {

                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) -> {
                            if (photoAmount < 3) {//moved up
                                var uri = Uri.EMPTY

                                try {
                                    uri = ComposeFileProvider.getImageUri(context, parseInt(scoutedTeamNumber.value), "Team${scoutedTeamNumber.value}Photo${photoArray.size}")
                                    println("permPhotosList Uri: $uri")

                                    imageUri = uri
                                    cameraLauncher.launch(uri)

                                    photoAmount++

                                    photoArray.add(uri.toString())

                                    for(img in permPhotosList) {
                                        if(img == uri.toString()) {
                                            permPhotosList.remove(uri.toString())
                                            println("deleted ${uri}")
                                        }
                                    }
                                    permPhotosList.add(uri.toString())
                                    println("URI added to permPhotosList: ${uri.toString()}")
                                } catch (e: NumberFormatException) {
                                    teamNumberPopup = true
                                }

                                hasImage = false
                            }
                        }

                        else -> {
                            launcher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }
            ) {
                Row {
                    Image(
                        painter = painterResource("KillCam.png"),
                        contentDescription = "Camera",
                        modifier = Modifier.fillMaxHeight()
                    )
                    Column {
                        Text(
                            text = "Take Picture",
                            color = defaultOnPrimary
                        )
                        Text(
                            text = "*Ask Permission First",
                            color = Color.Gray,
                            fontSize = 10.sp,
                        )
                    }
                }
            }
            Row(modifier = Modifier.horizontalScroll(ScrollState(0))) {
                if (hasImage) {//helps update Box
                    photoArray.forEach {
                        Box {
                            AsyncImage(
                                model = it,
                                contentDescription = "Robot image",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(7.5.dp))
                                    .size(height = 200.dp, width = 300.dp)
                            )
                            TextButton(
                                onClick = {
                                    photoArray.remove(it)
                                    photoAmount--
                                },
                                modifier = Modifier
                                    .scale(0.25f)
                                    .align(Alignment.BottomStart)
                            ) {
                                AsyncImage(
                                    model = trashCan,
                                    contentDescription = "Delete",
                                )
                            }
                        }
                    }
                }
            }
            if (photoAmount >= 1) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Amount of Photos: $photoAmount",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 2.5.dp)
                    .border(BorderStroke(2.dp, Color.Yellow), shape = RoundedCornerShape(5.dp))
            ) {
                Text(
                    text = "Drive Type:  ",
                    modifier = Modifier
                        .padding(15.dp)
                        .align(Alignment.CenterStart)
                )
                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .width(200.dp)
                        .padding(15.dp)
                        .align(Alignment.CenterEnd),
                    expanded = dropDownExpanded,
                    onExpandedChange = { it ->
                        dropDownExpanded = it
                    }
                ) {
                    TextField(
                        modifier = Modifier
                            .menuAnchor(),
                        value = driveType.value,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownExpanded)
                        },
                        textStyle = TextStyle(color = Color.White)
                    )
                    DropdownMenu(
                        expanded = dropDownExpanded,
                        onDismissRequest = {
                            dropDownExpanded = false
                        }
                    ) {
                        HorizontalDivider(
                            color = getCurrentTheme().onSurface,
                            thickness = 3.dp
                        )

                        DropdownMenuItem(
                            {
                                Text(
                                    text = "Swerve",
                                    color = Color.White
                                )
                            },
                            onClick = {
                                driveType.value = "Swerve"
                                dropDownExpanded = false
                            }
                        )

                        HorizontalDivider(
                            color = getCurrentTheme().onSurface,
                            thickness = 3.dp
                        )

                        DropdownMenuItem(
                            {
                                Text(
                                    text = "Tank",
                                    color = Color.White
                                )
                            },
                            onClick = {
                                driveType.value = "Tank"
                                dropDownExpanded = false
                            }
                        )

                        HorizontalDivider(
                            color = getCurrentTheme().onSurface,
                            thickness = 3.dp
                        )

                        DropdownMenuItem(
                            {
                                Text(
                                    text = "Mecanum",
                                    color = Color.White
                                )
                            },
                            onClick = {
                                driveType.value = "Mecanum"
                                dropDownExpanded = false
                            }
                        )

                        HorizontalDivider(
                            color = getCurrentTheme().onSurface,
                            thickness = 3.dp
                        )

                        DropdownMenuItem(
                            {
                                Text(
                                    text = "Other",
                                    color = Color.White
                                )
                            },
                            onClick = {
                                driveType.value = "Other"
                                dropDownExpanded = false
                            }
                        )

                        HorizontalDivider(
                            color = getCurrentTheme().onSurface,
                            thickness = 3.dp
                        )

                    }
//                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, focusedTextColor = Color.White),
                }
            }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.5.dp)
                        .border(BorderStroke(2.dp, Color.Yellow), shape = RoundedCornerShape(5.dp))
                ) {
                    Text(
                        text = "Motor Type for Drive Base:  ",
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.CenterStart)
                    )
                    ExposedDropdownMenuBox(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(15.dp)
                            .align(Alignment.CenterEnd),
                        expanded = dropDown2Expanded,
                        onExpandedChange = { it ->
                            dropDown2Expanded = it
                        }
                    ) {
                        TextField(
                            modifier = Modifier
                                .menuAnchor(),
                            value = motorType.value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDown2Expanded)
                            },
                            textStyle = TextStyle(color = Color.White)
                        )
                        ExposedDropdownMenu(
                            expanded = dropDown2Expanded,
                            onDismissRequest = {
                                dropDown2Expanded = false
                            }
                        ) {
                            HorizontalDivider(
                                color = getCurrentTheme().onSurface,
                                thickness = 3.dp
                            )

                            DropdownMenuItem(
                                {
                                    Text(
                                        text = "Falcon",
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    motorType.value = "Falcon"
                                    dropDown2Expanded = false
                                }
                            )

                            HorizontalDivider(
                                color = getCurrentTheme().onSurface,
                                thickness = 3.dp
                            )

                            DropdownMenuItem(
                                {
                                    Text(
                                        text = "Kraken",
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    motorType.value = "Kraken"
                                    dropDown2Expanded = false
                                }
                            )

                            HorizontalDivider(
                                color = getCurrentTheme().onSurface,
                                thickness = 3.dp
                            )

                            DropdownMenuItem(
                                {
                                    Text(
                                        text = "Neo Vortex",
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    motorType.value = "Neo Vortex"
                                    dropDown2Expanded = false
                                }
                            )

                            HorizontalDivider(
                                color = getCurrentTheme().onSurface,
                                thickness = 3.dp
                            )

                            DropdownMenuItem(
                                {
                                    Text(
                                        text = "Neo 550",
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    motorType.value = "Neo 550"
                                    dropDown2Expanded = false
                                }
                            )

                            HorizontalDivider(
                                color = getCurrentTheme().onSurface,
                                thickness = 3.dp
                            )

                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.5.dp)
                        .border(BorderStroke(2.dp, Color.Yellow), shape = RoundedCornerShape(5.dp))
                ) {
                    Text(
                        text = "Weight (pounds):",
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.CenterStart)
                    )
                    TextField(
                        value = weight.value,
                        onValueChange = { weight.value = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            cursorColor = Color.Yellow
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .padding(15.dp)
                            .width(width = 75.dp)
                            .align(Alignment.CenterEnd)
                    )
                }

                Spacer(modifier = Modifier.height(7.5.dp))
                HorizontalDivider(color = Color.Yellow, thickness = 2.dp)
                Spacer(modifier = Modifier.height(7.5.dp))

                Text(
                    text = "Scoring Abilities",
                    fontSize = 25.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 15.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.5.dp)
                        .border(BorderStroke(2.dp, Color.Yellow), shape = RoundedCornerShape(5.dp))
                ) {
                    Text(
                        text = "Coral: ",
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.CenterStart)
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "L4",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = l4.value,
                            onCheckedChange = {
                                l4.value = it
                            },
                            modifier = Modifier.padding(end = 30.dp)
                        )
                        Text(
                            text = "L3",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = l3.value,
                            onCheckedChange = {
                                l3.value = it
                            },
                            modifier = Modifier.padding(end = 30.dp)
                        )
                        Text(
                            text = "L2",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = l2.value,
                            onCheckedChange = {
                                l2.value = it
                            },
                            modifier = Modifier.padding(end = 30.dp)
                        )
                        Text(
                            text = "L1",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = l1.value,
                            onCheckedChange = {
                                l1.value = it
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.5.dp)
                        .border(BorderStroke(2.dp, Color.Yellow), shape = RoundedCornerShape(5.dp))
                ) {
                    Text(
                        text = "Algae:",
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.CenterStart)
                    )
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "Barge",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = algaeBarge.value,
                            onCheckedChange = {
                                algaeBarge.value = it
                            },
                            modifier = Modifier.padding(end = 30.dp)
                        )
                        Text(
                            text = "Processed",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = algaeProcess.value,
                            onCheckedChange = {
                                algaeProcess.value = it
                            },
                            modifier = Modifier.padding(end = 30.dp)
                        )
                        Text(
                            text = "Removal",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = algaeRemoval.value,
                            onCheckedChange = {
                                algaeRemoval.value = it
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(7.5.dp))
                HorizontalDivider(color = Color.Yellow, thickness = 2.dp)
                Spacer(modifier = Modifier.height(7.5.dp))

                Text(
                    text = "Preferred Performance",
                    fontSize = 25.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 15.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.5.dp)
                        .border(BorderStroke(2.dp, Color.Yellow), shape = RoundedCornerShape(5.dp))
                ) {
                    Text(
                        text = "Coral: ",
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.CenterStart)
                    )
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "High",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = coralHigh.value,
                            onCheckedChange = {
                                coralHigh.value = it
                            },
                            modifier = Modifier.padding(end = 30.dp)
                        )
                        Text(
                            text = "Low",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = coralLow.value,
                            onCheckedChange = {
                                coralLow.value = it
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.5.dp)
                        .border(BorderStroke(2.dp, Color.Yellow), shape = RoundedCornerShape(5.dp))
                ) {
                    Text(
                        text = "Other: ",
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.CenterStart)
                    )
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "Barge",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = bargePreferred.value,
                            onCheckedChange = {
                                bargePreferred.value = it
                            },
                            modifier = Modifier.padding(end = 30.dp)
                        )
                        Text(
                            text = "Processed",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = processPreferred.value,
                            onCheckedChange = {
                                processPreferred.value = it
                            },
                            modifier = Modifier.padding(end = 30.dp)
                        )
                        Text(
                            text = "Defense",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = defensePreferred.value,
                            onCheckedChange = {
                                defensePreferred.value = it
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(7.5.dp))
                HorizontalDivider(color = Color.Yellow, thickness = 2.dp)
                Spacer(modifier = Modifier.height(7.5.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.5.dp)
                        .border(BorderStroke(2.dp, Color.Yellow), shape = RoundedCornerShape(5.dp))
                ) {
                    Text(
                        text = "Cycle Time (seconds): ",
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.CenterStart)
                    )
                    TextField(
                        value = cycleTime.value,
                        onValueChange = { cycleTime.value = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            cursorColor = Color.Yellow
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .padding(15.dp)
                            .width(width = 75.dp)
                            .align(Alignment.CenterEnd)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.5.dp)
                        .border(BorderStroke(2.dp, Color.Yellow), shape = RoundedCornerShape(5.dp))
                ) {
                    Column {
                        Text(
                            text = "Rigidity",
                            modifier = Modifier
                                .padding(start = 15.dp, top = 10.dp, bottom = 5.dp)
                                .align(Alignment.Start)
                        )
                        OutlinedTextField(
                            value = rigidity.value,
                            onValueChange = { rigidity.value = it },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(6, 9, 13),
                                unfocusedContainerColor = Color(6, 9, 13),
                                focusedTextColor = defaultOnPrimary,
                                unfocusedTextColor = defaultOnPrimary,
                                cursorColor = Color.Yellow
                            ),
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .fillMaxWidth(9f / 10f)
                                .align(Alignment.End)
                                .padding(start = 10.dp, bottom = 15.dp)
                                .height(60.dp)
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        collectPrefDD = true
                    },
                    border = BorderStroke(2.dp, color = Color.Yellow),
                    shape = CircleShape
                ) {
                    Text(
                        text = "Collection Preference: ${collectPreference.value}",
                        fontSize = 20.sp,
                        color = defaultOnPrimary
                    )
                }
                Box(modifier = Modifier.padding(15.dp, 0.dp)) {
                    DropdownMenu(
                        expanded = collectPrefDD,
                        onDismissRequest = { collectPrefDD = false },
                        modifier = Modifier
                            .background(color = Color(15, 15, 15))
                            .clip(RoundedCornerShape(7.5.dp))
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                collectPrefDD = false
                                collectPreference.value = "OverTheBumper"
                            },
                            text = { Text("Over The Bumper", color = defaultOnPrimary) }
                        )
                        DropdownMenuItem(
                            onClick = {
                                collectPrefDD = false
                                collectPreference.value = "from the Feeder Station"
                            },
                            text = { Text("Feeder Station", color = defaultOnPrimary) }
                        )
                    }
                }

                Text(
                    text = "Comments:",
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                OutlinedTextField(
                    value = comments.value,
                    onValueChange = { comments.value = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(6, 9, 13),
                        unfocusedContainerColor = Color(6, 9, 13),
                        focusedTextColor = defaultOnPrimary,
                        unfocusedTextColor = defaultOnPrimary,
                        cursorColor = Color.Yellow
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth(9f / 10f)
                        .align(Alignment.CenterHorizontally)
                        .height(90.dp)
                )
                Row {
                    OutlinedButton(onClick = {
                        if(scoutedTeamNumber.value == "") {
                            teamNumRequirement = true
                        } else {
                            if (photoArray.size >= 1) {
                                robotCard = true
                            }

                            val addList = mutableListOf<String>()
                            val removeList = mutableListOf<String>()

                            println("photos:")
                            println(photoArray[0])
                            photoArray.forEach {
                                val startIndex = it.indexOf("/", 56)
                                addList.add(it.substring(startIndex+1))
                                removeList.add(it)
                                println(it.substring(startIndex+1))
                            }
                            photoArray.addAll(addList)
                            photoArray.removeAll(removeList)

                            pitsTeamDataArray[parseInt(scoutedTeamNumber.value)] = createPitsOutput(mutableIntStateOf(parseInt(scoutedTeamNumber.value)))
                            createScoutPitsDataFile(context, parseInt(scoutedTeamNumber.value), pitsTeamDataArray[parseInt(scoutedTeamNumber.value)]!!)

                            pitsReset()
                            photoAmount = 0

//                            coroutineScope.launch {
//                                listState.scrollToItem(0)
//                            }

                        }
                    }) { Text(text = "Submit", color = defaultOnPrimary) }
                    OutlinedButton(onClick = { backStack.push(RootNode.NavTarget.MainMenu) }) {
                        Text(text = "Back", color = defaultOnPrimary)
                    }
                    if (downloadActive) {
                        var array : SnapshotStateList<Uri> = SnapshotStateList()
                        snapshotFlow {
                            for((index) in photoArray.withIndex()) {
                                array[index] = Uri.parse(photoArray[index])
                                println("String: ${photoArray[index]}")
                                println("Uri: ${array[index]}")
                            }
                        }

                        downloadPitsPhotos(context, array, scoutedTeamNumber.value, photoAmount)
                        downloadActive = false
                    }
                }
//                if (robotCard) {
//                    Box(
//                        modifier = Modifier.padding(5.dp)
//                            .border(BorderStroke(2.dp, Color.Yellow), RoundedCornerShape(15.dp))
//                    ) {
//                        Profile(
//                            photoArray,
//                            scoutedTeamName,
//                            scoutedTeamNumber,
//                            driveType,
//                            motorType,
//                            auto,
//                            collectPreference,
//                            comments,
//                            scoutName.value,
//                            Modifier.padding(10.dp)
//                        )
//                    }
//                }
            }

            if(teamNumRequirement) {
                BasicAlertDialog(
                    onDismissRequest = { teamNumRequirement = false },
                    modifier = Modifier.clip(
                        RoundedCornerShape(5.dp)
                    ).border(BorderStroke(3.dp, defaultPrimaryVariant), RoundedCornerShape(5.dp))

                ) {
                    Column {
                        Text(text = "A valid team number must be provided.", modifier = Modifier.align(Alignment.CenterHorizontally))
                        Box(modifier = Modifier.fillMaxWidth(8f / 10f)) {
                            Button(
                                onClick = {
                                    teamNumRequirement = false
                                },
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(text = "Ok", color = defaultError)
                            }
                        }
                    }
                }
            }

            if(teamNumberPopup) {
                BasicAlertDialog(
                    onDismissRequest = { teamNumberPopup = false },
                    modifier = Modifier.clip(
                        RoundedCornerShape(5.dp)
                    ).border(BorderStroke(3.dp, getCurrentTheme().primaryVariant), RoundedCornerShape(5.dp))
                        .background(getCurrentTheme().secondary)
                ) {
                    Box(modifier = Modifier.fillMaxWidth(8f / 10f).padding(5.dp).fillMaxHeight(1/8f)) {
                        Text(text = "Please input a valid team number.",
                            modifier = Modifier.padding(5.dp).align(Alignment.TopCenter)
                        )
                        OutlinedButton(
                            onClick = {
                                teamNumberPopup = false
                            },
                            border = BorderStroke(2.dp, getCurrentTheme().secondaryVariant),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = getCurrentTheme().onSecondary, containerColor = getCurrentTheme().secondary) ,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            Text(text = "Ok", color = getCurrentTheme().error)
                        }
                    }
                }
            }

        }
