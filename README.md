# ZenWord

Welcome to **ZenWord**, an Android-based word game developed as the final project for the Algorithms and Data Structures II course (2023-24). This project utilizes efficient data structures such as sets and mappings to deliver a responsive and optimized gaming experience.

## 📖 Description

**ZenWord** challenges players to discover hidden words formed from a randomly chosen target word. In each game session:
- **Dynamic Word Length:** The game uses a configurable parameter (`wordLength`) to set the maximum length of hidden words (ranging from 3 up to 7 letters). At least one hidden word of every possible length is generated whenever possible.
- **Word Generation:** A target word is selected randomly from a Catalan dictionary, and its letters are arranged in a circle to be used for forming new words.
- **Bonus System:** Valid words that are not among the hidden words earn bonus points. Accumulating five bonus points allows the player to request a hint that reveals the first letter of one of the still-hidden words.

## 🎮 How to Play

1. **Main Screen:**
   - The game starts with a Zen-themed background featuring a circular display of letters and a list of hidden words.
   - The top of the screen displays the number of words discovered and the total count of valid solutions available.

2. **Forming Words:**
   - Tap letters from the circle to build your word. Selected letters become disabled to prevent re-use during the same attempt.
   - Use the **Clear** button to reset the current word input if needed.

3. **Submitting a Word:**
   - Press the **Send** button to submit your word.
   - If the word matches one of the hidden words, it is revealed and added to your list of discovered words.
   - If the word is valid (i.e., it can be constructed from the available letters) but not one of the hidden words, you earn bonus points.
   - The game alerts you if you attempt to enter a duplicate or invalid word.

4. **Additional Controls:**
   - **Random:** Shuffles the letters in the circle to inspire new word combinations.
   - **Help:** Uses bonus points to reveal the first letter of a randomly chosen hidden word.
   - **Restart:** Starts a new game with a different target word and a new set of hidden words.

5. **End of Game:**
   - The game concludes when all hidden words have been discovered, displaying a congratulatory message.
   - Only the controls for starting a new game or viewing bonus details remain active.

## 🗂 Project Structure

- **Android Studio Project:**  
  The entire project is developed in Android Studio, with well-commented code to explain the implementation details.
  
- **Data Files:**  
  Contains the Catalan dictionary file (`paraules.dic`), which stores words in both accented and unaccented formats.
  
- **Core Modules:**
  - **Data Management:**  
    Utilizes sets and mappings to manage:
    - The catalog of valid words.
    - Groupings of words by length.
    - The pool of valid solutions and discovered words.
    - Letter frequency mapping for the selected target word.
  
- **UI Components:**  
  XML layout files and Java classes handle the game logic, user input, and dynamic updates to the interface.

## ✨ Key Features

- **Dynamic Difficulty:** Adjust the challenge by configuring the `wordLength` parameter.
- **Optimized Data Structures:** Efficient use of sets and mappings ensures high performance and low memory overhead.
- **Interactive Bonus System:** Earn bonus points from valid words to unlock helpful hints.
- **User-Friendly Design:** Enjoy a clean, Zen-themed interface with intuitive controls.
- **Randomized Gameplay:** Every new game session features a different target word and set of hidden words.

## 📢 Author

- **Gaizka Medina Gordo**
