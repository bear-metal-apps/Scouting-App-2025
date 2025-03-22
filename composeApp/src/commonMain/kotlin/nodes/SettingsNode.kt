package nodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.google.gson.Gson
import com.google.gson.JsonObject
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
val canTeleFlash = mutableStateOf(true)
val matchNumberButtons = mutableStateOf(true)
var canChangeRobotStartPosition = mutableStateOf(false)

fun createSettingsDataOutput() : String{
    val gson = Gson()

    var jsonObject = JsonObject().apply {
        add("settings", JsonObject().apply {
            addProperty("miniMinus", miniMinus.value)
            addProperty("highContrast", highContrast.value)
            addProperty("effects", effects.value)
            addProperty("canTeleFlash", canTeleFlash.value)
            addProperty("matchNumberButtons", matchNumberButtons.value)
            addProperty("canChangeRobotStartPosition", canChangeRobotStartPosition.value)
        })
    }
    return jsonObject.toString()
}