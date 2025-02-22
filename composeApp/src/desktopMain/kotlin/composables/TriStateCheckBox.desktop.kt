package composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState

@Composable
actual fun TriStateCheckBox(
    label: String,
    ifChecked: MutableState<ToggleableState>,
    modifier: Modifier
) {
}

@Composable
actual fun CheckBox(
    label: String,
    ifChecked: MutableIntState,
    modifier: Modifier
) {
}