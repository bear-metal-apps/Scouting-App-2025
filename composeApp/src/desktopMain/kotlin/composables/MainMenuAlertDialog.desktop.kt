package composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
actual fun MainMenuAlertDialog(active: MutableState<Boolean>, bob: () -> Unit) {
}

@Composable
actual fun MainMenuAlertDialog(
    active: MutableState<Boolean>,
    bob: () -> Unit,
    team: Int,
    robotStartPosition: Int
) {
}