package pages

//import composables.AutoCheckboxesHorizontal
//import composables.AutoCheckboxesVertical
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.core.view.ViewCompat
import com.bumble.appyx.components.backstack.BackStack
import defaultOnPrimary
import defaultSecondary
import nodes.RootNode
import nodes.humanNetMissed
import nodes.humanNetScored
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
actual fun StratMenu(
    backStack: BackStack<RootNode.NavTarget>,
    scoutName: MutableState<String>,
    comp: MutableState<String>
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(100.dp)
                    //.border(BorderStroke(2.dp, Color.Yellow), RectangleShape)
                    .padding(0.dp)
            ) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Column(modifier = Modifier.padding(0.dp)) {
                        OutlinedButton(
                            modifier = Modifier
                                .width(150.dp)
                                .fillMaxHeight(1 / 2f),
                            border = BorderStroke(2.dp, color = Color.Yellow),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = defaultSecondary,
                                contentColor = defaultOnPrimary
                            ),
                            onClick = {
                                humanNetScored.value += 1
                            }
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
                            onClick = {
                                humanNetMissed.value += 1
                            }
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
                                .fillMaxHeight(1 / 2f),
                            border = BorderStroke(2.dp, color = Color.Yellow),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = defaultSecondary,
                                contentColor = defaultOnPrimary
                            ),
                            onClick = {
                                humanNetScored.value -= 1
                                if (humanNetScored.value < 0)
                                    humanNetScored.value = 0
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
                                if (humanNetMissed.value < 0)
                                    humanNetMissed.value = 0
                            }
                        ) {
                            Text(
                                text = "-",
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }

//                OutlinedButton(
//                    border = BorderStroke(2.dp, color = Color.Yellow),
//                    shape = CircleShape,
//                    colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
//                    onClick = {
//                        backStack.push(RootNode.NavTarget.MainMenu)
//                    },
//                    modifier = Modifier
//                ) {
//                    Text(
//                        text = "Done",
//                        color = Color.Yellow,
//                        fontSize = 35.sp
//                    )
//                }
            }
        }
    ) {
        var strategyList by remember { mutableStateOf(mutableListOf("Team 0", "Team 1", "Team 2")) }
        var driveSkillList by remember { mutableStateOf(mutableListOf("Team 0", "Team 1", "Team 2")) }
        var collectorEfficiencyList by remember { mutableStateOf(mutableListOf("Team 0", "Team 1", "Team 2")) }
        var connectionList by remember { mutableStateOf(mutableListOf("Team 0", "Team 1", "Team 2")) }
        
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            teamList(strategyList, onListChanged = { strategyList = it.toMutableList(); println(strategyList) })
            HorizontalDivider()
            teamList(driveSkillList, onListChanged = { driveSkillList = it.toMutableList(); println(driveSkillList) })
        }
    }
}

@Composable
fun teamList(initialList: List<String>, onListChanged: (List<String>) -> Unit) {
    val view = LocalView.current

    var list by remember { mutableStateOf(initialList) }
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        list = list.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        onListChanged(list)

        ViewCompat.performHapticFeedback(
            view,
            HapticFeedbackConstantsCompat.SEGMENT_FREQUENT_TICK
        )
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(list, key = { it }) {
            ReorderableItem(reorderableLazyListState, key = it) { isDragging ->
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
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
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
                                },
                            ),
                            onClick = {},
                        ) {
                            Icon(Icons.Rounded.Menu, contentDescription = "Reorder")
                        }
                    }
                }
            }
        }
    }
}