package nodes

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.BackStackModel
import com.bumble.appyx.components.backstack.ui.fader.BackStackFader
import com.bumble.appyx.navigation.composable.AppyxComponent
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.ParentNode
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import com.google.gson.JsonObject
import compKey
import pages.AutoTeleSelectorMenuBottom
import pages.AutoTeleSelectorMenuTop
import java.util.*

class AutoTeleSelectorNode(
    buildContext: BuildContext,
    private var robotStartPosition: MutableIntState,
    private val team: MutableIntState,
    private val mainMenuBackStack: BackStack<RootNode.NavTarget>,
    private val backStack: BackStack<NavTarget> = BackStack(
        model = BackStackModel(
            initialTarget = NavTarget.AutoScouting,
            savedStateMap = buildContext.savedStateMap
        ),
        visualisation = { BackStackFader(it) }
    )
) : ParentNode<AutoTeleSelectorNode.NavTarget>(
    appyxComponent = backStack,
    buildContext = buildContext
) {
    private val selectAuto = mutableStateOf("Auto")

    sealed class NavTarget : Parcelable {
        @Parcelize
        data object AutoScouting : NavTarget()

        @Parcelize
        data object TeleScouting : NavTarget()

        @Parcelize
        data object EndGameScouting : NavTarget()
    }

    override fun resolve(interactionTarget: NavTarget, buildContext: BuildContext): Node =
        when (interactionTarget) {
            NavTarget.AutoScouting -> AutoNode(buildContext, backStack, mainMenuBackStack, match, team, robotStartPosition)
            NavTarget.TeleScouting -> TeleNode(buildContext, backStack, mainMenuBackStack, match, team, robotStartPosition)
            NavTarget.EndGameScouting -> EndgameNode(buildContext,backStack, mainMenuBackStack, match, team, robotStartPosition )
        }

    @Composable
    override fun View(modifier: Modifier) {
        Column {
            AutoTeleSelectorMenuTop(match, team, robotStartPosition)
            AppyxComponent(
                appyxComponent = backStack,
                modifier = Modifier.weight(0.9f),
            )
            AutoTeleSelectorMenuBottom(robotStartPosition,team,selectAuto,backStack,mainMenuBackStack)
        }
    }
}

class TeamMatchKey(
    var match: Int,
    var team: Int
) {

    // Need to override equals() and hashCode() when using an object as a hashMap key:

    override fun hashCode(): Int {
        return Objects.hash(match, team)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TeamMatchKey

        if (match != other.match) return false
        if (team != other.team) return false

        return true
    }

    override fun toString(): String {
        return "${match}, $team"
    }
}
var undoList = Stack<Array<Any>>()
var redoList = Stack<Array<Any>>()
var jsonObject : JsonObject = JsonObject()

val match = mutableStateOf("1")

//CHECKED
var autoFeederCollection = mutableIntStateOf(0)
var coral3Collected = mutableStateOf(ToggleableState.Off)
var coral2Collected = mutableStateOf(ToggleableState.Off)
var coral1Collected = mutableStateOf(ToggleableState.Off)
var algae3Collected = mutableStateOf(ToggleableState.Off)
var algae2Collected = mutableStateOf(ToggleableState.Off)
var algae1Collected = mutableStateOf(ToggleableState.Off)
var algaeProcessed = mutableIntStateOf(0)
var algaeRemoved = mutableIntStateOf(0)
var autoCoralLevel4Scored = mutableIntStateOf(0)
var autoCoralLevel3Scored = mutableIntStateOf(0)
var autoCoralLevel2Scored = mutableIntStateOf(0)
var autoCoralLevel1Scored = mutableIntStateOf(0)
var autoCoralLevel4Missed = mutableIntStateOf(0)
var autoCoralLevel3Missed = mutableIntStateOf(0)
var autoCoralLevel2Missed = mutableIntStateOf(0)
var autoCoralLevel1Missed = mutableIntStateOf(0)
var autoNetScored = mutableIntStateOf(0)
var autoNetMissed = mutableIntStateOf(0)
val autoStop = mutableIntStateOf(0)

//CHECKED
val teleNet = mutableIntStateOf(0)
val teleNetMissed = mutableIntStateOf(0)
val teleLFour = mutableIntStateOf(0)
val teleLThree = mutableIntStateOf(0)
val teleLThreeAlgae = mutableIntStateOf(0)
val teleLTwo = mutableIntStateOf(0)
val teleLTwoAlgae = mutableIntStateOf(0)
val teleLOne = mutableIntStateOf(0)
val teleProcessed = mutableIntStateOf(0)
val teleLFourMissed = mutableIntStateOf(0)
val teleLThreeMissed = mutableIntStateOf(0)
val teleLTwoMissed = mutableIntStateOf(0)
val teleLOneMissed = mutableIntStateOf(0)
var lostComms = mutableIntStateOf(0)
var playedDefense = mutableStateOf(false)

//CHECKED
var aDeep = mutableStateOf(false)
var bDeep = mutableStateOf(false)
var cDeep = mutableStateOf(false)
var aClimb = mutableStateOf(ToggleableState(false))
var bClimb = mutableStateOf(ToggleableState(false))
var cClimb = mutableStateOf(ToggleableState(false))
var notes = mutableStateOf("")


fun createOutput(team: MutableIntState, robotStartPosition: MutableIntState): String {

    fun stateToInt(state: ToggleableState) = when (state) {
        ToggleableState.Off -> 0
        ToggleableState.Indeterminate -> 1
        ToggleableState.On -> 2
    }

    if (notes.value.isEmpty()){ notes.value = "No Comments"}
    notes.value = notes.value.replace(":","")

    jsonObject = JsonObject().apply {
        addProperty("match", match.value)
        addProperty("team", team.intValue)
        addProperty("comp", compKey)
        addProperty("scoutName", scoutName.value)
        addProperty("robotStartPosition", robotStartPosition.intValue)
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

fun loadData(match: Int, team: MutableIntState, robotStartPosition: MutableIntState) {

    fun intToState(i: Int) = when (i) {
        0 -> ToggleableState.Off
        1 -> ToggleableState.Indeterminate
        2 -> ToggleableState.On
        else -> ToggleableState.Off
    }

//    //Null possibility will most likely never happen.
//    if((teamDataArray[TeamMatchKey(match, team.value)]?.split("\n")) == null) {
//        print("null")
//    }
//
//    val list : MutableList<String> =
//        ((teamDataArray[TeamMatchKey(match, team.value)]?.split("\n"))?.toMutableList()?: createOutput(team, robotStartPosition).split("\n").toMutableList()).toMutableList()
//
//    println(list)
//
//    list.withIndex().forEach { (index, it) ->
//        var firstIndex: Int
//        for ((letterIndex, letter) in it.withIndex()) {
//            if (letter == ':') {
//                if(list[index].get(letterIndex+1).toString() == "\"") {
//                    firstIndex = letterIndex + 2
//                    list[index] = it.substring(firstIndex, it.length - 2)
//                } else {
//                    firstIndex = letterIndex + 1
//                    list[index] = it.substring(firstIndex, it.length - 1)
//                }
//            }
//        }
//    }
//    list.removeAt(0)
//    if(list.lastIndex == 47) { //TODO: IMPROVE THIS
//        list.removeAt(list.lastIndex)
//    }
//
//    println(list)

    if(teamDataArray[TeamMatchKey(match, team.value)] != null) {
        team.intValue = jsonObject.get("team").asInt
        compKey = jsonObject.get("comp").asString
        scoutName.value = jsonObject.get("scoutName").asString
        robotStartPosition.intValue = jsonObject.get("robotStartPosition").asInt
        autoFeederCollection.intValue = jsonObject.get("autoFeederCollection").asInt
        coral3Collected.value = intToState(jsonObject.get("coral3Collected").asInt)
        coral2Collected.value = intToState(jsonObject.get("coral2Collected").asInt)
        coral1Collected.value = intToState(jsonObject.get("coral1Collected").asInt)
        algae3Collected.value = intToState(jsonObject.get("algae3Collected").asInt)
        algae2Collected.value = intToState(jsonObject.get("algae2Collected").asInt)
        algae1Collected.value = intToState(jsonObject.get("algae1Collected").asInt)
        algaeProcessed.intValue = jsonObject.get("algaeProcessed").asInt
        algaeRemoved.intValue = jsonObject.get("algaeRemoved").asInt
        autoCoralLevel4Scored.intValue = jsonObject.get("autoCoralLevel4Scored").asInt
        autoCoralLevel3Scored.intValue = jsonObject.get("autoCoralLevel3Scored").asInt
        autoCoralLevel2Scored.intValue = jsonObject.get("autoCoralLevel2Scored").asInt
        autoCoralLevel1Scored.intValue = jsonObject.get("autoCoralLevel1Scored").asInt
        autoCoralLevel4Missed.intValue = jsonObject.get("autoCoralLevel4Missed").asInt
        autoCoralLevel3Missed.intValue = jsonObject.get("autoCoralLevel3Missed").asInt
        autoCoralLevel2Missed.intValue = jsonObject.get("autoCoralLevel2Missed").asInt
        autoCoralLevel1Missed.intValue = jsonObject.get("autoCoralLevel1Missed").asInt
        autoNetScored.intValue = jsonObject.get("autoNetScored").asInt
        autoNetMissed.intValue = jsonObject.get("autoNetMissed").asInt
        autoStop.intValue = jsonObject.get("autoStop").asInt
        teleNet.intValue = jsonObject.get("teleNet").asInt
        teleNetMissed.intValue = jsonObject.get("teleNetMissed").asInt
        teleLFour.intValue = jsonObject.get("teleLFour").asInt
        teleLThree.intValue = jsonObject.get("teleLThree").asInt
        teleLThreeAlgae.intValue = jsonObject.get("teleLThreeAlgae").asInt
        teleLTwo.intValue = jsonObject.get("teleLTwo").asInt
        teleLTwoAlgae.intValue = jsonObject.get("teleLTwoAlgae").asInt
        teleLOne.intValue = jsonObject.get("teleLOne").asInt
        teleProcessed.intValue = jsonObject.get("teleProcessed").asInt
        teleLFourMissed.intValue = jsonObject.get("teleLFourMissed").asInt
        teleLThreeMissed.intValue = jsonObject.get("teleLThreeMissed").asInt
        teleLTwoMissed.intValue = jsonObject.get("teleLTwoMissed").asInt
        teleLOneMissed.intValue = jsonObject.get("teleLOneMissed").asInt
        lostComms.intValue = jsonObject.get("lostComms").asInt
        playedDefense.value = jsonObject.get("playedDefense").asBoolean
        aDeep.value = jsonObject.get("aDeep").asBoolean
        bDeep.value = jsonObject.get("bDeep").asBoolean
        cDeep.value = jsonObject.get("cDeep").asBoolean
        aClimb.value = intToState(jsonObject.get("aClimb").asInt)
        bClimb.value = intToState(jsonObject.get("bClimb").asInt)
        cClimb.value = intToState(jsonObject.get("cClimb").asInt)
        notes.value = jsonObject.get("notes").asString
    } else {
        println("match is null!")
    }
}

fun reset(){

    compKey = ""
    scoutName.value = ""
    autoFeederCollection.intValue = 0
    coral3Collected.value = ToggleableState.Off
    coral2Collected.value = ToggleableState.Off
    coral1Collected.value = ToggleableState.Off
    algae3Collected.value = ToggleableState.Off
    algae2Collected.value = ToggleableState.Off
    algae1Collected.value = ToggleableState.Off
    algaeProcessed.intValue = 0
    algaeRemoved.intValue = 0
    autoCoralLevel4Scored.intValue = 0
    autoCoralLevel3Scored.intValue = 0
    autoCoralLevel2Scored.intValue = 0
    autoCoralLevel1Scored.intValue = 0
    autoCoralLevel4Missed.intValue = 0
    autoCoralLevel3Missed.intValue = 0
    autoCoralLevel2Missed.intValue = 0
    autoCoralLevel1Missed.intValue = 0
    autoNetScored.intValue = 0
    autoNetMissed.intValue = 0
    autoStop.intValue = 0
    teleNet.intValue = 0
    teleNetMissed.intValue = 0
    teleLFour.intValue = 0
    teleLThree.intValue = 0
    teleLThreeAlgae.intValue = 0
    teleLTwo.intValue = 0
    teleLTwoAlgae.intValue = 0
    teleLOne.intValue = 0
    teleProcessed.intValue = 0
    teleLFourMissed.intValue = 0
    teleLThreeMissed.intValue = 0
    teleLTwoMissed.intValue = 0
    teleLOneMissed.intValue = 0
    lostComms.intValue = 0
    playedDefense.value = false
    aDeep.value = false
    bDeep.value = false
    cDeep.value = false
    aClimb.value = ToggleableState.Off
    bClimb.value = ToggleableState.Off
    cClimb.value = ToggleableState.Off
    notes.value = ""

}