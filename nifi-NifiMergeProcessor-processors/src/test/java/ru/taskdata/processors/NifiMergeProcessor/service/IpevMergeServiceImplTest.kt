package ru.taskdata.processors.NifiMergeProcessor.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.measureTimeMillis


class IpevMergeServiceImplTest {

    companion object {
        // example files
        private const val EXAMPLE_1_INPUT_XML = "src/test/resources/merge/example_1_input.xml"
        private const val EXAMPLE_1_OUTPUT_XML = "src/test/resources/merge/example_1_output.xml"

        private const val EXAMPLE_2_INPUT_XML = "src/test/resources/merge/example_2_input.xml"
        private const val EXAMPLE_2_OUTPUT_XML = "src/test/resources/merge/example_2_output.xml"

        private const val EXAMPLE_3_INPUT_XML = "src/test/resources/merge/example_3_input.xml"
        private const val EXAMPLE_3_OUTPUT_XML = "src/test/resources/merge/example_3_output.xml"

        private const val EXAMPLE_4_INPUT_XML = "src/test/resources/merge/example_4_input.xml"
        private const val EXAMPLE_4_OUTPUT_XML = "src/test/resources/merge/example_4_output.xml"

        private const val EXAMPLE_5_INPUT_XML = "src/test/resources/merge/example_5_input.xml"
        private const val EXAMPLE_5_OUTPUT_XML = "src/test/resources/merge/example_5_output.xml"
        // ---

        private val mergeService: IpevMergeService = IpevMergeServiceImpl()
    }


    @Test
    fun `case data after registry_record`() {
        testCase(EXAMPLE_1_INPUT_XML, EXAMPLE_1_OUTPUT_XML)
        testCase(EXAMPLE_2_INPUT_XML, EXAMPLE_2_OUTPUT_XML)
    }

    @Test
    fun `case data before registry_record`() {
        testCase(EXAMPLE_3_INPUT_XML, EXAMPLE_3_OUTPUT_XML)
    }

    @Test
    fun `case tag data in registry_record`() {
        testCase(EXAMPLE_4_INPUT_XML, EXAMPLE_4_OUTPUT_XML)
    }

    @Test
    fun `case tag other_data in registry_record`() {
        testCase(EXAMPLE_5_INPUT_XML, EXAMPLE_5_OUTPUT_XML)
    }

    @Test @Disabled
    fun `big stream data`() {
        val elapsedTime = measureTimeMillis {
            for(i: Int in 1..100_000) {
                testCase(EXAMPLE_1_INPUT_XML, EXAMPLE_1_OUTPUT_XML)
                testCase(EXAMPLE_2_INPUT_XML, EXAMPLE_2_OUTPUT_XML)
                testCase(EXAMPLE_3_INPUT_XML, EXAMPLE_3_OUTPUT_XML)
                testCase(EXAMPLE_4_INPUT_XML, EXAMPLE_4_OUTPUT_XML)
                testCase(EXAMPLE_5_INPUT_XML, EXAMPLE_5_OUTPUT_XML)
            }
        }

        println("The code took to execute $elapsedTime sec")
    }

    private fun testCase(pathFileIn: String, pathFileOut: String) {
        val flowFileContentIn = readUsingFiles(pathFileIn)
        val flowFileContentOut = mergeService.merge(flowFileContentIn)
        val resultOut = readUsingFiles(pathFileOut)

        Assertions.assertEquals(flowFileContentOut, resultOut)
    }

    private fun readUsingFiles(fileName: String) = String(Files.readAllBytes(Paths.get(fileName)))

}
