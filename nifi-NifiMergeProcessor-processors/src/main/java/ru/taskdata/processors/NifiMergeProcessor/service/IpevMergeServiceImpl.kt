package ru.taskdata.processors.NifiMergeProcessor.service

import ru.taskdata.processors.NifiMergeProcessor.service.jxml.JXmlMarshaller
import ru.taskdata.processors.NifiMergeProcessor.service.jxml.JXmlPersonalDataUnmarshaller
import ru.taskdata.processors.NifiMergeProcessor.service.stax.StaxMerger
import kotlin.text.Charsets.UTF_8


class IpevMergeServiceImpl : IpevMergeService {

    companion object {
        private const val REGISTRY_RECORD = "registry_record"
        private const val META_XML = "<?xml version='1.0' encoding='UTF-8'?>"

        private val marshallerPersonalData = JXmlPersonalDataUnmarshaller( JXmlMarshaller() )
    }


    override fun merge(content: String): String {
        val contentSB = StringBuilder(content)

        val registryRecordXml = contentSB.cutSliceXmlRegistryRecord()
        val data = contentSB.cutSliceXmlPersonalData()

        val merger = StaxMerger(
            content = registryRecordXml.toByteArray(UTF_8),
            personalData = marshallerPersonalData.unmarshallStringToPersonalData(data)
        )
        val registryRecordXmlResult = merger.insertPersData().toString(UTF_8)

        return registryRecordXmlResult.replace(META_XML, "")
    }


    private fun StringBuilder.cutSliceXmlRegistryRecord(): String {
        val range = this.rangeIndexRegistryRecord()
        return this.substring(range.first, range.last)
    }

    private fun StringBuilder.cutSliceXmlPersonalData(): String {
        val range = this.rangeIndexRegistryRecord()
        return this.removeRange(range.first, range.last).toString()
    }

    private fun StringBuilder.rangeIndexRegistryRecord(): IntRange {
        val startTag = "<$REGISTRY_RECORD>"
        val endTag = "</$REGISTRY_RECORD>"

        val startIndex = this.indexOf(startTag, ignoreCase = true)
        val endIndex = this.indexOf(endTag, ignoreCase = true)

        return IntRange(start = startIndex, endInclusive = endIndex + endTag.length)
    }

}
