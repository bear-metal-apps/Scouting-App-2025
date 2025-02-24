package pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import nodes.RootNode
import androidx.compose.runtime.MutableState
import com.bumble.appyx.components.backstack.BackStack

@Composable
@Suppress("VARIABLE_WITH_REDUNDANT_INITIALIZER")
expect fun PitsScoutMenu(
    backStack: BackStack<RootNode.NavTarget>,
    pitsPerson: MutableState<String>,
    scoutName: MutableState<String>,
    numOfPitsPeople: MutableIntState,
)