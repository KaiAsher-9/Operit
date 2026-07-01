package com.ai.assistance.operit.core.tools.system

import android.content.Context
import android.os.Build
import com.ai.assistance.operit.util.AppLogger
import androidx.annotation.RequiresApi
import com.ai.assistance.operit.terminal.CommandExecutionEvent
import com.ai.assistance.operit.terminal.SessionDirectoryEvent
import com.ai.assistance.operit.terminal.TerminalManager
import com.ai.assistance.operit.terminal.data.TerminalState
import com.ai.assistance.operit.terminal.data.TerminalSessionData
import com.ai.assistance.operit.terminal.provider.type.HiddenExecResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Terminal wrapper - proot process startup DISABLED
 * All method signatures preserved for compilation compatibility.
 * All method bodies are no-ops to prevent proot from being launched at runtime.
 */
@RequiresApi(Build.VERSION_CODES.O)
class Terminal private constructor(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: Terminal? = null

        fun getInstance(context: Context): Terminal {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Terminal(context.applicationContext).also { INSTANCE = it }
            }
        }

        private const val TAG = "Terminal"
    }

    // TerminalManager is NOT initialized - this prevents proot binary from ever being invoked
    private val scope = CoroutineScope(Dispatchers.Main)

    // Empty stubs matching original property types
    val commandEvents: SharedFlow<CommandExecutionEvent> = MutableSharedFlow()
    val directoryEvents: SharedFlow<SessionDirectoryEvent> = MutableSharedFlow()
    val terminalState: StateFlow<TerminalState> = MutableStateFlow(TerminalState())
    val sessions: Flow<List<TerminalSessionData>> = MutableStateFlow(emptyList())
    val currentSessionId: Flow<String?> = MutableStateFlow(null)
    val currentDirectory: Flow<String> = MutableStateFlow("/")
    val isInteractiveMode: Flow<Boolean> = MutableStateFlow(false)
    val interactivePrompt: Flow<String> = MutableStateFlow("")
    val isFullscreen: Flow<Boolean> = MutableStateFlow(false)

    /** Initialize - DISABLED, returns false immediately */
    suspend fun initialize(): Boolean {
        AppLogger.d(TAG, "Terminal initialization skipped - proot disabled")
        return false
    }

    /** Destroy - no-op */
    fun destroy() { }

    /** Create session - DISABLED, returns empty string */
    suspend fun createSession(title: String? = null): String {
        AppLogger.d(TAG, "createSession skipped - proot disabled")
        return ""
    }

    fun switchToSession(sessionId: String) { }

    fun closeSession(sessionId: String) { }

    /** Execute command - DISABLED, returns null */
    suspend fun executeCommand(sessionId: String, command: String): String? {
        return null
    }

    /** Execute hidden command - DISABLED, returns error result */
    suspend fun executeHiddenCommand(
        command: String,
        executorKey: String = "default",
        timeoutMs: Long = 120000L
    ): HiddenExecResult {
        return HiddenExecResult(
            output = "",
            exitCode = -1,
            state = HiddenExecResult.State.OK,
            error = "proot disabled"
        )
    }

    /** Execute command flow - DISABLED, returns empty flow */
    fun executeCommandFlow(sessionId: String, command: String): Flow<CommandExecutionEvent> {
        return emptyFlow()
    }

    fun sendInput(sessionId: String, input: String) { }

    fun sendInterruptSignal(sessionId: String) { }

    /** Always returns true to prevent business logic from blocking */
    fun isConnected(): Boolean {
        return true
    }
}
