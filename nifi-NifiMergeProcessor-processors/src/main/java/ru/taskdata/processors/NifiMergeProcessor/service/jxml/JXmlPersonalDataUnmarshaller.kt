package ru.taskdata.processors.NifiMergeProcessor.service.jxml

import ru.taskdata.processors.NifiMergeProcessor.service.stax.PersonalData
import ru.taskdata.processors.NifiMergeProcessor.service.stax.PersonalDataRecord


class JXmlPersonalDataUnmarshaller (
        private val jXmlMarshaller : JXmlMarshaller
) {

    fun unmarshallStringToPersonalData(xml: String): PersonalData {
        val pdJxml: PersonalDataJXml =  jXmlMarshaller.fromXml(xml)
        return convertToPersonalData(pdJxml)
    }


    private fun convertToPersonalData(ps: PersonalDataJXml): PersonalData {
        var records = mutableListOf<PersonalDataRecord>()
        if (ps.records!= null ) {
            records = ps.records.map { convertToPersonalDataRecord(it) }.toMutableList()
        }

        return PersonalData(
                hashId = ps.hashId,
                records = records
        )
    }

    private fun convertToPersonalDataRecord(ps: PersonalDataRecordJXml) = PersonalDataRecord(
        xpath = ps.xpath,
        id = ps.id,
        parentId = ps.parentId,
        value = ps.value
    )


}