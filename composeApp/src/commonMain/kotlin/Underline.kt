import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.utils.multiplatform.Parcelable

@Composable
expect fun Underline(
    modifier: Modifier,
    end: Offset,
    thickness: Dp,
    color: Color
)