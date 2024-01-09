
[[_TOC_]]

--------------------------


### Описание
Данный проект является библиотекой (**nar**) для Apache Ni-Fi.
Библиотека является частью процесса по миграции данных с ИПЭВ1 на ИПЭВ2 

Ссылки (внутренние ресурсы TaskData)
- Ссылка на постановку: [ИПЭВ 2.0 Миграция данных из ИПЭВ1](https://confluence.taskdata.com/pages/viewpage.action?pageId=487424005)
- Ссылка на задачу: [DITIPEVDM-476](https://jira.taskdata.com/browse/DITIPEVDM-476)


### Версии программ
- Apache Ni-Fi 1.32.2
- OpenJDK 1.8.0-292 
  (в целом работает на любой версии 1.8.x-xxx)


### Установка
1. Остановить Apache Ni-Fi, если он запущен
2. В папку `%root_dir_nifi%\lib\` положить скомпилированную библиотеку `*.nar`
3. Запустить Apache Ni-Fi


### Описание работы
Библиотека предназначена для смердживания данных. Т.к. подразумевается очень большой объем записей основной задачей было обеспечить очень быструю работу и минимальные затраты памяти.
1. На вход: подается Flowfile, содержащий в качестве контента текст (пример `\example\input.xml`). В этом тексте склеены 2 xml-файла. Порядок склейки могут быть любыми. Символы переноса строк и табуляции могут быть любыми или отсутствовать.
2. Дааные: 2 xml:
   - registry_record с зашифрованными данными;
   - data, в которая содержит карту замены.
3. Цель: расшифровать данные в registry_record.
Для парсинга xml используется самописный StaxParser с индивидуальной логикой. В данном проекте он был замствован из проекта "ИПЭВ 1".
4. На выходе: Flowfile, содержащий в качестве контента текст (пример `\example\output.xml`). Текст содержит в себе xml registry_record с рашифрованными данными
5. Обработка ошибок: если контент будет пустым или во время обработки сессии возникнут ошибки, то на выходе будет записан FlowFile, который в качестве контента будет содержать человекочитаемое сообщение об ошибке.
Больше информации о работе программы можно найти в тестах. 