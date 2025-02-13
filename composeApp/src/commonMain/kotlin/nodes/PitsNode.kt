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

val pitsTeamDataArray : HashMap<Int, String> = hashMapOf<Int, String>()

fun createPitsOutput(team: MutableIntState): String {

    fun stateToInt(state: ToggleableState) = when (state) {
        ToggleableState.Off -> 0
        ToggleableState.Indeterminate -> 1
        ToggleableState.On -> 2
    }

    if (comments.value.isEmpty()){ notes.value = "No Comments"}
    comments.value = notes.value.replace(":","")

    jsonObject = JsonObject().apply {
        addProperty("team", team.intValue)
        addProperty("comp", compKey)
        addProperty("scoutName", scoutName.value)
//        addProperty("robotStartPosition", robotStartPosition.intValue)
        addProperty("autoFeederCollection", autoFeederCollection.intValue)
        addProperty("coral3Collected", stateToInt(coral3Collected.value))
        addProperty("coral2Collected", stateToInt(coral2Collected.value))
        addProperty("coral1Collected", stateToInt(coral1Collected.value))
        addProperty("algae3Collected", stateToInt(algae3Collected.value))
        addProperty("algae2Collected", stateToInt(algae2Collected.value))
        addProperty("algae1Collected", stateToInt(algae1Collected.value))
        addProperty("algaeProcessed", algaeProcessed.intValue)
        addProperty("algaeRemoved", algaeRemoved.intValue)
        addProperty("autoCoralLevel4Scored", autoCoralLevel4Scored.intValue)
        addProperty("autoCoralLevel3Scored", autoCoralLevel3Scored.intValue)
        addProperty("autoCoralLevel2Scored", autoCoralLevel2Scored.intValue)
        addProperty("autoCoralLevel1Scored", autoCoralLevel1Scored.intValue)
        addProperty("autoCoralLevel4Missed", autoCoralLevel4Missed.intValue)
        addProperty("autoCoralLevel3Missed", autoCoralLevel3Missed.intValue)
        addProperty("autoCoralLevel2Missed", autoCoralLevel2Missed.intValue)
        addProperty("autoCoralLevel1Missed", autoCoralLevel1Missed.intValue)
        addProperty("autoNetScored", autoNetScored.intValue)
        addProperty("autoNetMissed", autoNetMissed.intValue)
        addProperty("autoStop", autoStop.intValue)
        addProperty("teleNet", teleNet.intValue)
        addProperty("teleNetMissed", teleNetMissed.intValue)
        addProperty("teleLFour", teleLFour.intValue)
        addProperty("teleLThree", teleLThree.intValue)
        addProperty("teleLThreeAlgae", teleLThreeAlgae.intValue)
        addProperty("teleLTwo", teleLTwo.intValue)
        addProperty("teleLTwoAlgae", teleLTwoAlgae.intValue)
        addProperty("teleLOne", teleLOne.intValue)
        addProperty("teleProcessed", teleProcessed.intValue)
        addProperty("teleLFourMissed", teleLFourMissed.intValue)
        addProperty("teleLThreeMissed", teleLThreeMissed.intValue)
        addProperty("teleLTwoMissed", teleLTwoMissed.intValue)
        addProperty("teleLOneMissed", teleLOneMissed.intValue)
        addProperty("lostComms", lostComms.intValue)
        addProperty("playedDefense", playedDefense.value)
        addProperty("aDeep", aDeep.value)
        addProperty("bDeep", bDeep.value)
        addProperty("cDeep", cDeep.value)
        addProperty("aClimb", stateToInt(aClimb.value))
        addProperty("bClimb", stateToInt(bClimb.value))
        addProperty("cClimb", stateToInt(cClimb.value))
        addProperty("notes", notes.value)
    }
    return jsonObject.toString()
}

fun pitsReset(){
    scoutedTeamName.value = ""
    scoutedTeamNumber.value = ""
    photoArray.clear()
    driveType.value = ""
    motorType.value = ""
    auto.value = ""
    width.value = ""
    length.value = ""
    weight.value = ""
    l4.value = false
    l3.value = false
    l2.value = false
    l1.value = false
    algaeBarge.value = false
    algaeProcess.value = false
    algaeRemoval.value = false
    cycleTime.value = ""
    rigidity.value = ""
    coralHigh.value = false
    coralLow.value = false
    algaePreferred.value = false
    defensePreferred.value = false
    collectPreference.value = "None Selected"
    comments.value = ""
}