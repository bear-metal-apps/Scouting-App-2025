package nodes

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.BackStackModel
import com.bumble.appyx.components.backstack.ui.fader.BackStackFader
import com.bumble.appyx.navigation.composable.AppyxComponent
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.ParentNode
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import org.tahomarobotics.scouting.Client
import pages.MainMenu
import java.lang.Integer.parseInt


class RootNode(
    buildContext: BuildContext,
    private val backStack: BackStack<NavTarget> = BackStack(
        model = BackStackModel(
            initialTarget = NavTarget.LoginPage,
            savedStateMap = buildContext.savedStateMap
        ),
        visualisation = { BackStackFader(it) }
    )
) : ParentNode<RootNode.NavTarget>(
    appyxComponent = backStack,
    buildContext = buildContext
) {
    private var team = mutableIntStateOf(1)
    private var robotStartPosition = mutableIntStateOf(0)
    private var pitsPerson = mutableStateOf("P1")
    private var pageIndex = mutableIntStateOf(0)
    var comp = mutableStateOf("")

    sealed class NavTarget : Parcelable {
        @Parcelize
        data object MainMenu : NavTarget()

        @Parcelize
        data object MatchScouting : NavTarget()

        @Parcelize
        data object PitsScouting : NavTarget()

        @Parcelize
        data object StratScreen : NavTarget()

        @Parcelize
        data object LoginPage : NavTarget()

        @Parcelize
        data object Settings : NavTarget()
    }

    override fun resolve(interactionTarget: NavTarget, buildContext: BuildContext): Node =
        when (interactionTarget) {
            NavTarget.LoginPage -> LoginNode(buildContext, backStack, comp, robotStartPosition)
            NavTarget.MainMenu -> MainMenu(buildContext, backStack, robotStartPosition, scoutName, comp, team)
            NavTarget.MatchScouting -> AutoTeleSelectorNode(buildContext, robotStartPosition, team, backStack)
            NavTarget.PitsScouting -> PitsNode(buildContext, backStack, pitsPerson, numOfPitsPeople)
            NavTarget.StratScreen -> StratNode(buildContext, backStack, scoutName, comp)
            NavTarget.Settings -> SettingsNode(buildContext, backStack)
        }

    @Composable
    override fun View(modifier: Modifier) {
        Column {
            AppyxComponent(
                appyxComponent = backStack,
                modifier = Modifier.weight(0.9f)
            )

        }
    }
}

var numOfPitsPeople = mutableIntStateOf(1234567890)
var scoutName = mutableStateOf("")
val teamDataArray: HashMap<TeamMatchStartKey, String> = hashMapOf<TeamMatchStartKey, String>()
var client: Client? = null


/**
 * @return int 0 if string has 0 ints and the first 10 digits of an int
 * @author The coolest GPT lead with red hair during the 2024-2025 season
 * *IMPORTANT* - when using it does NOT remove "/n" so you should set it to be a single line
 */
fun String.betterParseInt(): Int {
    var stringBuilder = StringBuilder()
    for (c in this) {
        try {
            parseInt(c.toString())
            stringBuilder.append(parseInt(c.toString()))
        } catch (e: NumberFormatException) {
            //Remove from original string
        }
    }
    if (stringBuilder.length >= 10) {
        stringBuilder.deleteCharAt(stringBuilder.lastIndex)
    }
    if (stringBuilder.toString().isNotEmpty()) {
        return parseInt(stringBuilder.toString())
    } else {
        return 0
    }
}