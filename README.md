
### Описание
Данный проект является библиотекой (**nar**) для Apache Ni-Fi.
Библиотека является частью процесса по миграции данных из [система 1] в [система 2] 

### Постановка задачи
В определенный момент возникла необходимость в миграции данных с ИПЭВ1 в ИПЭВ2. Был построен процесс миграции с помощью Apache NiFi в статье "ИПЭВ 2.0 Миграция данных из ИПЭВ1". В разделе "Библиотека объединения" описан процесс преобразования данных, который должен выполняться самописной библиотекой. Формальную постановку лучше почитать в источнике, т.к. здесь я приведу только основную идею работы.

Идея: в библиотеку приходит текст, который из себя представляет 2 склеенных xml файла:
1. регистровая запись с зашифрованными полями персональных данных;
2. персональные данные с путями, в которые их нужно подставить.

Библиотека должна распарсить этот файл, выполнить преобразование с помощью интегрированного из ИПЭВ1 StaxParser и на выходе отдать регистровую запись с расшифрованными данными.


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
