package com.example.desperatehousewivescharacters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.desperatehousewivescharacters.ui.theme.DesperateHousewivesCharactersTheme
import kotlinx.coroutines.delay

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DesperateHousewivesCharactersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showTitleScreen by remember { mutableStateOf(true) }

                    if (showTitleScreen) {
                        TitleScreen {
                            showTitleScreen = false
                        }
                    } else {
                        CharacterList()
                    }
                }
            }
        }
    }
}

// Title screen shown for 3 seconds
@Composable
fun TitleScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000) // Show title screen for 3 seconds
        onTimeout()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "DH",
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Desperate Housewives",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Character Guide",
                fontSize = 18.sp
            )
        }
    }
}

// Data model for a character
data class Character(val name: String, val description: String, val imageUrl: String)

// Main composable for displaying the character list
@Composable
fun CharacterList() {
    var searchQuery by remember { mutableStateOf("") }
    val characters = remember { createCharacterList() }
    var filteredCharacters by remember { mutableStateOf(characters) }
    var selectedCharacter by remember { mutableStateOf<Character?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { newQuery ->
                searchQuery = newQuery
                filteredCharacters = characters.filter {
                    it.name.contains(newQuery, ignoreCase = true) ||
                            it.description.contains(newQuery, ignoreCase = true)
                }
            },
            label = { Text("Search character") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        // RecyclerView to display the list of characters
        AndroidView(
            factory = { ctx ->
                RecyclerView(ctx).apply {
                    layoutManager = LinearLayoutManager(ctx)
                    adapter = CharacterAdapter(filteredCharacters) { character ->
                        selectedCharacter = character
                    }
                }
            },
            update = { recyclerView ->
                (recyclerView.adapter as? CharacterAdapter)?.updateList(filteredCharacters)
            },
            modifier = Modifier.weight(1f)
        )
    }

    // Popup dialog with detailed character information
    selectedCharacter?.let { character ->
        Dialog(onDismissRequest = { selectedCharacter = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(character.name, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(character.description, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { selectedCharacter = null }) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

// Adapter for RecyclerView
class CharacterAdapter(
    private var characters: List<Character>,
    private val onItemClick: (Character) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.characterImageView)
        val nameTextView: TextView = view.findViewById(R.id.characterNameTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.characterDescriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        // Load image using Coil library
        holder.imageView.load(character.imageUrl) {
            crossfade(true)
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.ic_menu_report_image)
        }
        holder.nameTextView.text = character.name
        holder.descriptionTextView.text = character.description
        holder.itemView.setOnClickListener { onItemClick(character) }
    }

    override fun getItemCount() = characters.size

    fun updateList(newList: List<Character>) {
        characters = newList
        notifyDataSetChanged()
    }
}

// Function to create the list of Desperate Housewives characters
fun createCharacterList(): List<Character> {
    return listOf(
        Character("Susan Mayer", "Clumsy and accident-prone artist, always looking for love and often finding herself in comical situations.", "https://upload.wikimedia.org/wikipedia/en/b/b4/Teri_Hatcher_as_Susan.jpg"),
        Character("Lynette Scavo", "Former career woman turned stay-at-home mom, known for her sarcasm and clever strategies in managing her chaotic family.", "https://i.pinimg.com/474x/b0/fc/18/b0fc18e6f781d8a0bf687834c304a649.jpg"),
        Character("Bree Van de Kamp", "Perfectionist homemaker with a penchant for cooking and etiquette, hiding personal struggles behind a facade of domestic bliss.", "https://i.pinimg.com/originals/68/c6/ba/68c6bab81d87333eeb213c3b68019816.jpg"),
        Character("Gabrielle Solis", "Ex-model adjusting to suburban life, known for her materialism and fiery temperament, often scheming to maintain her luxurious lifestyle.", "https://upload.wikimedia.org/wikipedia/en/thumb/3/36/Eva_Longoria_as_Gabrielle.jpg/220px-Eva_Longoria_as_Gabrielle.jpg"),
        Character("Edie Britt", "Seductive real estate agent and neighborhood vamp, often at odds with the other housewives due to her unapologetic pursuit of men and success.", "https://ew.com/thmb/x1xWRjPRzdPvwxh_8FV5ut9kukg=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/gettyimages-93416576-2000-bc99a1fb117a448095a9ae1fb08e5a34.jpg"),
        Character("Mary Alice Young", "The series' narrator who committed suicide in the pilot episode, her secrets continue to impact the lives of her friends and family.", "https://img5.allocine.fr/acmedia/medias/nmedia/18/77/47/74/19409473.jpg"),
        Character("Carlos Solis", "Gabrielle's husband, a driven businessman with a complicated past, often involved in illegal activities to maintain his family's wealthy lifestyle.", "https://upload.wikimedia.org/wikipedia/commons/8/80/Ricardo_Chavira_Cannes.jpg"),
        Character("Mike Delfino", "Mysterious plumber with a dark past, becomes involved with Susan while hiding secrets that could jeopardize their relationship.", "https://static.wikia.nocookie.net/desperate/images/3/34/Mikeghost.png/revision/latest?cb=20120523105039"),
        Character("Tom Scavo", "Lynette's husband, struggles with his career and often feels overshadowed by his wife's strong personality, leading to marital conflicts.", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMyKoyYfymGdrekJG2nlBVLAD8tqB59uhF33YpxrlkzL0SiMIRKXHxi6Jt893aoGg8K1Y&usqp=CAU"),
        Character("Paul Young", "Mary Alice's widower, initially portrayed as a grieving husband but later revealed to have a sinister side and involvement in dark secrets.", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-gZ8hkdvhILHf_B6q1-JjzmMU-hunbhsDnQ&s")
    ).also { Log.d(TAG, "Created character list with ${it.size} characters") }
}