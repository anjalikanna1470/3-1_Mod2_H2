from groq import Groq

from config import GROQ_API_KEY, MODEL_NAME


client = Groq(
    api_key=GROQ_API_KEY
)


SYSTEM_PROMPT = """
You are a helpful conversational AI assistant.

Your main task is to maintain context across multiple
messages in the same conversation.

Rules:

1. Remember information provided earlier in the conversation.
2. Use previous messages when answering follow-up questions.
3. If the user gives their name, remember it.
4. If the user asks something like "What is my name?",
   answer using the conversation history.
5. Keep responses clear and natural.
6. Do not say that you cannot remember previous messages
   when the conversation history is provided.
"""


def generate_response(history, user_message):

    messages = [
        {
            "role": "system",
            "content": SYSTEM_PROMPT
        }
    ]

    # Add previous conversation
    for item in history:

        role = item.get("role")
        content = item.get("content")

        if role in ["user", "assistant"] and content:
            messages.append({
                "role": role,
                "content": content
            })

    # Add current user message
    messages.append({
        "role": "user",
        "content": user_message
    })

    print("\nSending messages to Groq:")
    print(messages)

    response = client.chat.completions.create(
        model=MODEL_NAME,
        messages=messages,
        temperature=0.7,
        max_completion_tokens=500
    )

    reply = response.choices[0].message.content

    if not reply:
        raise RuntimeError(
            "Groq returned an empty response."
        )

    return reply.strip()