package ru.taskdata.processors.NifiMergeProcessor.service

/**
 * Сервис принимает на вход данные для объединения и возвращает смерженные данные
 */
interface IpevMergeService {
    /**
     * Выполняет мерж данных: заменяет данные в RegistryRecord полями из второй XML
     * @param data контент FlowFile
     * @return RegistryRecord c замененными полями
     */
    fun merge(content: String): String
}