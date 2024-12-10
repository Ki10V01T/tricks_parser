# Tricks parser

This is a utility for parsing winetricks file in order to find and easily download Windows components and drivers using links from a file\
To work, you will need to install Java 17.

    -o path to output folder. In this folder, downloaded packages will be store.
    -p overrides config file path to winetricks file. Uses overrided  value instead of set in json config file.
    --DEBUG ignore other keys and uses debug values (not implemented yet)

If no options are specified, the default values (winetricks stored in /src/resources/winetricks) will be used.

Usage example:
1. Enter next command
```bash
java -jar tricks_parser.jar -o /home/user/downloads/output-folder -p /home/user/documents/winetricks
```
2. The application will display a list of all found packages and prompt you to enter the package number for further download
3. After you make a choice, the application will try to download the Windows package using the link from winetricks

---
Это утилита для парсинга файла winetricks с целью поиска и простой загрузки компонентов и драйверов Windows по ссылкам из файла\
Для работы вам потребуется установить Java 17.

    -o путь к выходной папке. В этой папке будут храниться загруженные пакеты.
    -p переопределяет путь к файлу конфигурации для файла winetricks. Использует значение переопределения вместо заданного в файле конфигурации json.
    --DEBUG игнорирует другие ключи и использует значения отладки (пока не реализовано)
Если параметры не указаны, будут использоваться значения по умолчанию (winetricks, хранящиеся в /src/resources/winetricks).

Пример использования:
1. Введите следующую команду
```bash
java -jar tricks_parser.jar -o /home/user/downloads/output-folder -p /home/user/documents/winetricks
```
2. Приложение отобразит список всех найденных пакетов и предложит вам ввести номер нужного вам пакета для дальнейшей загрузки
3. После того, как вы сделаете выбор, приложение попытается загрузить пакет Windows по ссылке из winetricks в выбранную папку.