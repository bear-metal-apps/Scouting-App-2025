package composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
expect fun EnumerableValue(
    label: String,
    value: MutableIntState,
    flashColor: Color,
    alignment: Alignment,
    modifier: Modifier
)