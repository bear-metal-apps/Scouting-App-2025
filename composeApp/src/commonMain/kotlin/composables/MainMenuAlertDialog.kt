package composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
expect fun MainMenuAlertDialog(active: MutableState<Boolean>, bob: () -> Unit)