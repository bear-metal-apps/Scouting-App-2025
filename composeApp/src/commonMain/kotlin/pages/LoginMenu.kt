package pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import com.bumble.appyx.components.backstack.BackStack
import nodes.RootNode

@Composable
expect fun LoginMenu(
    backStack: BackStack<RootNode.NavTarget>,
    comp: MutableState<String>,
    robotStartPosition: MutableIntState
)