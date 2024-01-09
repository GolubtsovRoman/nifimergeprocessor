package ru.taskdata.processors.NifiMergeProcessor.service.stax

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.events.Characters
import javax.xml.stream.events.EndElement
import javax.xml.stream.events.StartElement


class StaxMerger(
    private val content: ByteArray,
    personalData: PersonalData
) {

    companion object {
        private val inputFactory = XMLInputFactory.newInstance()
        private val outputFactory = XMLOutputFactory.newInstance();
        private val eventFactory = XMLEventFactory.newInstance()
    }

    private val out by lazy { ByteArrayOutputStream() }
    private val writer by lazy { outputFactory.createXMLEventWriter(out) }
    private val eventReader by lazy { inputFactory.createXMLEventReader(ByteArrayInputStream(content)) }
    private val personalData by lazy { personalData.records.map { it.id to it.value }.toMap() }

    private var lockInput = false


    internal fun insertPersData(): ByteArray {
        while (eventReader.hasNext()) {
            val event = eventReader.nextEvent()
            val newEvent = when (event.eventType) {
                XMLStreamConstants.START_ELEMENT -> processStartElement(event.asStartElement())
                XMLStreamConstants.END_ELEMENT -> processEndElement(event.asEndElement())
                XMLStreamConstants.CHARACTERS -> processCharacterEvent(event.asCharacters())
                else -> event
            }
            if (!lockInput) {
                writer.add(newEvent)
            }
        }

        return out.toByteArray()
    }

    private fun processStartElement(startElement: StartElement): StartElement {
        val elementName = startElement.name.localPart.lowercase(Locale.getDefault())
        lockInput = elementName == "hash_id"
        return startElement
    }

    private fun processEndElement(endElement: EndElement): EndElement {
        val elementName = endElement.name.localPart.lowercase(Locale.getDefault())
        lockInput = lockInput && elementName == "hash_id"
        return endElement
    }

    private fun processCharacterEvent(charactersEvent: Characters): Characters {
        val content = personalData[charactersEvent.data.trim()] ?: charactersEvent.data
        return eventFactory.createCharacters(content)
    }

}
