package ru.taskdata.processors.NifiMergeProcessor.service.jxml

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.ArrayList


data class PersonalDataJXml(
        @JsonProperty("hash_id")
        val hashId: String,

        @JsonProperty("elements")
        val records: MutableList<PersonalDataRecordJXml>?
) {
    constructor() : this("", ArrayList<PersonalDataRecordJXml>())

}