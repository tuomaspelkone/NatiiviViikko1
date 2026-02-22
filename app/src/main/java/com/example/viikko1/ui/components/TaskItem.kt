package com.example.viikko1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.viikko1.data.local.entity.Task
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,    // Kutsutaan kun checkbox muuttuu
    onDelete: () -> Unit     // Kutsutaan kun poistopainiketta painetaan
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox: merkitse tehtävä valmiiksi / keskeneräiseksi
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() }
            )

            // Tehtävän tiedot (otsikko + kuvaus + päivämäärä)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    // Yliviivaus jos tehtävä on valmis
                    textDecoration = if (task.isCompleted) {
                        TextDecoration.LineThrough
                    } else null
                )
                // Näytä kuvaus vain jos se ei ole tyhjä
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                // Muotoile aikaleima luettavaan muotoon
                Text(
                    text = "Luotu: ${formatDate(task.createdAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Poistopainike (roskakori-ikoni)
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Poista")
            }
        }
    }
}

// Apufunktio: muuntaa millisekunnit luettavaksi päivämääräksi
// esim. 1708123456789 → "17.02.2024 10:30"
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

