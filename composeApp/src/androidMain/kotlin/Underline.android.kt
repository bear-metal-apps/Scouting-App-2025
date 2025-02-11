import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.utils.multiplatform.Parcelable

@Composable
actual fun Underline(
    modifier: Modifier,
    end: Offset,
    thickness: Dp,
    color: Color
) = Canvas(modifier.fillMaxWidth().height(thickness)) {
    drawLine(
        color = color,
        strokeWidth = thickness.toPx(),
        start = Offset(0f, thickness.toPx() / 2),
        end = end,
    )
}