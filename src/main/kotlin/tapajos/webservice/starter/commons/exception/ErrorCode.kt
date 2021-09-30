package tapajos.webservice.starter.commons.exception

class ErrorCode(val key: String) {

    companion object {
        val DEFAULT_ERROR = ErrorCode("default.business.error")
    }

    init {
        require(key.isNotEmpty()) { "key can't be null or empty"}
    }

    enum class Labels(val suffix: String) {
        CODE("code"),
        MESSAGE("message"),
        TITLE("title"),
        TYPE("type")
    }
}