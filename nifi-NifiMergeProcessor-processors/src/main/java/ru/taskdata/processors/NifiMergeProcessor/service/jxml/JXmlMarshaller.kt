package ru.taskdata.processors.NifiMergeProcessor.service.jxml

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef


class JXmlMarshaller {
    val xmlMapper = buildJacksonObjectMapper()

    final fun toXml(obj:Any): String {
        return xmlMapper.writeValueAsString(obj)
    }


    final inline fun <reified T: Any> fromXml(xml:String): T {
        return xmlMapper.readValue(xml, jacksonTypeRef<T>())
    }

    private fun buildJacksonObjectMapper(): ObjectMapper {
        val mapper = XmlMapper()

        mapper.findAndRegisterModules();
        return mapper
    }
}