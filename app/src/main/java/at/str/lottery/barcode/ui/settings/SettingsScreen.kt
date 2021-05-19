package at.str.lottery.barcode.ui.settings

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

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(12.dp),
    ) {
        Text("Lottery Barcode Scanner", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))
        when (viewState.kioskConfig) {
            null -> {
                HeaderDescription(header = "Server URL", description = "Not set")
                HeaderDescription(header = "Kiosk ID", description = "Not set")
            }
            else -> {
                HeaderDescription(
                    header = "Server URL",
                    description = viewState.kioskConfig?.serverUrl.toString()
                )
                HeaderDescription(
                    header = "Kiosk ID",
                    description = viewState.kioskConfig?.kioskId ?: "Not set"
                )
            }
        }
    }
}

@Preview
@Composable
fun HeaderDescriptionPreview() {
    HeaderDescription(header = "Server URL", description = "https://example.com")
}

@Composable
fun HeaderDescription(header: String, description: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Column() {
            Text(header, style = MaterialTheme.typography.h6)
            Text(description, modifier = Modifier.height(40.dp))
        }
    }
}
