package nodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import compKey
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
val photoArray = mutableListOf<String>()
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
var bargePreferred = mutableStateOf(false)
var processPreferred = mutableStateOf(false)
var defensePreferred = mutableStateOf(false)
var collectPreference = mutableStateOf("None Selected")
var comments = mutableStateOf("")

var permPhotosList = mutableListOf<String>()

val pitsTeamDataArray : HashMap<Int, String> = hashMapOf()

var pitsImgJsonObj = JsonArray()

fun createPitsOutput(team: MutableIntState): String {

    fun stateToInt(state: ToggleableState) = when (state) {
        ToggleableState.Off -> 0
        ToggleableState.Indeterminate -> 1
        ToggleableState.On -> 2
    }

    if (comments.value.isEmpty()){ notes.value = "No Comments"}
    comments.value = notes.value.replace(":","")

    var gson = Gson()

    if(pitsTeamDataArray[team.intValue] == null) {
        pitsTeamDataArray[team.intValue] = "{}"
        jsonObject = gson.fromJson(pitsTeamDataArray[team.intValue].toString(), JsonObject::class.java)
    } else {
        jsonObject = gson.fromJson(pitsTeamDataArray[team.intValue].toString(), JsonObject::class.java)
    }

    //Adds Uris from permPhotosList to a JsonArray that is permanently stored as a file
//    pitsImgJsonObj = JsonArray().apply {
//        for(img in permPhotosList) {
//            add(img)
//        }
//    }

    jsonObject = JsonObject().apply {
        addProperty("team", scoutedTeamNumber.value)
        addProperty("team_name", scoutedTeamName.value)
        addProperty("event_key", compKey)
        addProperty("scout_name", scoutName.value)
        for((index, value) in photoArray.withIndex()) {
            addProperty("Photo${index}", value)
        }
        addProperty("driveType", driveType.value)
        addProperty("motorType", motorType.value)
        addProperty("auto", auto.value)
        addProperty("width", width.value)
        addProperty("length", length.value)
        addProperty("weight", weight.value)
        addProperty("l4", l4.value)
        addProperty("l3", l3.value)
        addProperty("l2", l2.value)
        addProperty("l1", l1.value)
        addProperty("algaeBarge", algaeBarge.value)
        addProperty("algaeProcess", algaeProcess.value)
        addProperty("algaeRemoval", algaeRemoval.value)
        addProperty("cycleTime", cycleTime.value)
        addProperty("rigidity", rigidity.value)
        addProperty("coralHigh", coralHigh.value)
        addProperty("coralLow", coralLow.value)
        addProperty("bargePreferred", bargePreferred.value)
        addProperty("processPreferred", processPreferred.value)
        addProperty("defensePreferred", defensePreferred.value)
        addProperty("collectPreference", collectPreference.value)
        addProperty("comments", comments.value)
    }
    println(gson.toJson(photoArray))
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
    bargePreferred.value = false
    processPreferred.value = false
    defensePreferred.value = false
    collectPreference.value = "None Selected"
    comments.value = ""

}