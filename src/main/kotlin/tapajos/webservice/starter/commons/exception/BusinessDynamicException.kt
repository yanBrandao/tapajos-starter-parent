package tapajos.webservice.starter.commons.exception

import com.fasterxml.jackson.databind.ObjectMapper
import java.lang.Exception
import java.lang.RuntimeException

class BusinessDynamicException(
        val statusCode: Int,
        val response: Any
) :RuntimeException() {

    constructor(statusCode: Int, responseJsonObject: String):
            this(statusCode, try {
                ObjectMapper().readValue(responseJsonObject, Any::class.java)
            } catch (ex: Exception) {
                /** Exception when object is not a json. **/
                responseJsonObject
            })
}