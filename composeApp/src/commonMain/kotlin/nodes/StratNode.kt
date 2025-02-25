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

var stratTeamDataArray = HashMap<teamsAllianceKey, String>()

class teamsAllianceKey(
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

        other as teamsAllianceKey

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

fun createStratOutput(): String {

    val gson = Gson()

    stratJsonObject = JsonObject().apply {
        addProperty("event_key", matchNum)
        addProperty("is_red_alliance", isRedAlliance)

        addProperty("humanNetScored", humanNetScored.intValue)
        addProperty("humanNetMissed", humanNetMissed.intValue)

        addProperty("strategyOrder1", strategyOrder[0].number)
        addProperty("strategyOrder2", strategyOrder[1].number)
        addProperty("strategyOrder3", strategyOrder[2].number)

        addProperty("drivingSkillOrder1", drivingSkillOrder[0].number)
        addProperty("drivingSkillOrder2", drivingSkillOrder[1].number)
        addProperty("drivingSkillOrder3", drivingSkillOrder[2].number)

        addProperty("collectorOrder1", collectorOrder[0].number)
        addProperty("collectorOrder2", collectorOrder[1].number)
        addProperty("collectorOrder3", collectorOrder[2].number)

        addProperty("connectionOrder1", connectionOrder[0].number)
        addProperty("connectionOrder2", connectionOrder[1].number)
        addProperty("connectionOrder3", connectionOrder[2].number)
    }

    return stratJsonObject.toString()

}

fun loadStratData(match: Int, isRed: Boolean) {

    val gson = Gson()

    val currentTeams : List<Team>

    if(stratTeamDataArray[teamsAllianceKey(match, isRed)] != null) {

        stratJsonObject = gson.fromJson(stratTeamDataArray[teamsAllianceKey(match, isRed)], JsonObject::class.java)
        currentTeams = getTeamsOnAlliance(match, isRed)

        isRedAlliance = stratJsonObject.get("is_red_alliance").asBoolean
        matchNum = stratJsonObject.get("event_key").asInt

        humanNetScored.value = stratJsonObject.get("humanNetScored").asInt
        humanNetMissed.value = stratJsonObject.get("humanNetMissed").asInt

        repeat(3) {
            for(team in currentTeams) {
                if(team.number == stratJsonObject.get("strategyOrder${it+1}").asInt) {
                    strategyOrder[it] = team
                    println("Found team!")
                    break
                }
            }
            println(strategyOrder.toString())
        }

        repeat(3) {
            for(team in currentTeams) {
                if(team.number == stratJsonObject.get("drivingSkillOrder${it+1}").asInt) {
                    drivingSkillOrder[it] = team
                    break
                }
            }
        }

        repeat(3) {
            for(team in currentTeams) {
                if(team.number == stratJsonObject.get("collectorOrder${it+1}").asInt) {
                    collectorOrder[it] = team
                    break
                }
            }
        }

        repeat(3) {
            for(team in currentTeams) {
                if(team.number == stratJsonObject.get("connectionOrder${it+1}").asInt) {
                    connectionOrder[it] = team
                    break
                }
            }
        }

    } else {
        stratReset()
        stratTeamDataArray[teamsAllianceKey(match, isRed)] = createStratOutput()
    }

}

fun stratReset() {
    humanNetMissed.intValue = 0
    humanNetScored.intValue = 0
}