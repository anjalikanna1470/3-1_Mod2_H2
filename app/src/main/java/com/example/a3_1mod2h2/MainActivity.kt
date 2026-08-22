package com.example.a3_1mod2h2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a3_1mod2h2.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: ChatAdapter

    private val messages = mutableListOf<ChatMessage>()

    /*
     * One session ID is created when the Activity starts.
     *
     * Every message sent during this conversation
     * uses the same session ID.
     */
    private val sessionId: String by lazy {
        UUID.randomUUID().toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupRecyclerView()

        setupSendButton()

        showWelcomeMessage()

        Log.d(
            "CONTEXT_FLOW",
            "Session ID: $sessionId"
        )
    }

    private fun setupRecyclerView() {

        adapter = ChatAdapter(messages)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this)

        binding.recyclerView.adapter = adapter
    }

    private fun setupSendButton() {

        binding.btnSend.setOnClickListener {

            val message = binding.etMessage
                .text
                .toString()
                .trim()

            if (message.isEmpty()) {
                return@setOnClickListener
            }

            sendMessage(message)
        }
    }

    private fun showWelcomeMessage() {

        adapter.addMessage(
            ChatMessage(
                message = "Hello! I'm Context Flow Lab. Ask me something and then ask a follow-up question to test conversation context.",
                isUser = false
            )
        )
    }

    private fun sendMessage(message: String) {

        binding.etMessage.text.clear()

        /*
         * Immediately display the user's message.
         */
        adapter.addMessage(
            ChatMessage(
                message = message,
                isUser = true
            )
        )

        scrollToBottom()

        binding.btnSend.isEnabled = false

        lifecycleScope.launch {

            try {

                Log.d(
                    "CONTEXT_FLOW",
                    "Sending message: $message"
                )

                Log.d(
                    "CONTEXT_FLOW",
                    "Session ID: $sessionId"
                )

                val response = ApiClient.api.sendMessage(
                    ChatRequest(
                        session_id = sessionId,
                        message = message
                    )
                )

                Log.d(
                    "CONTEXT_FLOW",
                    "HTTP Code: ${response.code()}"
                )

                Log.d(
                    "CONTEXT_FLOW",
                    "Response successful: ${response.isSuccessful}"
                )

                if (response.isSuccessful) {

                    val body = response.body()

                    Log.d(
                        "CONTEXT_FLOW",
                        "Response body: $body"
                    )

                    if (body != null) {

                        if (!body.reply.isNullOrBlank()) {

                            /*
                             * Successful AI response.
                             */
                            adapter.addMessage(
                                ChatMessage(
                                    message = body.reply,
                                    isUser = false
                                )
                            )

                        } else if (!body.error.isNullOrBlank()) {

                            /*
                             * Flask returned an error message.
                             */
                            adapter.addMessage(
                                ChatMessage(
                                    message = body.error,
                                    isUser = false,
                                    isError = true
                                )
                            )

                        } else {

                            adapter.addMessage(
                                ChatMessage(
                                    message = "The server returned an empty response.",
                                    isUser = false,
                                    isError = true
                                )
                            )
                        }

                    } else {

                        adapter.addMessage(
                            ChatMessage(
                                message = "The server returned no response body.",
                                isUser = false,
                                isError = true
                            )
                        )
                    }

                } else {

                    /*
                     * HTTP error such as 400, 404, 500, etc.
                     */
                    val errorBody = response.errorBody()
                        ?.string()

                    Log.e(
                        "CONTEXT_FLOW",
                        "HTTP Error ${response.code()}: $errorBody"
                    )

                    adapter.addMessage(
                        ChatMessage(
                            message = "Server error ${response.code()}.\n$errorBody",
                            isUser = false,
                            isError = true
                        )
                    )
                }

            } catch (exception: Exception) {

                Log.e(
                    "CONTEXT_FLOW",
                    "Connection/Parsing error",
                    exception
                )

                adapter.addMessage(
                    ChatMessage(
                        message = "Connection error:\n${exception.message}",
                        isUser = false,
                        isError = true
                    )
                )

                Toast.makeText(
                    this@MainActivity,
                    "Unable to connect to Flask server",
                    Toast.LENGTH_SHORT
                ).show()

            } finally {

                binding.btnSend.isEnabled = true

                scrollToBottom()
            }
        }
    }

    private fun scrollToBottom() {

        if (adapter.itemCount > 0) {

            binding.recyclerView.scrollToPosition(
                adapter.itemCount - 1
            )
        }
    }
}