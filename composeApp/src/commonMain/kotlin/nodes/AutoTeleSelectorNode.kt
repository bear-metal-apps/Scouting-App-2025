package nodes

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.BackStackModel
import com.bumble.appyx.components.backstack.operation.pop
import com.bumble.appyx.components.backstack.ui.fader.BackStackFader
import com.bumble.appyx.navigation.composable.AppyxComponent
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.ParentNode
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import com.google.gson.Gson
import com.google.gson.JsonObject
import compKey
import composables.MainMenuAlertDialog
import pages.AutoTeleSelectorMenuBottom
import pages.AutoTeleSelectorMenuTop
import java.util.*
import java.util.Stack
import java.util.Objects


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
            var mainMenuDialog = mutableStateOf(false)
            var pageIndex = mutableIntStateOf(0)
            AutoTeleSelectorMenuTop(match, team, robotStartPosition, pageIndex)
            MainMenuAlertDialog(mainMenuDialog,
                bob = {
                        mainMenuBackStack.pop()
                },
                team.intValue,
                robotStartPosition.intValue)
            AppyxComponent(
                appyxComponent = backStack,
                modifier = Modifier.weight(0.9f),
            )
            AutoTeleSelectorMenuBottom(robotStartPosition,team,pageIndex,backStack,mainMenuBackStack,mainMenuDialog)
        }
    }
}

class TeamMatchStartKey(
    var match: Int,
    var team: Int,
    var robotStartPosition: Int
) {

    // Need to override equals() and hashCode() when using an object as a hashMap key:

    override fun hashCode(): Int {
        return Objects.hash(match, team, robotStartPosition)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TeamMatchStartKey

        if (match != other.match) return false
        if (team != other.team) return false
        if(robotStartPosition != other.robotStartPosition) return false

        return true
    }

    override fun toString(): String {
        return "${match}, ${team}"
    }

}

var saveData = mutableStateOf(false)
var saveDataPopup = mutableStateOf(false)
var saveDataSit = mutableStateOf(false) // False = nextMatch, True = MainMenu

var undoList = Stack<Array<Any>>()
var redoList = Stack<Array<Any>>()
var jsonObject : JsonObject = JsonObject()

val match = mutableStateOf("1")

var tempMatch = match.value
var tempTeam : Int = 0

// Auto
var autoFeederCollection = mutableIntStateOf(0)
var groundCollectionCoral = mutableStateOf(ToggleableState.Off)
var groundCollectionAlgae = mutableStateOf(ToggleableState.Off)
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

// Tele
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

// Endgame
var park = mutableStateOf(false)
var deep = mutableStateOf(false)
var shallow = mutableStateOf(false)
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
        addProperty("groundCollectionAlgae", stateToInt(groundCollectionAlgae.value))
        addProperty("groundCollectionCoral", stateToInt(groundCollectionCoral.value))
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
        addProperty("park", park.value)
        addProperty("deep", deep.value)
        addProperty("shallow", shallow.value)
        addProperty("notes", notes.value)
    }
//    println(jsonObject.toString())
    return jsonObject.toString()
}

fun loadData(match: Int, team: MutableIntState, robotStartPosition: MutableIntState) {

    fun intToState(i: Int) = when (i) {
        0 -> ToggleableState.Off
        1 -> ToggleableState.Indeterminate
        2 -> ToggleableState.On
        else -> ToggleableState.Off
    }

    val gson = Gson()

    if(teamDataArray[TeamMatchStartKey(match, team.value, robotStartPosition.intValue)] != null) {

        jsonObject = gson.fromJson(teamDataArray[TeamMatchStartKey(match, team.value, robotStartPosition.intValue)].toString(), JsonObject::class.java)

        team.intValue = jsonObject.get("team").asInt
        compKey = jsonObject.get("comp").asString
        scoutName.value = jsonObject.get("scoutName").asString
        robotStartPosition.intValue = jsonObject.get("robotStartPosition").asInt
        autoFeederCollection.intValue = jsonObject.get("autoFeederCollection").asInt
        groundCollectionAlgae.value = intToState(jsonObject.get("groundCollectionAlgae").asInt)
        groundCollectionCoral.value = intToState(jsonObject.get("groundCollectionCoral").asInt)
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
        park.value = jsonObject.get("park").asBoolean
        deep.value = jsonObject.get("deep").asBoolean
        shallow.value = jsonObject.get("shallow").asBoolean
        notes.value = jsonObject.get("notes").asString
    } else {
        println("match data is null")
        reset()
        if(saveData.value) {
            teamDataArray[TeamMatchStartKey(match, team.intValue, robotStartPosition.intValue)] = createOutput(team, robotStartPosition)
        }
    }
}

fun reset(){

    autoFeederCollection.intValue = 0
    groundCollectionCoral.value = ToggleableState.Off
    groundCollectionAlgae.value = ToggleableState.Off
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
    park.value = false
    deep.value = false
    shallow.value = false
    notes.value = ""

}