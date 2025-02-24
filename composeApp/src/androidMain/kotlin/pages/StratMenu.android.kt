package pages

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.core.view.ViewCompat
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.pop
import defaultOnPrimary
import defaultSecondary
import nodes.*
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
actual fun StratMenu(
    backStack: BackStack<RootNode.NavTarget>,
    scoutName: MutableState<String>,
    comp: MutableState<String>,
    teams: List<Team>,
    isRedAlliance: Boolean
) {
    var mutableMatchNum by remember { mutableStateOf(matchNum) }
    
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(100.dp)
                    .padding(0.dp)
            ) {
                Row(Modifier.fillMaxWidth()) {
//                    Text(
//                        text = "Human Net",
//                        fontSize = 12.sp,
//                        color = Color.White,
//                        modifier = Modifier
//                            .rotate(-90f)
//                            .align(Alignment.CenterVertically)
//                    )
                    Column(modifier = Modifier.padding(0.dp)) {
                        OutlinedButton(
                            modifier = Modifier
                                .width(150.dp)
                                .fillMaxHeight(0.5f),
                            border = BorderStroke(2.dp, color = Color.Yellow),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = defaultSecondary,
                                contentColor = defaultOnPrimary
                            ),
                            onClick = { humanNetScored.value += 1 }
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Scored",
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                                Text(
                                    text = humanNetScored.value.toString(),
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }
                        OutlinedButton(
                            modifier = Modifier
                                .width(150.dp)
                                .fillMaxHeight(),
                            border = BorderStroke(2.dp, color = Color.Yellow),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = defaultSecondary,
                                contentColor = defaultOnPrimary
                            ),
                            onClick = { humanNetMissed.value += 1 }
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Missed",
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                                Text(
                                    text = humanNetMissed.value.toString(),
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(0.dp)) {
                        OutlinedButton(
                            modifier = Modifier
                                .width(50.dp)
                                .fillMaxHeight(0.5f),
                            border = BorderStroke(2.dp, color = Color.Yellow),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = defaultSecondary,
                                contentColor = defaultOnPrimary
                            ),
                            onClick = {
                                humanNetScored.value -= 1
                                if (humanNetScored.value < 0) humanNetScored.value = 0
                            }
                        ) {
                            Text(
                                text = "-",
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                        OutlinedButton(
                            modifier = Modifier
                                .width(50.dp)
                                .fillMaxHeight(),
                            border = BorderStroke(2.dp, color = Color.Yellow),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = defaultSecondary,
                                contentColor = defaultOnPrimary
                            ),
                            onClick = {
                                humanNetMissed.value -= 1
                                if (humanNetMissed.value < 0) humanNetMissed.value = 0
                            }
                        ) {
                            Text(
                                text = "-",
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(modifier = Modifier.padding(0.dp), horizontalAlignment = Alignment.End) {
                        OutlinedButton(
                            modifier = Modifier
                                .width(150.dp)
                                .fillMaxHeight(0.5f),
                            border = BorderStroke(2.dp, color = Color.Yellow),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = defaultSecondary,
                                contentColor = defaultOnPrimary
                            ),
                            onClick = { }
                        ) {
                            Text(
                                text = "Current Match: $mutableMatchNum",
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                        Row {
                            OutlinedButton(
                                modifier = Modifier
                                    .width(75.dp)
                                    .fillMaxHeight(),
                                border = BorderStroke(2.dp, color = Color.Yellow),
                                shape = RectangleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = defaultSecondary,
                                    contentColor = defaultOnPrimary
                                ),
                                onClick = {
                                    stratTeamDataArray[teamsAllianceKey(matchNum, isRedAlliance)] = createStratOutput()
                                    updateMatchNum(matchNum - 1)
                                    mutableMatchNum = matchNum
                                    loadStratData(matchNum, isRedAlliance)
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                    contentDescription = "Back",
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                            OutlinedButton(
                                modifier = Modifier
                                    .width(75.dp)
                                    .fillMaxHeight(),
                                border = BorderStroke(2.dp, color = Color.Yellow),
                                shape = RectangleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = defaultSecondary,
                                    contentColor = defaultOnPrimary
                                ),
                                onClick = {
                                    stratTeamDataArray[teamsAllianceKey(matchNum, isRedAlliance)] = createStratOutput()
                                    updateMatchNum(matchNum + 1)
                                    mutableMatchNum = matchNum
                                    loadStratData(matchNum, isRedAlliance)
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                    contentDescription = "Forward",
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(0.dp), horizontalAlignment = Alignment.End) {
                        OutlinedButton(
                            modifier = Modifier
                                .width(125.dp)
                                .fillMaxHeight(0.5f),
                            border = BorderStroke(2.dp, color = Color.Yellow),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isRedAlliance) Color.Red else Color.Blue,
                                contentColor = defaultOnPrimary
                            ),
                            onClick = { }
                        ) {
                            Text(
                                text = "${if (isRedAlliance) "Red" else "Blue"} Alliance",
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                        OutlinedButton(
                            modifier = Modifier
                                .width(125.dp)
                                .fillMaxHeight(),
                            border = BorderStroke(2.dp, color = Color.Yellow),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = defaultSecondary,
                                contentColor = defaultOnPrimary
                            ),
                            onClick = { stratTeamDataArray[teamsAllianceKey(matchNum, isRedAlliance)] = createStratOutput(); backStack.pop() }
                        ) {
                            Text(
                                text = "Main",
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            teamList(
                label = "Team Strategy",
                teams = strategyOrder,
                onMove = { from, to ->
                    strategyOrder.apply {
                        add(to, removeAt(from))
                    }
                }
            )
            teamList(
                label = "Driving Skill",
                teams = drivingSkillOrder,
                onMove = { from, to ->
                    drivingSkillOrder.apply {
                        add(to, removeAt(from))
                    }
                }
            )
            teamList(
                label = "Collector Efficiency",
                teams = collectorOrder,
                onMove = { from, to ->
                    collectorOrder.apply {
                        add(to, removeAt(from))
                    }
                }
            )
            teamList(
                label = "Robot Connection",
                teams = connectionOrder,
                onMove = { from, to ->
                    connectionOrder.apply {
                        add(to, removeAt(from))
                    }
                }
            )
            OutlinedButton(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                border = BorderStroke(2.dp, color = Color.Yellow),
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(
                    containerColor = defaultSecondary,
                    contentColor = defaultOnPrimary
                ),
                onClick = {
                    stratTeamDataArray[teamsAllianceKey(matchNum, isRedAlliance)] = createStratOutput()
                    nextMatch()
                    mutableMatchNum = matchNum
                    loadStratData(matchNum, isRedAlliance)
                }
            ) {
                Text(
                    text = "Next Match",
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun teamList(
    label: String,
    teams: List<Team>,
    onMove: (from: Int, to: Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 8.dp, start = 10.dp)
                .align(Alignment.Start)
        )
        val view = LocalView.current
        val lazyListState = rememberLazyListState()
        val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
            onMove(from.index, to.index)
            ViewCompat.performHapticFeedback(
                view,
                HapticFeedbackConstantsCompat.SEGMENT_FREQUENT_TICK
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            state = lazyListState,
            contentPadding = PaddingValues(8.dp)
        ) {
            itemsIndexed(teams, key = { _, team -> team.number }) { index, team ->
                ReorderableItem(reorderableLazyListState, key = team.number) { isDragging ->
                    val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        shadowElevation = elevation,
                        color = defaultSecondary,
                        contentColor = defaultOnPrimary,
                        shape = RectangleShape,
                        border = BorderStroke(2.dp, Color.Yellow)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Team ${team.number}, ${team.name}",
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            )
                            IconButton(
                                modifier = Modifier.draggableHandle(
                                    onDragStarted = {
                                        ViewCompat.performHapticFeedback(
                                            view,
                                            HapticFeedbackConstantsCompat.GESTURE_START
                                        )
                                    },
                                    onDragStopped = {
                                        ViewCompat.performHapticFeedback(
                                            view,
                                            HapticFeedbackConstantsCompat.GESTURE_END
                                        )
                                    }
                                ),
                                onClick = { }
                            ) {
                                Icon(Icons.Rounded.Menu, contentDescription = "Reorder")
                            }
                        }
                    }
                }
            }
        }
    }
}