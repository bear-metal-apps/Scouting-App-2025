@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)

package pages

import Underline
import nodes.RootNode
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.push
import composables.Profile
import composables.download
import defaultOnPrimary
import getCurrentTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.tahomarobotics.scouting.ComposeFileProvider
import java.io.File
import java.lang.Double
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt

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
        var downloadActive by remember { mutableStateOf(false) }

        var pitsPersonDD by remember { mutableStateOf(false) }
        var scoutedTeamName by remember { mutableStateOf("") }


        var scoutedTeamNumber by remember { mutableStateOf("") }
        val photoArray = remember { mutableStateListOf(Uri.EMPTY) }
        var driveType by remember { mutableStateOf("") }
        var motorType by remember { mutableStateOf("") }
        var auto by remember { mutableStateOf("") }
        var width by remember { mutableStateOf("") }
        var length by remember { mutableStateOf("") }
        var weight by remember { mutableStateOf("") }
        var l4 by remember { mutableStateOf(false) }
        var l3 by remember { mutableStateOf(false) }
        var l2 by remember { mutableStateOf(false) }
        var l1 by remember { mutableStateOf(false) }
        var algaeBarge by remember { mutableStateOf(false) }
        var algaeProcessed by remember { mutableStateOf(false) }
        var algaeRemoval by remember { mutableStateOf(false) }
        var cycleTime by remember { mutableStateOf("") }
        var rigidity by remember { mutableStateOf("") }
        var coralHigh by remember { mutableStateOf(false) }
        var coralLow by remember { mutableStateOf(false) }
        var algae by remember { mutableStateOf(false) }
        var defense by remember { mutableStateOf(false) }
        var collectPreference by remember { mutableStateOf("None Selected") }
        var comments by remember { mutableStateOf("") }

        var photoAmount by remember { mutableIntStateOf(0) }
        val scrollState = rememberScrollState(0)
        val isScrollEnabled by remember { mutableStateOf(true) }
        var robotCard by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val trashCan = ImageRequest.Builder(context).data(File("trash.png")).build()
        val cam = ImageRequest.Builder(context).data(File("KillCam.png")).build()

        var dropDownExpanded by remember { mutableStateOf(false) }
        var dropDown2Expanded by remember { mutableStateOf(false) }
        var collectPrefDD by remember { mutableStateOf(false) }

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
                TextButton(
                    onClick = { pitsPersonDD = true },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(15.dp)
                ) {
                    Text(
                        text = pitsPerson.value,
                        fontSize = 40.sp,
                        //color = defaultOnPrimary,
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(15.dp)
                        .offset(0.dp, 15.dp)
                ) {
                    DropdownMenu(
                        expanded = pitsPersonDD,
                        onDismissRequest = { pitsPersonDD = false },
                        modifier = Modifier
                            .background(color = Color(15, 15, 15))
                            .clip(RoundedCornerShape(7.5.dp))

                    ) {
                        for (x in 1..numOfPitsPeople.intValue) {
                            DropdownMenuItem(
                                onClick = {
                                    pitsPersonDD = false
                                    pitsPerson.value = "P$x"
                                },
                                text = {
                                    Text("P$x", color = defaultOnPrimary)
                                }
                            )
                        }
                    }
                }
            }
            Row {
                Text(
                    text = "Team Name: ",
                    fontSize = 20.sp,
                    color = defaultOnPrimary
                )
                OutlinedTextField(
                    value = scoutedTeamName,
                    onValueChange = { scoutedTeamName = it },
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(6, 9, 13),
                        unfocusedContainerColor = Color(6, 9, 13),
                        focusedTextColor = defaultOnPrimary,
                        unfocusedTextColor = defaultOnPrimary
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.size(85.dp, 60.dp)
                )
                Text(
                    text = "Team Number: ",
                    fontSize = 20.sp,
                    color = defaultOnPrimary,
                )
                OutlinedTextField(
                    value = scoutedTeamNumber,
                    onValueChange = { scoutedTeamNumber = it },
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(6, 9, 13),
                        unfocusedContainerColor = Color(6, 9, 13),
                        focusedTextColor = defaultOnPrimary,
                        unfocusedTextColor = defaultOnPrimary
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.size(85.dp, 60.dp)
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

                                uri = ComposeFileProvider.getImageUri(
                                    context,
                                    "photo_$photoAmount"
                                )
                                imageUri = uri
                                cameraLauncher.launch(uri)

                                photoArray.add(photoAmount, uri)
                                photoAmount++
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
                        value = driveType,
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
                                driveType = "Swerve"
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
                                driveType = "Tank"
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
                                driveType = "Mecanum"
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
                                    text = "Omni",
                                    color = Color.White
                                )
                            },
                            onClick = {
                                driveType = "Omni"
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
                                    text = "H-Drive",
                                    color = Color.White
                                )
                            },
                            onClick = {
                                driveType = "H-Drive"
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
                        text = "Motor Type:  ",
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
                            value = motorType,
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
                                        text = "Motor",
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    motorType = "Motor"
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
                                        text = "Spark",
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    motorType = "Spark"
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
                                        text = "Neo",
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    motorType = "Neo"
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
                        text = "Auto:  ",
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.CenterStart)
                    )
                    TextField(
                        value = auto,
                        onValueChange = { auto = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.CenterEnd)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.5.dp)
                        .border(BorderStroke(2.dp, Color.Yellow), shape = RoundedCornerShape(5.dp))
                ) {
                    Text(
                        text = "Frame Perimeter:",
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.CenterStart)
                    )
                    Row (
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Row (
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = "Width",
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            TextField(
                                value = width,
                                onValueChange = {
                                    width = it
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.White
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .padding(top = 15.dp, bottom = 15.dp, start = 5.dp, end = 20.dp)
                                    .width(width = 75.dp)
                            )
                        }

                        Row (
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = "Length",
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            TextField(
                                value = length,
                                onValueChange = { length = it },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.White
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .padding(top = 15.dp, bottom = 15.dp, start = 5.dp, end = 15.dp)
                                    .width(width = 75.dp)
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
                        value = weight,
                        onValueChange = { weight = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .padding(15.dp)
                            .align(Alignment.CenterEnd)
                    )
                }

                Spacer(modifier = Modifier.height(7.5.dp))
                HorizontalDivider(color = Color.Yellow, thickness = 2.dp)
                Spacer(modifier = Modifier.height(7.5.dp))

                Text(
                    text = "Scoring",
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Underline(
                    modifier = Modifier,
                    end = Offset(160f, 1.5f),
                    color = Color.White,
                    thickness = 3.dp
                )

                Spacer(modifier = Modifier.height(7.5.dp))

                Text(
                    text = "Coral",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "L4",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = l4,
                        onCheckedChange = {
                            l4 = it
                        },
                        modifier = Modifier.padding(end = 30.dp)
                    )
                    Text(
                        text = "L3",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = l3,
                        onCheckedChange = {
                            l3 = it
                        },
                        modifier = Modifier.padding(end = 30.dp)
                    )
                    Text(
                        text = "L2",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = l2,
                        onCheckedChange = {
                            l2 = it
                        },
                        modifier = Modifier.padding(end = 30.dp)
                    )
                    Text(
                        text = "L1",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = l1,
                        onCheckedChange = {
                            l1 = it
                        }
                    )
                }

                Text(
                    text = "Algae",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Barge",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = algaeBarge,
                        onCheckedChange = {
                            algaeBarge = it
                        },
                        modifier = Modifier.padding(end = 30.dp)
                    )
                    Text(
                        text = "L4",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = l4,
                        onCheckedChange = {
                            l4 = it
                        },
                        modifier = Modifier.padding(end = 30.dp)
                    )
                    Text(
                        text = "L4",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = l4,
                        onCheckedChange = {
                            l4 = it
                        },
                        modifier = Modifier.padding(end = 30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(7.5.dp))
                HorizontalDivider(color = Color.Yellow, thickness = 2.dp)
                Spacer(modifier = Modifier.height(7.5.dp))

                Text(
                    text = "Preferred Performance",
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Underline(
                    modifier = Modifier,
                    end = Offset(475f, 1.5f),
                    color = Color.White,
                    thickness = 3.dp
                )

                Spacer(modifier = Modifier.height(7.5.dp))

                Text(
                    text = "Coral",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(7.5.dp))

                Text(
                    text = "Other",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(7.5.dp))
                HorizontalDivider(color = Color.Yellow, thickness = 2.dp)
                Spacer(modifier = Modifier.height(7.5.dp))

                Text(
                    text = "Other",
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Underline(
                    modifier = Modifier,
                    end = Offset(110f, 1.5f),
                    color = Color.White,
                    thickness = 3.dp
                )

                Spacer(modifier = Modifier.height(7.5.dp))

                Spacer(modifier = Modifier.height(7.5.dp))
                HorizontalDivider(color = Color.Yellow, thickness = 2.dp)
                Spacer(modifier = Modifier.height(7.5.dp))

                OutlinedButton(
                    onClick = {
                        collectPrefDD = true
                    },
                    border = BorderStroke(2.dp, color = Color.Yellow),
                    shape = CircleShape
                ) {
                    Text(
                        text = "Collection Preference: $collectPreference",
                        fontSize = 15.sp,
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
                                collectPreference = "OverTheBumper"
                            },
                            text = { Text("Over The Bumper", color = defaultOnPrimary) }
                        )
                        DropdownMenuItem(
                            onClick = {
                                collectPrefDD = false
                                collectPreference = "UnderTheBumper"
                            },
                            text = { Text("Under The Bumper", color = defaultOnPrimary) }
                        )
                        DropdownMenuItem(
                            onClick = {
                                collectPrefDD = false
                                collectPreference = "from the Feeder Station"
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
                    value = comments,
                    onValueChange = { comments = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(6, 9, 13),
                        unfocusedContainerColor = Color(6, 9, 13),
                        focusedTextColor = defaultOnPrimary,
                        unfocusedTextColor = defaultOnPrimary
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth(9f / 10f)
                        .align(Alignment.CenterHorizontally)
                        .height(90.dp)
                )
                Row {
                    OutlinedButton(onClick = {
                        if (photoArray.size >= 1) {
                            robotCard = true
                        }
                    }) { Text(text = "Submit", color = defaultOnPrimary) }
                    OutlinedButton(onClick = { robotCard = false }) {
                        Text(
                            text = "Close",
                            color = defaultOnPrimary
                        )
                    }
                    OutlinedButton(onClick = { downloadActive = true }) {
                        Text(
                            text = "Download",
                            color = defaultOnPrimary
                        )
                    }
                    OutlinedButton(onClick = { backStack.push(RootNode.NavTarget.MainMenu) }) {
                        Text(text = "Back", color = defaultOnPrimary)
                    }
                    if (downloadActive) {
                        download(context, photoArray, scoutedTeamNumber, photoAmount)
                        downloadActive = false
                    }
                }
                if (robotCard) {
                    Box(
                        modifier = Modifier.padding(5.dp)
                            .border(BorderStroke(2.dp, Color.Yellow), RoundedCornerShape(15.dp))
                    ) {
                        Profile(
                            photoArray,
                            scoutedTeamName,
                            scoutedTeamNumber,
                            driveType,
                            motorType,
                            auto,
                            collectPreference,
                            comments,
                            scoutName.value,
                            Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
