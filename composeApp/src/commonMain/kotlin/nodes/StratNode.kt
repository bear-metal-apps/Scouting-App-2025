package nodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import getTeamsOnAlliance
import pages.StratMenu

class StratNode(
    buildContext: BuildContext,
    private val backStack: BackStack<RootNode.NavTarget>,
    private val scoutName: MutableState<String>,
    private val comp: MutableState<String>
) : Node(buildContext) {
    @Composable
    override fun View(modifier: Modifier) {
        StratMenu(backStack, scoutName, comp, teams, isRedAlliance)
    }
}

var isRedAlliance: Boolean = false
var matchNum: Int = 1

var teams: List<Team> = getTeamsOnAlliance(matchNum, isRedAlliance)

data class Team(
    val number: Int,
    val name: String
)

fun setContext(
    redAlliance: Boolean
) {
    isRedAlliance = redAlliance
    updateMatchNum(matchNum)
}

fun updateMatchNum(matchNumber: Int) {
    if (matchNumber < 1) {
        matchNum = 1
        return
    }
    matchNum = matchNumber
    teams = getTeamsOnAlliance(matchNum, isRedAlliance)

    strategyOrder.clear()
    strategyOrder.addAll(teams)

    drivingSkillOrder.clear()
    drivingSkillOrder.addAll(teams)

    collectorOrder.clear()
    collectorOrder.addAll(teams)

    connectionOrder.clear()
    connectionOrder.addAll(teams)
}

fun nextMatch() {
    updateMatchNum(matchNum + 1)
}

var humanNetScored = mutableIntStateOf(0)
var humanNetMissed = mutableIntStateOf(0)
val strategyOrder = mutableStateListOf<Team>()
val drivingSkillOrder = mutableStateListOf<Team>()
val collectorOrder = mutableStateListOf<Team>()
val connectionOrder = mutableStateListOf<Team>()