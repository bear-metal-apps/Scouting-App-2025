package pages

import androidx.compose.runtime.Composable
import com.bumble.appyx.components.backstack.BackStack
import nodes.RootNode


@Composable
expect fun SettingsMenu(
    mainMenuBackStack: BackStack<RootNode.NavTarget>
)