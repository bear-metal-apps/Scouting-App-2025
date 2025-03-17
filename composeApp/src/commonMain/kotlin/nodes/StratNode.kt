package nodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.google.gson.Gson
import com.google.gson.JsonObject
import compKey
import getTeamsOnAlliance
import isSynced
import pages.StratMenu
import java.util.*
import kotlin.collections.HashMap

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

var stratJsonObject: JsonObject =
    JsonObject() // Don't know if there needs to be another json for strat, but just being safe for now!

//var stratTeamDataArray = HashMap<TeamsAllianceKey, String>()
var stratTeamDataArray = HashMap<String, HashMap<Int, HashMap<Boolean, String>>>()

var saveStratData = mutableStateOf(false)

var saveStratDataPopup = mutableStateOf(false)

/**
 * True = the user is exiting the match using the main menu button.
 *
 * False = the user is exiting the match using the next match button.
 */
var saveStratDataSit = mutableStateOf(false) // false = nextMatch, true = main men



var isRedAlliance: Boolean = false
var stratMatch: Int = 1

var tempStratMatch = stratMatch

var teams: List<Team> = getTeamsOnAlliance(stratMatch, isRedAlliance)

data class Team(
    val number: Int,
    val name: String
)

fun setContext(
    redAlliance: Boolean
) {
    isRedAlliance = redAlliance
    updateMatchNum(stratMatch)
}

fun updateMatchNum(matchNumber: Int) {
    if (matchNumber < 1) {
        stratMatch = 1
        return
    }
    stratMatch = matchNumber
    teams = getTeamsOnAlliance(stratMatch, isRedAlliance)

    strategyOrder.clear()
    strategyOrder.addAll(teams)

    drivingSkillOrder.clear()
    drivingSkillOrder.addAll(teams)

    mechanicalSoundnessOrder.clear()
    mechanicalSoundnessOrder.addAll(teams)

//    loadStratData(stratMatch, isRedAlliance)
}

fun nextMatch() {
    updateMatchNum(stratMatch + 1)
}

var humanNetScored = mutableIntStateOf(0)
var humanNetMissed = mutableIntStateOf(0)
val strategyOrder = mutableStateListOf<Team>()
val drivingSkillOrder = mutableStateListOf<Team>()
val mechanicalSoundnessOrder = mutableStateListOf<Team>()

fun createStratOutput(match: Int): String {

    println("saved data")

    stratJsonObject = JsonObject().apply {
        addProperty("event_key", compKey)
        addProperty("match", match)
        addProperty("is_red_alliance", isRedAlliance)

        addProperty("human_net_scored", humanNetScored.intValue)
        addProperty("human_net_missed", humanNetMissed.intValue)

        add("strategy", JsonObject().apply {
            for (i in strategyOrder.indices) {
                addProperty("${i + 1}", strategyOrder[i].number)
            }
        })

        add("driving_skill", JsonObject().apply {
            for (i in drivingSkillOrder.indices) {
                addProperty("${i + 1}", drivingSkillOrder[i].number)
            }
        })

        add("mechanical_soundness", JsonObject().apply {
            for (i in mechanicalSoundnessOrder.indices) {
                addProperty("${i + 1}", mechanicalSoundnessOrder[i].number)
            }
        })
    }

    return stratJsonObject.toString()
}

fun loadStratData(match: Int, isRed: Boolean) {
    val gson = Gson()
    val currentTeams: List<Team>

    if (!stratTeamDataArray.getOrPut(compKey) { hashMapOf() }.getOrPut(stratMatch) { hashMapOf() }.get(isRed).isNullOrEmpty()) {
        stratJsonObject = gson.fromJson(stratTeamDataArray.getOrPut(compKey) { hashMapOf() }.get(stratMatch)?.get(isRed), JsonObject::class.java)

        currentTeams = getTeamsOnAlliance(match, isRed)

        isRedAlliance = stratJsonObject.get("is_red_alliance")?.asBoolean ?: false
        stratMatch = stratJsonObject.get("match")?.asInt ?: 1

        humanNetScored.value = stratJsonObject.get("human_net_scored")?.asInt ?: 0
        humanNetMissed.value = stratJsonObject.get("human_net_missed")?.asInt ?: 0

        repeat(currentTeams.size) {
            for (team in currentTeams) {
                if (team.number == stratJsonObject.get("strategy")?.asJsonObject?.get("${it + 1}")?.asInt) {
                    strategyOrder[it] = team
                    break
                }
            }
            println(strategyOrder.toString())
        }

        repeat(3) {
            for (team in currentTeams) {
                if (team.number == stratJsonObject.get("driving_skill")?.asJsonObject?.get("${it + 1}")?.asInt) {
                    drivingSkillOrder[it] = team
                    break
                }
            }
        }

        repeat(3) {
            for (team in currentTeams) {
                if (team.number == stratJsonObject.get("mechanical_soundness")?.asJsonObject?.get("${it + 1}")?.asInt) {
                    mechanicalSoundnessOrder[it] = team
                    break
                }
            }
        }

        saveStratData.value = true

    } else {
        stratReset()
        if(saveStratData.value && isSynced()) {
            stratTeamDataArray.getOrPut(compKey) { hashMapOf() }.getOrPut(stratMatch) { hashMapOf() }.set(isRed, createStratOutput(stratMatch))
        }
    }
}

fun stratReset() {
    humanNetMissed.intValue = 0
    humanNetScored.intValue = 0
}