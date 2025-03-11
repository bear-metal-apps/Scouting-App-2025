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