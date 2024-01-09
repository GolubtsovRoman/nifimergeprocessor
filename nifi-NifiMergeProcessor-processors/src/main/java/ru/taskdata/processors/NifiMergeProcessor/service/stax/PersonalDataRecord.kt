package ru.taskdata.processors.NifiMergeProcessor.service.stax

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType


@XmlAccessorType(XmlAccessType.FIELD)
data class PersonalDataRecord(
    val xpath: String,
    val id: String,
    val value: String,
    val parentId: Int
) {
    constructor() : this("", "", "", -1)
}
