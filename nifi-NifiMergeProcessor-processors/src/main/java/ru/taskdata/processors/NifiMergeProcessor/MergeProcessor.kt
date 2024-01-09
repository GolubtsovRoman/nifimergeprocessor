package ru.taskdata.processors.NifiMergeProcessor

import org.apache.commons.io.IOUtils
import org.apache.nifi.annotation.behavior.ReadsAttribute
import org.apache.nifi.annotation.behavior.ReadsAttributes
import org.apache.nifi.annotation.behavior.WritesAttribute
import org.apache.nifi.annotation.behavior.WritesAttributes
import org.apache.nifi.annotation.documentation.CapabilityDescription
import org.apache.nifi.annotation.documentation.SeeAlso
import org.apache.nifi.annotation.documentation.Tags
import org.apache.nifi.annotation.lifecycle.OnScheduled
import org.apache.nifi.processor.*
import org.apache.nifi.processor.exception.ProcessException
import ru.taskdata.processors.NifiMergeProcessor.service.IpevMergeService
import ru.taskdata.processors.NifiMergeProcessor.service.IpevMergeServiceImpl
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*


@Tags("NiFi Merge Processor")
@CapabilityDescription("NiFi Merge Processor")
@SeeAlso
@ReadsAttributes(ReadsAttribute(attribute = "", description = ""))
@WritesAttributes(WritesAttribute(attribute = "", description = ""))
class MergeProcessor : AbstractProcessor() {

    companion object {
        // out branches
        private val REL_SUCCESS = Relationship.Builder()
            .name("success")
            .description("If everything is ok")
            .build()
        private val REL_FAILURE = Relationship.Builder()
            .name("failure")
            .description("If something is wrong")
            .build()

        private const val ERROR_ATTRIBUTE = "error"
    }


    private var relationships: Set<Relationship>? = null
    override fun getRelationships(): Set<Relationship> {
        return relationships!!
    }

    private val mergeService: IpevMergeService = IpevMergeServiceImpl()


    override fun init(context: ProcessorInitializationContext) {
        val relationships: MutableSet<Relationship> = HashSet()
        relationships.add(REL_SUCCESS)
        relationships.add(REL_FAILURE)
        this.relationships = Collections.unmodifiableSet(relationships)
    }

    @OnScheduled
    fun onScheduled(context: ProcessContext?) { }

    @Throws(ProcessException::class)
    override fun onTrigger(context: ProcessContext, session: ProcessSession) {
        val flowFile = session.get() ?: return

        var resultContent = ""
        try {
            session.read(flowFile) { inputStream: InputStream ->
                val content = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
                if (content == null || content.isEmpty()) {
                    session.putAttribute(flowFile, ERROR_ATTRIBUTE, "Контент FlowFile пустой или == null")
                    session.transfer(flowFile, REL_FAILURE)
                } else {
                    resultContent = merge(content)
                }
            }

            session.write(flowFile).use { flowFileOutputStream ->
                flowFileOutputStream.write(
                    resultContent.toByteArray(StandardCharsets.UTF_8)
                )
            }
            session.transfer(flowFile, REL_SUCCESS)
        } catch (exp: Exception) {
            session.putAttribute(flowFile, ERROR_ATTRIBUTE,
                "Возникла критическая ошибка при попытке чтения или записи сессии. StackTrace: [$exp]")
            session.transfer(flowFile, REL_FAILURE)
        }
    }

    private fun merge(content: String): String {
        return mergeService.merge(content)
    }

}
