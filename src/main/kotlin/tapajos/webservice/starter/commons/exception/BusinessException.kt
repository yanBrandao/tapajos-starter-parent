package tapajos.webservice.starter.commons.exception

import java.lang.IllegalStateException
import java.lang.RuntimeException

class BusinessException private constructor(
        val errorCode: ErrorCode,
        override val cause: Throwable? = null,
        val messages: Array<String> = arrayOf()
): RuntimeException(cause){

    private lateinit var log: () -> Unit
    private var logUsed = false

    fun executeAppLog(defaultLog: () -> Unit) {
        if (logUsed) throw IllegalStateException("You should not log an exception twice.")

        if(this::log.isInitialized) {
            logUsed = true
            log()
        } else {
            defaultLog
        }
    }

    constructor(key: String = ErrorCode.DEFAULT_ERROR.key, cause: Throwable? = null) :
            this(errorCode = ErrorCode(key), cause = cause)

    constructor(key: String = ErrorCode.DEFAULT_ERROR.key, cause: Throwable? = null, messages: Array<String>) :
            this(errorCode = ErrorCode(key), cause = cause, messages = messages)

    constructor(key: String = ErrorCode.DEFAULT_ERROR.key, applicationLog: () -> Unit) :
            this(errorCode = ErrorCode(key)) {
        this.log = applicationLog
    }

    constructor(key: String = ErrorCode.DEFAULT_ERROR.key, cause: Throwable? = null, applicationLog: () -> Unit) :
            this(errorCode = ErrorCode(key), cause = cause) {
        this.log = applicationLog
    }

}