package com.nandkishor.guesstheno

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nandkishor.guesstheno.ui.theme.GuessTheNoTheme
import kotlinx.serialization.Serializable
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuessTheNoTheme{
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) { AppNavigation() }
            }
        }
    }
}

@Serializable
object RulesScreen

@Serializable
data class GameScreen(
    val chancesLeft: Int
)

@Serializable
data class GameOverPopup(
    val isWinner: Boolean
)

@Composable
fun AppNavigation() {
    // Create a NavController
    val navController = rememberNavController()

    // Setting up the NavHost to handle navigation
    NavHost(
        navController = navController,
        startDestination = RulesScreen
    ) {
        composable<RulesScreen> {
            RulesScreen(navController)
        }
        composable<GameScreen> {
            val args = it.toRoute<GameScreen>()
            GameScreen(chancesLeft = args.chancesLeft, navController = navController)
        }
        composable<GameOverPopup> {
            val args = it.toRoute<GameOverPopup>()
            GameOverPopup(isWinner = args.isWinner, navController = navController)
        }
    }
}

@Composable
fun RulesScreen(navController: NavController) {
    var chances by remember { mutableIntStateOf(3) }
    var chancesInput by remember { mutableStateOf(chances.toString()) }

    // Create a background color and padding to the entire screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Header with a fun icon or image (this could be a logo or image for your game)
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with your own image resource
                contentDescription = "Game Logo",
                modifier = Modifier.size(100.dp),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
            )


            // Game Title with bigger and bolder text
            Text(
                text = "Guess the Number Game",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            // Game Rules description with increased line spacing and font size
            Text(
                text = "Welcome to the Guess the Number Game! The rules are simple:\n\n" +
                        "1. Guess a number between 1 and 100.\n\n" +
                        "2. You can choose how many chances you want to guess.\n\n" +
                        "3. Once you guess correctly or run out of chances, the game ends.",
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 32.dp),
                lineHeight = 20.sp // Increases line spacing for better readability
            )

            // Input for number of chances
            Column{
                OutlinedTextField(
                    value = chancesInput,
                    onValueChange = {
                        chancesInput = it
                        chances = it.toIntOrNull() ?: 0 // Convert input to an integer or set to 0 if invalid
                    },
                    label = { Text("Enter Number of Chances") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { defaultKeyboardAction(ImeAction.Done) }),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )

                // Buttons to increase number of chances
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { chances += 3; chancesInput = chances.toString() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("+3", color = MaterialTheme.colorScheme.onSecondary) }
                    Button(
                        onClick = { chances += 5; chancesInput = chances.toString() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("+5", color = MaterialTheme.colorScheme.onSecondary) }
                    Button(
                        onClick = { chances += 10; chancesInput = chances.toString() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("+10", color = MaterialTheme.colorScheme.onSecondary) }
                    Button(
                        onClick = { chances += 15; chancesInput = chances.toString() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("+15", color = MaterialTheme.colorScheme.onSecondary) }
                }

                // Elevated Button to start the game
                Button(
                    onClick = {
                        // Navigate to the Game screen and pass chances as an argument
                        navController.navigate(GameScreen(chancesLeft = chances))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Start", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Composable
fun GameScreen(chancesLeft: Int, navController: NavController) {
    var randomNumber by remember { mutableIntStateOf(Random.nextInt(1, 101)) }
    var chances by remember { mutableIntStateOf(chancesLeft) }
    val context = LocalContext.current

    // Plain background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        // Exit button to return to Rules Screen
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(text = "Exit",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error
                ) )
        }
        Column(
            verticalArrangement = Arrangement.Center, // Center all content vertically
            horizontalAlignment = Alignment.CenterHorizontally, // Center all content horizontally
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with your own image resource
                contentDescription = "Game Logo",
                modifier = Modifier.size(100.dp),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Title Text
            Text(
                text = "Guess the Number",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(bottom = 24.dp) // Add some space below title
            )

            // Display Remaining Chances
            Text(
                text = "Chances Left: $chances",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )


            // Giving hints
            var guess by remember { mutableStateOf("") }
            var highOrLow by remember { mutableStateOf("Make your 1st guess!!") }
            Text(
                text = highOrLow,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Guessing Input Field
            OutlinedTextField(
                value = guess,
                onValueChange = { guess = it },
                label = { Text("Enter your guess") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = {
                    if (guess.toIntOrNull() == randomNumber) {
                        navController.navigate(GameOverPopup(isWinner = true)) // Win scenario
                    } else if (chances - 1 == 0) {
                        navController.navigate(GameOverPopup(isWinner = false)) // Loss scenario
                    } else {
                        highOrLow = if (randomNumber.toString() < guess) "Your Guess is High" else "Your Guess is Low"
                        chances-- // Decrease chances and continue
                        guess = "" // Reset Guess
                        KeyboardActions { defaultKeyboardAction(ImeAction.Done) }
                    }
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp), // Add padding to the sides
                shape = MaterialTheme.shapes.medium,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp)) // Space between input and button

            // Check Guess Button
            Button(
                onClick = {
                    if (guess == "") {
                        Toast.makeText(context, "No number entered", Toast.LENGTH_SHORT ).show()
                    } else {
                        if (guess.toIntOrNull() == randomNumber) {
                            navController.navigate(GameOverPopup(isWinner = true)) // Win scenario
                        } else if (chances - 1 == 0) {
                            navController.navigate(GameOverPopup(isWinner = false)) // Loss scenario
                        } else {
                            highOrLow = if (randomNumber.toString() < guess) "Your Guess is High" else "Your Guess is Low"
                            chances-- // Decrease chances and continue
                            guess = ""
                            KeyboardActions { defaultKeyboardAction(ImeAction.Done) }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp), // Button height for better touch target
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Check Guess", fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun GameOverPopup(isWinner: Boolean, navController: NavController) {
    // Dark background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Text to display if player won or ren out of chances
            Text(
                text = if (isWinner) "You Won! 🎉" else "You Ran Out of Chances!!️",
                style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Play again button
            Button(
                onClick = {
                    navController.navigate(RulesScreen) {
                        // Pop up to "rules" screen to restart the game
                        popUpTo(RulesScreen) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5)),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Play Again", fontWeight = FontWeight.Bold)
            }
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun CompletePreview() {
//    AppNavigation()
//}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun FirstPrev() {
//    val navController = rememberNavController()
//    RulesScreen(navController)
//}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun SecondPrev() {
//    val navController = rememberNavController()
//    GameScreen(4, navController)
//}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun ThirdPrev() {
//    val navController = rememberNavController()
//    GameOverPopup(false, navController)
//}
