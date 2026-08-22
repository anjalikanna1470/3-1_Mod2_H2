from flask import Flask, request, jsonify
from flask_cors import CORS
import traceback

from groq_service import generate_response
from memory import get_history, add_message, clear_history


# ============================================================
# FLASK APPLICATION
# ============================================================

app = Flask(__name__)

# Allow Android app to communicate with Flask
CORS(app)


# ============================================================
# HOME / HEALTH CHECK
# ============================================================

@app.route("/", methods=["GET"])
def home():

    return jsonify({
        "success": True,
        "status": "running",
        "message": "Context Flow Chatbot Backend"
    }), 200


# ============================================================
# CHAT API
# ============================================================

@app.route("/chat", methods=["POST"])
def chat():

    try:

        # ----------------------------------------------------
        # 1. Read JSON request
        # ----------------------------------------------------

        data = request.get_json(silent=True)

        print("\n========== CHAT REQUEST ==========")
        print("Received:", data)

        if not data:

            return jsonify({
                "success": False,
                "session_id": None,
                "reply": None,
                "error": "Request body is empty or invalid JSON"
            }), 400


        # ----------------------------------------------------
        # 2. Get message
        # ----------------------------------------------------

        message = data.get("message", "")

        if message is None:
            message = ""

        message = str(message).strip()


        if not message:

            return jsonify({
                "success": False,
                "session_id": data.get("session_id"),
                "reply": None,
                "error": "Message is required"
            }), 400


        # ----------------------------------------------------
        # 3. Get session ID
        # ----------------------------------------------------

        session_id = data.get(
            "session_id",
            "default"
        )

        if session_id is None:
            session_id = "default"

        session_id = str(session_id).strip()


        if not session_id:

            session_id = "default"


        print("Session ID:", session_id)
        print("Message:", message)


        # ----------------------------------------------------
        # 4. Get previous conversation history
        # ----------------------------------------------------

        history = get_history(session_id)

        print("Previous history:", history)


        # ----------------------------------------------------
        # 5. Add user's new message to memory
        # ----------------------------------------------------

        add_message(
            session_id,
            "user",
            message
        )


        # ----------------------------------------------------
        # 6. Generate response using Groq
        # ----------------------------------------------------

        print("\nSending messages to Groq:")

        reply = generate_response(
            history=history,
            user_message=message
        )


        print("Groq reply:", reply)


        # ----------------------------------------------------
        # 7. Validate Groq response
        # ----------------------------------------------------

        if reply is None:

            reply = "I couldn't generate a response."


        reply = str(reply).strip()


        # ----------------------------------------------------
        # 8. Save assistant response to memory
        # ----------------------------------------------------

        add_message(
            session_id,
            "assistant",
            reply
        )


        print("=================================\n")


        # ----------------------------------------------------
        # 9. Send successful JSON response to Android
        # ----------------------------------------------------

        response_data = {
            "success": True,
            "session_id": session_id,
            "reply": reply,
            "error": None
        }


        print("Response sent to Android:")
        print(response_data)


        return jsonify(response_data), 200


    # ========================================================
    # ERROR HANDLING
    # ========================================================

    except Exception as e:

        print("\n========== CHAT ERROR ==========")

        print("ERROR:", str(e))

        traceback.print_exc()

        print("================================\n")


        session_id = None

        try:

            if data:
                session_id = data.get(
                    "session_id",
                    "default"
                )

        except Exception:

            session_id = "default"


        # ----------------------------------------------------
        # Return proper JSON error to Android
        # ----------------------------------------------------

        error_response = {
            "success": False,
            "session_id": session_id,
            "reply": None,
            "error": str(e)
        }


        print("Error response sent to Android:")
        print(error_response)


        return jsonify(error_response), 500


# ============================================================
# CLEAR CONVERSATION
# ============================================================

@app.route("/clear", methods=["POST"])
def clear():

    try:

        data = request.get_json(
            silent=True
        ) or {}


        session_id = data.get(
            "session_id",
            "default"
        )


        if session_id is None:
            session_id = "default"


        session_id = str(session_id).strip()


        if not session_id:
            session_id = "default"


        print("\n========== CLEAR CHAT ==========")

        print("Session ID:", session_id)


        # Clear memory

        clear_history(session_id)


        print("Conversation cleared")

        print("================================\n")


        return jsonify({

            "success": True,

            "status": "success",

            "message": "Conversation cleared",

            "session_id": session_id,

            "reply": None,

            "error": None

        }), 200


    except Exception as e:

        print("\n========== CLEAR ERROR ==========")

        traceback.print_exc()

        print("================================\n")


        return jsonify({

            "success": False,

            "status": "error",

            "message": None,

            "session_id": None,

            "reply": None,

            "error": str(e)

        }), 500


# ============================================================
# RUN FLASK SERVER
# ============================================================

if __name__ == "__main__":

    print("\n==========================================")
    print("     CONTEXT FLOW LAB BACKEND")
    print("==========================================")
    print("Server: http://127.0.0.1:5000")
    print("Android Emulator: http://10.0.2.2:5000")
    print("==========================================\n")


    app.run(

        host="0.0.0.0",

        port=5000,

        debug=True

    )