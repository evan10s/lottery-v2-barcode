package at.str.lottery.barcode.ui.settings

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.str.lottery.barcode.model.ScanTrackerViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.internal.http2.Header

@ExperimentalLayout
@ExperimentalCoroutinesApi
@Composable
fun SettingsScreen(navController: NavController, scanTrackerViewModel: ScanTrackerViewModel) {
    val viewState by scanTrackerViewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(12.dp),
    ) {
        Text("Lottery Barcode Scanner", style = MaterialTheme.typography.h5)

        when (viewState.kioskConfig) {
            null -> {
                HeaderDescription(header = "Server URL", description = "Not set")
                HeaderDescription(header = "Kiosk ID", description = "Not set")
            }
            else -> {
                HeaderDescription(
                    header = "Server URL",
                    description = viewState.kioskConfig?.serverUrl.toString(),
                    onEdit = { scanTrackerViewModel.onUpdateServerUrl(Uri.parse(it)) }
                )
                HeaderDescription(
                    header = "Kiosk ID",
                    description = viewState.kioskConfig?.kioskId ?: "Not set"
                )
            }
        }
    }
}

@ExperimentalLayout
@Preview
@Composable
fun HeaderDescriptionPreview() {
    HeaderDescription(header = "Server URL", description = "https://example.com")
}

@ExperimentalLayout
@Composable
fun HeaderDescription(header: String, description: String, onEdit: (String) -> Unit) {
    val (editable, setEdit) = remember { mutableStateOf(false) }

    // FIXME
    val (desc, setDesc) = remember { mutableStateOf(description)}

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clickable(onClick = {
                setEdit(!editable)
            })
            .padding(8.dp)
            .fillMaxWidth()

    ) {
        Column {
            Text(header, style = MaterialTheme.typography.h6)
            if (!editable) {
                Text(text = desc)
            } else {
                TextInput(desc, setDesc, {})
            }
        }
        if (!editable) {
            Icon(Icons.Filled.Edit, modifier = Modifier.align(Alignment.CenterVertically))
        } else {
            Icon(Icons.Filled.Close, modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Composable
fun TextInput(text: String,
                  onTextChange: (String) -> Unit,
                  onImeAction: () -> Unit,
                  modifier: Modifier = Modifier
) = TextField(
        value = text,
        onValueChange = onTextChange,
        backgroundColor = Color.Transparent,
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        onImeActionPerformed = { action, softKeyboardController ->
            if (action == ImeAction.Done) {
                onImeAction()
                softKeyboardController?.hideSoftwareKeyboard()
            }
        },
        modifier = modifier
    )