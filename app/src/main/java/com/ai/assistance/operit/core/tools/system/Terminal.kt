package com.ai.assistance.operit.core.tools.system

import android.content.Context
import android.os.Build
import com.ai.assistance.operit.util.AppLogger
import androidx.annotation.RequiresApi
import com.ai.assistance.operit.terminal.CommandExecutionEvent
import com.ai.assistance.operit.terminal.SessionDirectoryEvent
import com.ai.assistance.operit.terminal.TerminalManager
import com.ai.assistance.operit.terminal.data.TerminalState
import com.ai.assistance.operit.terminal.provider.type.HiddenExecResult
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

/**
 * 终端管理器 - 已禁用proot启动
 * 保留类签名和方法签名以确保编译通过，但所有方法体均为空操作
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

    // 不再初始化 TerminalManager，避免拉起 proot 进程
    // private val terminalManager = TerminalManager.getInstance(context)
    private val scope = CoroutineScope(Dispatchers.Main)

    // 使用空的 Flow/StateFlow 替代真实的 terminalManager 暴露
    val commandEvents: SharedFlow<CommandExecutionEvent> = MutableSharedFlow()
    val directoryEvents: SharedFlow<SessionDirectoryEvent> = MutableSharedFlow()
    val terminalState: StateFlow<TerminalState> = MutableStateFlow(TerminalState())
    val sessions = MutableStateFlow(emptyList<com.ai.assistance.operit.terminal.data.TerminalSession>())
    val currentSessionId = MutableStateFlow<String?>(null)
    val currentDirectory = MutableStateFlow("/")
    val isInteractiveMode = MutableStateFlow(false)
    val interactivePrompt = MutableStateFlow("")
    val isFullscreen = MutableStateFlow(false)

    /**
     * 初始化终端管理器 - 已禁用，直接返回false
     */
    suspend fun initialize(): Boolean {
        AppLogger.d(TAG, "Terminal initialization skipped - proot disabled")
        return false
    }

    /**
     * 销毁终端管理器 - 空操作
     */
    fun destroy() {
        AppLogger.d(TAG, "Terminal destroy called - no-op (proot disabled)")
    }

    /**
     * 创建新的终端会话 - 已禁用，返回空session ID
     */
    suspend fun createSession(title: String? = null): String {
        AppLogger.d(TAG, "Terminal createSession skipped - proot disabled")
        return ""
    }
    
    /**
     * 切换到指定会话 - 空操作
     */
    fun switchToSession(sessionId: String) { }

    /**
     * 关闭终端会话 - 空操作
     */
    fun closeSession(sessionId: String) { }

    /**
     * 执行命令 - 已禁用，直接返回null
     */
    suspend fun executeCommand(sessionId: String, command: String): String? {
        AppLogger.d(TAG, "Terminal executeCommand skipped - proot disabled")
        return null
    }

    suspend fun executeHiddenCommand(
        command: String,
        executorKey: String = "default",
        timeoutMs: Long = 120000L
    ): HiddenExecResult {
        AppLogger.d(TAG, "Terminal executeHiddenCommand skipped - proot disabled")
        return HiddenExecResult(exitCode = -1, stdout = "", stderr = "proot disabled")
    }

    /**
     * 执行命令 - Flow版本，返回空Flow
     */
    fun executeCommandFlow(sessionId: String, command: String): Flow<CommandExecutionEvent> {
        return emptyFlow()
    }
    
    /**
     * 发送输入 - 空操作
     */
    fun sendInput(sessionId: String, input: String) { }

    /**
     * 发送中断信号 - 空操作
     */
    fun sendInterruptSignal(sessionId: String) { }

    /**
     * 检查服务是否已连接 - 始终返回true以防止业务逻辑阻塞
     */
    fun isConnected(): Boolean {
        return true
    }
}
