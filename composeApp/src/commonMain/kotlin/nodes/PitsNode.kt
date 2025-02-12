package nodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import pages.PitsScoutMenu

class PitsNode(
    buildContext: BuildContext,
    private val backStack: BackStack<RootNode.NavTarget>,
    private val pitsPerson: MutableState<String>,
    private val NumOfPitsPeople: MutableIntState
) : Node(buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        PitsScoutMenu(
            backStack,
            pitsPerson,
            scoutName,
            NumOfPitsPeople
        )
    }

}

var scoutedTeamName = mutableStateOf("")
var scoutedTeamNumber = mutableStateOf("")
val photoArray = mutableStateListOf("")
var driveType = mutableStateOf("")
var motorType = mutableStateOf("")
var auto = mutableStateOf("")
var width = mutableStateOf("")
var length = mutableStateOf("")
var weight = mutableStateOf("")
var l4 = mutableStateOf(false)
var l3 = mutableStateOf(false)
var l2 = mutableStateOf(false)
var l1 = mutableStateOf(false)
var algaeBarge = mutableStateOf(false)
var algaeProcess = mutableStateOf(false)
var algaeRemoval = mutableStateOf(false)
var cycleTime = mutableStateOf("")
var rigidity = mutableStateOf("")
var coralHigh = mutableStateOf(false)
var coralLow = mutableStateOf(false)
var algaePreferred = mutableStateOf(false)
var defensePreferred = mutableStateOf(false)
var collectPreference = mutableStateOf("None Selected")
var comments = mutableStateOf("")