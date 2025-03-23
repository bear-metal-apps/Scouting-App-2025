package nodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
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
var totalScoutXp = mutableStateOf(3700f)
var xpPerMatch = 100f
val xpInRank = mutableStateOf(1800f)
var rankIndex = 1
val maxXpList = mutableListOf(
    1500f,
    3300f,
    5175f,
    6975f,
    8550f,
    9900f
)

/**
 *
 * */
fun updateScoutXP(totalScoutXp : MutableState<Float>, rankImage : MutableState<String>, updateXP: MutableState<Boolean>){
    updateXP.value = false
    if(totalScoutXp.value < maxXpList[0]){
        //PolyCarb
        rankImage.value = "PolyCarb.png"
        xpPerMatch = 100f
        rankIndex =  0

    } else if(totalScoutXp.value < maxXpList[1]){
        //Copper
        rankImage.value = "Copper.png"
        xpPerMatch = 90f
        rankIndex =  1

    }else if(totalScoutXp.value < maxXpList[2]){
        //Aluminum
        rankImage.value = "Aluminum.png"
        xpPerMatch = 75f
        rankIndex =  2

    } else if(totalScoutXp.value < maxXpList[3]){
        //Titanium
        rankImage.value = "Titanium.png"
        xpPerMatch = 60f
        rankIndex =  3

    }else if(totalScoutXp.value < maxXpList[4]){
        //Gold
        rankImage.value = "Gold.png"
        xpPerMatch = 45f
        rankIndex =  4

    }else if(totalScoutXp.value < maxXpList[5]){
        //StainlessSteel
        rankImage.value = "StainlessSteel.png"
        xpPerMatch = 30f
        rankIndex =  5

    }else{
        //BearMetalRank
        rankImage.value = "BearMetalRank.png"
        xpPerMatch = 15f
        rankIndex =  6

    }
    xpInRank.value = totalScoutXp.value - maxXpList[rankIndex-1]
}