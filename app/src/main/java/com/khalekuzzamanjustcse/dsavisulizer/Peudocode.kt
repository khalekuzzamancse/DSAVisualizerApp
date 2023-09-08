import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

@Composable
fun PseudocodeText(
    modifier: Modifier = Modifier
) {
    val lines by remember {
        mutableStateOf(
            listOf(
                PseudocodeLine(1, "procedure selectionSort(arr)"),
                PseudocodeLine(2, "n = length of arr"),
                PseudocodeLine(3, ""),
                PseudocodeLine(4, "for i from 0 to n - 1"),
                PseudocodeLine(5, "minIndex = i"),
                PseudocodeLine(6, "for j from i + 1 to n - 1"),
                PseudocodeLine(7, "if arr[j] < arr[minIndex]"),
                PseudocodeLine(8, "minIndex = j"),
                PseudocodeLine(9, ""),
                PseudocodeLine(10, "swap(arr[i], arr[minIndex])"),
                PseudocodeLine(11, "end for"),
                PseudocodeLine(12, ""),
                PseudocodeLine(13, "end procedure")
            )
        )
    }

    var cnt by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(Unit) {
        while (true) {
            lines[cnt].textColor.value = Color.Red
            delay(1000)
            cnt++
            cnt %= lines.size
            lines[if (cnt == 0) 0 else cnt - 1].textColor.value = Color.Unspecified
        }
    }

    ElevatedCard(
        shape = RectangleShape,
        modifier = Modifier
            .border(width = 1.dp, Color.Black)
            .padding(16.dp)

    ) {
        val maxLineNumber = lines.lastOrNull()?.lineNumber ?: 1
        val numberOfDigits = maxLineNumber.toString().length
        for (line in lines) {
            Row {
                Text(
                    text = "${line.lineNumber.toString().padStart(numberOfDigits, '0')}: ",
                    modifier = Modifier.width(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = line.text,
                    color = line.textColor.value
                )
            }
        }

    }


}

data class PseudocodeLine(
    val lineNumber: Int,
    var text: String,
    val textColor: MutableState<Color> = mutableStateOf(Color.Unspecified)
)
