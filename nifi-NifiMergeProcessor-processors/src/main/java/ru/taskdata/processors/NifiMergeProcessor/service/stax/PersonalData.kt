package ru.taskdata.processors.NifiMergeProcessor.service.stax

import java.util.*
import javax.xml.bind.annotation.*


@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
data class PersonalData(
    @field:XmlElement(name = "hash_id")
    val hashId: String,

    @field:XmlElement(name = "element")
    @field:XmlElementWrapper(name = "elements")
    val records: MutableList<PersonalDataRecord>
) {
    constructor() : this("", ArrayList<PersonalDataRecord>())
}
