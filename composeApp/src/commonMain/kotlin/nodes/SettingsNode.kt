package nodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.google.gson.JsonObject
import compKey
import pages.SettingsMenu
import theme

class SettingsNode(
    buildContext: BuildContext,
    private val mainMenuBackStack: BackStack<RootNode.NavTarget>,
) : Node(buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        SettingsMenu(mainMenuBackStack)
    }
}

//Settings variables
val miniMinus = mutableStateOf(true)
val highContrast = mutableStateOf(true)
val effects = mutableStateOf(true)
val teleFlash = mutableStateOf(true)
val matchNumberButtons = mutableStateOf(true)
val activeXPBar = mutableStateOf(true)

//Rank Variables
var totalScoutXp = mutableStateOf(100f)
var xpPerMatch = 1f
val xpInRank = mutableStateOf(1500f)
var rankIndex = 2
var minimumXpinRank = 0f
var updatedXP = mutableStateOf(true)
val maxXpList = mutableListOf(
    1500f,
    3300f,
    5175f,
    6975f,
    8550f,
    9900f
)
var xpLeft = maxXpList[rankIndex]

var scoutingRanks = HashMap<String, ArrayList<Float>>()
var ranksJson = JsonObject()


fun createRankJson(xpAdded : Int) {
    ranksJson = JsonObject().apply {
        addProperty("event_key", compKey)
        addProperty("match", match.value)
        addProperty("scout_name", scoutName.value)
        addProperty("Xp", xpAdded)
    }
}

/**
 *
 * */
fun updateScoutXP(totalScoutXp : MutableState<Float>, updateXP: MutableState<Boolean>){
    updateXP.value = false
    if(totalScoutXp.value < maxXpList[0]){
        //PolyCarb
        xpPerMatch = 100f
        rankIndex =  0
        xpLeft = totalScoutXp.value
        minimumXpinRank = 0f

    } else if(totalScoutXp.value < maxXpList[1]){
        //Copper
        xpPerMatch = 90f
        rankIndex =  1
        xpLeft = maxXpList[rankIndex].minus(maxXpList[rankIndex-1])
        minimumXpinRank = maxXpList[rankIndex-1]

    }else if(totalScoutXp.value < maxXpList[2]){
        //Aluminum
        xpPerMatch = 75f
        rankIndex =  2
        xpLeft = maxXpList[rankIndex].minus(maxXpList[rankIndex-1])
        minimumXpinRank = maxXpList[rankIndex-1]

    } else if(totalScoutXp.value < maxXpList[3]){
        //Titanium
        xpPerMatch = 60f
        rankIndex =  3
        xpLeft = maxXpList[rankIndex].minus(maxXpList[rankIndex-1])
        minimumXpinRank = maxXpList[rankIndex-1]

    }else if(totalScoutXp.value < maxXpList[4]){
        //Gold
        xpPerMatch = 45f
        rankIndex =  4
        xpLeft = maxXpList[rankIndex].minus(maxXpList[rankIndex-1])
        minimumXpinRank = maxXpList[rankIndex-1]

    }else if(totalScoutXp.value < maxXpList[5]){
        //StainlessSteel
        xpPerMatch = 30f
        rankIndex =  5
        xpLeft = maxXpList[rankIndex].minus(maxXpList[rankIndex-1])
        minimumXpinRank = maxXpList[rankIndex-1]

    }else{
        //BearMetalRank
        xpPerMatch = 15f
        rankIndex =  6
        xpLeft = maxXpList[rankIndex].minus(maxXpList[rankIndex-1])
        minimumXpinRank = maxXpList[rankIndex-1]

    }
    try {
        xpInRank.value = totalScoutXp.value - maxXpList[rankIndex-1]
    }catch (e: IndexOutOfBoundsException){
        xpInRank.value = totalScoutXp.value
    }
    if(rankIndex > 0){
       minimumXpinRank = maxXpList[rankIndex-1]
    }else{
        minimumXpinRank = 0f
    }
    updatedXP.value = true
}