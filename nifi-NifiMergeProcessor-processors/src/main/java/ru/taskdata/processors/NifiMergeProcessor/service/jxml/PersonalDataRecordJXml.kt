package ru.taskdata.processors.NifiMergeProcessor.service.jxml


data class PersonalDataRecordJXml(
    val xpath: String,
    val id: String,
    val value: String,
    val parentId: Int
) {
    constructor() : this("", "", "", -1)
}
