package nodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.google.gson.Gson
import com.google.gson.JsonObject
import compKey
import getTeamsOnAlliance
import pages.StratMenu
import java.util.*

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

var stratJsonObject : JsonObject = JsonObject() // Don't know if there needs to be another json for strat, but just being safe for now!

var stratTeamDataArray = HashMap<TeamsAllianceKey, String>()

class TeamsAllianceKey(
    val match: Int,
    val isRed: Boolean
) {
    // Need to override equals() and hashCode() when using an object as a hashMap key:

    override fun hashCode(): Int {
        return Objects.hash(match, isRed)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TeamsAllianceKey

        if (match != other.match) return false
        if (isRed != other.isRed) return false

        return true
    }

    override fun toString(): String {
        return "$match, ${if(isRed) "Red Alliance" else "Blue Alliance"}"
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

    mechanicalSoundnessOrder.clear()
    mechanicalSoundnessOrder.addAll(teams)
}

fun nextMatch() {
    updateMatchNum(matchNum + 1)
}

var humanNetScored = mutableIntStateOf(0)
var humanNetMissed = mutableIntStateOf(0)
val strategyOrder = mutableStateListOf<Team>()
val drivingSkillOrder = mutableStateListOf<Team>()
val mechanicalSoundnessOrder = mutableStateListOf<Team>()

fun createStratOutput(): String {
    stratJsonObject = JsonObject().apply {
        addProperty("event_key", compKey)
        addProperty("match", matchNum)
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

    if (stratTeamDataArray[TeamsAllianceKey(match, isRed)] != null) {
        stratJsonObject = gson.fromJson(stratTeamDataArray[TeamsAllianceKey(match, isRed)], JsonObject::class.java)
        currentTeams = getTeamsOnAlliance(match, isRed)

        isRedAlliance = stratJsonObject.get("is_red_alliance")?.asBoolean ?: false
        matchNum = stratJsonObject.get("match")?.asInt ?: 1

        humanNetScored.value = stratJsonObject.get("human_net_scored")?.asInt ?: 0
        humanNetMissed.value = stratJsonObject.get("human_net_missed")?.asInt ?: 0

        repeat(3) {
            for (team in currentTeams) {
                if (team.number == stratJsonObject.get("strategy_order_${it + 1}")?.asInt) {
                    strategyOrder[it] = team
                    println("Found team!")
                    break
                }
            }
            println(strategyOrder.toString())
        }

        repeat(3) {
            for (team in currentTeams) {
                if (team.number == stratJsonObject.get("driving_skill_order_${it + 1}")?.asInt) {
                    drivingSkillOrder[it] = team
                    break
                }
            }
        }

        repeat(3) {
            for (team in currentTeams) {
                if (team.number == stratJsonObject.get("mechanical_soundness_order_${it + 1}")?.asInt) {
                    mechanicalSoundnessOrder[it] = team
                    break
                }
            }
        }
    } else {
        stratReset()
        stratTeamDataArray[TeamsAllianceKey(match, isRed)] = createStratOutput()
    }
}

fun stratReset() {
    humanNetMissed.intValue = 0
    humanNetScored.intValue = 0
}