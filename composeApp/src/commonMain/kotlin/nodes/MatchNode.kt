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
import pages.MatchMenuBottom
import pages.MatchMenuTop
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
            NavTarget.AutoScouting -> AutoNode(
                buildContext,
                backStack,
                mainMenuBackStack,
                match,
                team,
                robotStartPosition
            )

            NavTarget.TeleScouting -> TeleNode(
                buildContext,
                backStack,
                mainMenuBackStack,
                match,
                team,
                robotStartPosition
            )

            NavTarget.EndGameScouting -> EndgameNode(
                buildContext,
                backStack,
                mainMenuBackStack,
                match,
                team,
                robotStartPosition,
            )
        }

    @Composable
    override fun View(modifier: Modifier) {
        Column {
            var mainMenuDialog = mutableStateOf(false)
            MatchMenuTop(match, team, robotStartPosition)
            MainMenuAlertDialog(
                mainMenuDialog,
                bob = {
                    mainMenuBackStack.pop()
                    teamDataArray.get(compKey)?.get(match.value.betterParseInt())?.set(robotStartPosition.intValue, createOutput(
                        team, robotStartPosition))
                },
                team.intValue,
                robotStartPosition.intValue)
            AppyxComponent(
                appyxComponent = backStack,
                modifier = Modifier.weight(0.9f),
            )
            MatchMenuBottom(
                robotStartPosition,
                team,
                pageIndex,
                backStack,
                mainMenuBackStack,
                mainMenuDialog
            )
        }
    }
}

data class TeamMatchStartKey(
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
        return "${match}, $team"
    }

}

var saveData = mutableStateOf(false)
var saveDataPopup = mutableStateOf(false)
var saveDataSit = mutableStateOf(false) // False = nextMatch, True = MainMenu


var teleFlash = mutableStateOf(false)

var undoList = Stack<Array<Any>>()
var redoList = Stack<Array<Any>>()
var jsonObject: JsonObject = JsonObject()

var pageIndex = mutableIntStateOf(0)

//Settings variables
val miniMinus = mutableStateOf(true)

val match = mutableStateOf("1")

var stringMatch = mutableStateOf("")
var stringTeam = mutableStateOf("")

/**
 * This value is briefly set to true if it is the first time the user is loading the data of the current match.
 */
var matchFirst = mutableStateOf(true)

// Auto
var collectCoral = mutableIntStateOf(0)
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
val teleLTwo = mutableIntStateOf(0)
val teleLOne = mutableIntStateOf(0)
val teleReefAlgaeCollected = mutableStateOf(0)
val teleRemoved = mutableIntStateOf(0)
val teleProcessed = mutableIntStateOf(0)
val teleLFourMissed = mutableIntStateOf(0)
val teleLThreeMissed = mutableIntStateOf(0)
val teleLTwoMissed = mutableIntStateOf(0)
val teleLOneMissed = mutableIntStateOf(0)
var lostComms = mutableIntStateOf(0)
var playedDefense = mutableStateOf(false)
var penalties = mutableIntStateOf(0)

// Endgame
var park = mutableStateOf(false)
var deep = mutableStateOf(false)
var shallow = mutableStateOf(false)
var notes = mutableStateOf("")

var totalAutoCoralAttempts = mutableIntStateOf(0)

fun stateToInt(state: ToggleableState) = when (state) {
    ToggleableState.Off -> 0
    ToggleableState.Indeterminate -> 1
    ToggleableState.On -> 2
}

fun createJson(team: MutableIntState, robotStartPosition: MutableIntState) {

    jsonObject = JsonObject().apply {
        addProperty("team", team.intValue.toString())
        addProperty("event_key", compKey)
        addProperty("match", match.value)
        addProperty("scout_name", scoutName.value)
        addProperty("notes", notes.value)
        addProperty("robotStartPosition", robotStartPosition.intValue)
        add("auto", JsonObject().apply {
            addProperty("stop", autoStop.intValue)
            add("algae", JsonObject().apply {
                addProperty("ground_collection", stateToInt(groundCollectionAlgae.value))
                addProperty("removed", algaeRemoved.intValue)
                addProperty("processed", algaeProcessed.intValue)
            })
            add("coral", JsonObject().apply {
                addProperty("collection", collectCoral.value)
                addProperty("reef_level1", autoCoralLevel1Scored.intValue)
                addProperty("reef_level2", autoCoralLevel2Scored.intValue)
                addProperty("reef_level3", autoCoralLevel3Scored.intValue)
                addProperty("reef_level4", autoCoralLevel4Scored.intValue)
                addProperty("reef_level1_missed", autoCoralLevel1Missed.intValue)
                addProperty("reef_level2_missed", autoCoralLevel2Missed.intValue)
                addProperty("reef_level3_missed", autoCoralLevel3Missed.intValue)
                addProperty("reef_level4_missed", autoCoralLevel4Missed.intValue)
            })
            add("net", JsonObject().apply {
                addProperty("scored", autoNetScored.intValue)
                addProperty("missed", autoNetMissed.intValue)
            })
        })
        add("tele", JsonObject().apply {
            addProperty("lost_comms", lostComms.intValue)
            addProperty("penalties", penalties.intValue)
//            addProperty("played_defense", playedDefense.value)
            add("algae", JsonObject().apply {
                addProperty("reef_removed", teleReefAlgaeCollected.value)
                addProperty("processed", teleProcessed.intValue)
            })
            add("coral", JsonObject().apply {
                addProperty("reef_level1", teleLOne.intValue)
                addProperty("reef_level2", teleLTwo.intValue)
                addProperty("reef_level3", teleLThree.intValue)
                addProperty("reef_level4", teleLFour.intValue)
                addProperty("reef_level1_missed", teleLOneMissed.intValue)
                addProperty("reef_level2_missed", teleLTwoMissed.intValue)
                addProperty("reef_level3_missed", teleLThreeMissed.intValue)
                addProperty("reef_level4_missed", teleLFourMissed.intValue)

            })
            add("net", JsonObject().apply {
                addProperty("scored", teleNet.intValue)
                addProperty("missed", teleNetMissed.intValue)
            })
        })
//        add("endgame", JsonObject().apply {
//            addProperty("park", park.value)
//            addProperty("deep", deep.value)
//            addProperty("shallow", shallow.value)
//        })
    }
}

fun createOutput(team: MutableIntState, robotStartPosition: MutableIntState): String {

    println("saved data")

    if (notes.value.isEmpty()) {
        notes.value = "No Comments"
    }
//    notes.value = notes.value.replace(":", "")

    jsonObject = JsonObject().apply {
        addProperty("team", team.intValue.toString())
        addProperty("event_key", compKey)
        addProperty("match", match.value)
        addProperty("scout_name", scoutName.value)
        addProperty("notes", notes.value)
        addProperty("robotStartPosition", robotStartPosition.intValue)
        add("auto", JsonObject().apply {
            addProperty("stop", autoStop.intValue)
            add("algae", JsonObject().apply {
                addProperty("ground_collection", stateToInt(groundCollectionAlgae.value))
                addProperty("removed", algaeRemoved.intValue)
                addProperty("processed", algaeProcessed.intValue)
            })
            add("coral", JsonObject().apply {
                addProperty("collection", collectCoral.value)
                addProperty("reef_level1", autoCoralLevel1Scored.intValue)
                addProperty("reef_level2", autoCoralLevel2Scored.intValue)
                addProperty("reef_level3", autoCoralLevel3Scored.intValue)
                addProperty("reef_level4", autoCoralLevel4Scored.intValue)
                addProperty("reef_level1_missed", autoCoralLevel1Missed.intValue)
                addProperty("reef_level2_missed", autoCoralLevel2Missed.intValue)
                addProperty("reef_level3_missed", autoCoralLevel3Missed.intValue)
                addProperty("reef_level4_missed", autoCoralLevel4Missed.intValue)
            })
            add("net", JsonObject().apply {
                addProperty("scored", autoNetScored.intValue)
                addProperty("missed", autoNetMissed.intValue)
            })
        })
        add("tele", JsonObject().apply {
            addProperty("lost_comms", lostComms.intValue)
            addProperty("penalties", penalties.intValue)
//            addProperty("played_defense", playedDefense.value)
            add("algae", JsonObject().apply {
                addProperty("reef_collected", teleReefAlgaeCollected.value)
                addProperty("processed", teleProcessed.intValue)
            })
            add("coral", JsonObject().apply {
                addProperty("reef_level1", teleLOne.intValue)
                addProperty("reef_level2", teleLTwo.intValue)
                addProperty("reef_level3", teleLThree.intValue)
                addProperty("reef_level4", teleLFour.intValue)
                addProperty("reef_level1_missed", teleLOneMissed.intValue)
                addProperty("reef_level2_missed", teleLTwoMissed.intValue)
                addProperty("reef_level3_missed", teleLThreeMissed.intValue)
                addProperty("reef_level4_missed", teleLFourMissed.intValue)

            })
            add("net", JsonObject().apply {
                addProperty("scored", teleNet.intValue)
                addProperty("missed", teleNetMissed.intValue)
            })
        })
//        add("endgame", JsonObject().apply {
//            addProperty("park", park.value)
//            addProperty("deep", deep.value)
//            addProperty("shallow", shallow.value)
//        })
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

    val gson = Gson()

    if(!teamDataArray.getOrPut(compKey) { hashMapOf() }.getOrPut(match) { hashMapOf() } .get(robotStartPosition.intValue).isNullOrEmpty()) {

        jsonObject = gson.fromJson(teamDataArray.get(compKey)?.get(match)?.get(robotStartPosition.intValue).toString(), JsonObject::class.java)

        team.intValue = jsonObject.get("team").asInt
        compKey = jsonObject.get("event_key").asString
//        match.value = parseInt(jsonObject.get("match").asString)
        scoutName.value = jsonObject.get("scout_name").asString
        robotStartPosition.intValue = jsonObject.get("robotStartPosition").asInt
        groundCollectionAlgae.value =
            intToState(jsonObject.getAsJsonObject("auto").getAsJsonObject("algae").get("ground_collection").asInt)
        collectCoral.value =
            jsonObject.getAsJsonObject("auto").getAsJsonObject("coral").get("collection").asInt
        algaeProcessed.intValue = jsonObject.getAsJsonObject("auto").getAsJsonObject("algae").get("processed").asInt
        algaeRemoved.intValue = jsonObject.getAsJsonObject("auto").getAsJsonObject("algae").get("removed").asInt
        autoCoralLevel4Scored.intValue =
            jsonObject.getAsJsonObject("auto").getAsJsonObject("coral").get("reef_level4").asInt
        autoCoralLevel3Scored.intValue =
            jsonObject.getAsJsonObject("auto").getAsJsonObject("coral").get("reef_level3").asInt
        autoCoralLevel2Scored.intValue =
            jsonObject.getAsJsonObject("auto").getAsJsonObject("coral").get("reef_level2").asInt
        autoCoralLevel1Scored.intValue =
            jsonObject.getAsJsonObject("auto").getAsJsonObject("coral").get("reef_level1").asInt
        autoCoralLevel4Missed.intValue =
            jsonObject.getAsJsonObject("auto").getAsJsonObject("coral").get("reef_level4_missed").asInt
        autoCoralLevel3Missed.intValue =
            jsonObject.getAsJsonObject("auto").getAsJsonObject("coral").get("reef_level3_missed").asInt
        autoCoralLevel2Missed.intValue =
            jsonObject.getAsJsonObject("auto").getAsJsonObject("coral").get("reef_level2_missed").asInt
        autoCoralLevel1Missed.intValue =
            jsonObject.getAsJsonObject("auto").getAsJsonObject("coral").get("reef_level1_missed").asInt
        autoNetScored.intValue = jsonObject.getAsJsonObject("auto").getAsJsonObject("net").get("scored").asInt
        autoNetMissed.intValue = jsonObject.getAsJsonObject("auto").getAsJsonObject("net").get("missed").asInt
        autoStop.intValue = jsonObject.getAsJsonObject("auto").get("stop").asInt
        penalties.intValue = jsonObject.getAsJsonObject("tele").get("penalties").asInt
        teleNet.intValue = jsonObject.getAsJsonObject("tele").getAsJsonObject("net").get("scored").asInt
        teleNetMissed.intValue = jsonObject.getAsJsonObject("tele").getAsJsonObject("net").get("missed").asInt
        teleLFour.intValue = jsonObject.getAsJsonObject("tele").getAsJsonObject("coral").get("reef_level4").asInt
        teleLThree.intValue = jsonObject.getAsJsonObject("tele").getAsJsonObject("coral").get("reef_level3").asInt
        teleLTwo.intValue = jsonObject.getAsJsonObject("tele").getAsJsonObject("coral").get("reef_level2").asInt
        teleLOne.intValue = jsonObject.getAsJsonObject("tele").getAsJsonObject("coral").get("reef_level1").asInt
        teleReefAlgaeCollected.value =
            jsonObject.getAsJsonObject("tele").getAsJsonObject("algae").get("reef_collected").asInt
        teleProcessed.intValue = jsonObject.getAsJsonObject("tele").getAsJsonObject("algae").get("processed").asInt
        teleLFourMissed.intValue =
            jsonObject.getAsJsonObject("tele").getAsJsonObject("coral").get("reef_level4_missed").asInt
        teleLThreeMissed.intValue =
            jsonObject.getAsJsonObject("tele").getAsJsonObject("coral").get("reef_level3_missed").asInt
        teleLTwoMissed.intValue =
            jsonObject.getAsJsonObject("tele").getAsJsonObject("coral").get("reef_level2_missed").asInt
        teleLOneMissed.intValue =
            jsonObject.getAsJsonObject("tele").getAsJsonObject("coral").get("reef_level1_missed").asInt
        lostComms.intValue = jsonObject.getAsJsonObject("tele").get("lost_comms").asInt
//        playedDefense.value = jsonObject.getAsJsonObject("tele").get("played_defense").asBoolean
//        park.value = jsonObject.getAsJsonObject("endgame").get("park").asBoolean
//        deep.value = jsonObject.getAsJsonObject("endgame").get("deep").asBoolean
//        shallow.value = jsonObject.getAsJsonObject("endgame").get("shallow").asBoolean
        notes.value = if(jsonObject.get("notes").asString == "No Comments") "" else jsonObject.get("notes").asString

        saveData.value = true

    } else {
        reset()
        if(saveData.value) {
            teamDataArray.getOrPut(compKey) { hashMapOf() }.getOrPut(match) { hashMapOf() }.getOrPut(robotStartPosition.intValue) {createOutput(team, robotStartPosition) }
        }
    }
}

fun reset() {

    collectCoral.intValue = 0
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
    teleLTwo.intValue = 0
    teleLOne.intValue = 0
    teleReefAlgaeCollected.value = 0
    teleRemoved.intValue = 0
    teleProcessed.intValue = 0
    teleLFourMissed.intValue = 0
    teleLThreeMissed.intValue = 0
    teleLTwoMissed.intValue = 0
    teleLOneMissed.intValue = 0
    lostComms.intValue = 0
    playedDefense.value = false
    penalties.intValue = 0
    park.value = false
    deep.value = false
    shallow.value = false
    notes.value = ""

}