package at.str.lottery.barcode.ui.settings

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.str.lottery.barcode.model.ScanTrackerViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun SettingsScreen(navController: NavController, scanTrackerViewModel: ScanTrackerViewModel) {
    val viewState by scanTrackerViewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(12.dp),
    ) {
        Text("Lottery Barcode Scanner", style = MaterialTheme.typography.h5)

        when (viewState.kioskConfig) {
            null -> {
                HeaderDescription(header = "Server URL", description = "Not set", onEdit = {})
                HeaderDescription(header = "Kiosk ID", description = "Not set", onEdit = {})
            }
            else -> {
                HeaderDescription(
                    header = "Server URL",
                    description = viewState.kioskConfig?.serverUrl.toString(),
                    onEdit = { scanTrackerViewModel.onUpdateServerUrl(Uri.parse(it)) }
                )
                HeaderDescription(
                    header = "Kiosk ID",
                    description = viewState.kioskConfig?.kioskId ?: "Not set",
                    onEdit = {}
                )
            }
        }
    }
}

@Preview
@Composable
fun HeaderDescriptionPreview() {
    HeaderDescription(header = "Server URL", description = "https://example.com", onEdit = {})
}

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
        Column() {
            Text(header, style = MaterialTheme.typography.h6)
            if (!editable) {
                //Text(text = desc, modifier = Modifier.preferredHeight(40.dp))
            } else {
//                textInput(desc, setDesc, {})
            }
        }
        if (!editable) {
            //Icon(Icons.Filled.Edit, modifier = Modifier.align(Alignment.CenterVertically))
        } else {
            //Icon(Icons.Filled.Close, modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

//@Composable
//fun textInput(text: String,
//              onTextChange: (String) -> Unit,
//              onImeAction: () -> Unit,
//              modifier: Modifier = Modifier
//) = TextField(
//        text,
//        onValueChange = onTextChange,
//        backgroundColor = Color.Transparent,
//        maxLines = 1,
//        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//        onImeActionPerformed = { action, softKeyboardController ->
//            if (action == ImeAction.Done) {
//                onImeAction()
//                softKeyboardController?.hideSoftwareKeyboard()
//            }
//        },
//        modifier = modifier
//    )