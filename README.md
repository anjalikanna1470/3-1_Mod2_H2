# Context Flow Lab

Context Flow Lab is a multi-turn AI chatbot Android application that maintains conversation context across multiple interactions.

## Features

- Multi-turn chatbot conversation
- Context-aware follow-up questions
- Session-based conversation memory
- Groq-powered AI responses
- Kotlin Android application
- XML-based UI
- Flask Python backend
- Retrofit API communication
- RecyclerView chat interface
- Clear conversation functionality

## Technologies Used

- Kotlin
- XML
- Android Studio
- Python
- Flask
- Groq API
- Retrofit
- Gson
- Kotlin Coroutines

## Project Structure

```text
Context Flow Lab/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com.example.a3_1mod2h2/
│           │       ├── MainActivity.kt
│           │       ├── ApiClient.kt
│           │       ├── ChatApi.kt
│           │       ├── ChatAdapter.kt
│           │       └── ChatModels.kt
│           │
│           └── res/
│               ├── layout/
│               ├── drawable/
│               ├── mipmap/
│               └── values/
│
├── backend/
│   ├── app.py
│   ├── run.py
│   ├── config.py
│   ├── groq_service.py
│   ├── memory.py
│   └── requirements.txt
│
├── gradle/
├── .gitignore
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
└── gradlew.bat
Context Flow

The chatbot maintains conversation history using a unique session ID.

Example:

User: My name is Anjali

AI: Nice to meet you, Anjali! How can I help you today?

User: What is my name?

AI: Your name is Anjali.

The previous messages are stored in the backend memory and provided to the AI when processing follow-up questions.

Backend Setup

Open the backend folder in a terminal.

Install the required packages:

pip install -r requirements.txt

Create a .env file inside the backend folder:

GROQ_API_KEY=your_groq_api_key

Start the Flask server:

python run.py

The backend runs on:

http://127.0.0.1:5000

For the Android Emulator, the application connects to:

http://10.0.2.2:5000/
Android Setup
Open the project in Android Studio.
Sync the Gradle files.
Start the Flask backend.
Start an Android Emulator.
Run the Android application.
Send a message and ask a follow-up question to test context retention.
API Endpoints
Chat
POST /chat

Request:

{
  "session_id": "unique-session-id",
  "message": "My name is Anjali"
}
Clear Conversation
POST /clear

Request:

{
  "session_id": "unique-session-id"
}
Security

The Groq API key is stored locally and is not included in the GitHub repository.

The .env and local.properties files are excluded using .gitignore.

Author

KANNA ANJALI
