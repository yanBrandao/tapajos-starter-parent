package tapajos.webservice.starter.commons.exception

import java.lang.RuntimeException
import kotlin.reflect.KClass

class NotFoundException(
        private val resource: String,
        private val value: Any
) :RuntimeException() {

    override val message: String
        get() = "Not found resource [${resource}] with value [${value}]"

    constructor(resource: KClass<*>, value: Any): this(resource.simpleName!!, value)
}