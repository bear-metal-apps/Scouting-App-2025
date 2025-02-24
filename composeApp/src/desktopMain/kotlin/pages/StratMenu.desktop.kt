package pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.bumble.appyx.components.backstack.BackStack
import nodes.RootNode
import nodes.Team

@Composable
actual fun StratMenu(
    backStack: BackStack<RootNode.NavTarget>,
    scoutName: MutableState<String>,
    comp: MutableState<String>,
    teams: List<Team>,
    isRedAlliance: Boolean
) {
}