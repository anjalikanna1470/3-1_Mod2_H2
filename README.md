# Context Flow Lab

A multi-turn AI chatbot application that maintains conversation context across multiple interactions.

## Features

- Multi-turn chatbot conversation
- Context-aware follow-up questions
- Session-based conversation memory
- Kotlin Android application
- XML-based UI
- Flask Python backend
- Groq API integration
- Retrofit API communication
- RecyclerView chat interface
- Clear conversation support

## Tech Stack

### Android

- Kotlin
- XML
- Android SDK
- RecyclerView
- Retrofit
- Gson
- Kotlin Coroutines

### Backend

- Python
- Flask
- Flask-CORS
- Groq API

## Architecture

```text
Android App
     |
     | Retrofit HTTP Request
     v
Flask Backend
     |
     v
Conversation Memory
     |
     v
Groq API
     |
     v
AI Response
     |
     v
Android Chat UI
