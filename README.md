Мои лабораторные работы для BSUIR/БГУИР (белорусский государственный университет информатики и радиоэлектроники).

Предмет - MiAPR/МиАПР (системное программирование).

## Общая информация

Каждая папка в этом репозитории - проект Gradle, который должен быть открыт через IntelliJ IDEA. После установки среды проконтролируйте, чтобы версии Gradle JVM и JDK соответствовали указанным ниже, иначе есть вероятность, что установка пройдёт некорректно.

| Технология | Версия  | Пояснение                                    | Где настроить                                                    |
|------------|---------|----------------------------------------------|------------------------------------------------------------------|
| Gradle     | 8.4-bin | Версия системы автоматической сборки         | -                                                                |
| Gradle JVM | 17.0.9  | Версия Java, используемая для запуска Gradle | File -> Settings -> Build -> Build Tools -> Gradle -> Gradle JVM |
| Kotlin     | 1.9.20  | Версия Kotlin, используемая в проекте        | -                                                                |
| JDK        | 17.0.9  | Версия SDK, используемая в проекте           | File -> Project Structure -> Project -> SDK                      |

Если значения не соответствуют необходимым, необходимо перезагрузить проект Gradle. Ниже об этом будет написано подробнее.

## Установка

Собственно, для начала нужно скачать репозиторий и разархивировать его в любое место на диске. Каждую из папок внутри него будем называть **папкой проекта**. В этих папках лежат папки (gradle, src) и различные файлы.

Первым делом, запустите IntelliJ IDEA и откройте папку проекта: `File -> Open -> [Выбираете папку]`. Сразу после открытия начнётся установка среды. Если от вас потребуется разрешение на скачивание файлов, дайте его. Спустя некоторое время все необходимые файлы скачаются, и среда будет готова к работе.

Если на этом моменте что-то пошло не так, значит, самое время проверить значения, указанные в таблице из первого раздела. После изменения этих значений необходимо перезагрузить проект Gradle. Это можно сделать в **меню Gradle**, нажав на циклические стрелки. Меню можно открыть, нажав на значок слона в верхней правой части окна.

## Условия

### Лабораторная работа 1

Реализовать метод K-средних для классификации множества точек на несколько классов. Вывести изображение с точками разного цвета.

### Лабораторная работа 2

Реализовать метод Максимина для классификации множества точек на несколько классов, причём число классов определяется самостоятельно. Вывести изображение с точками разного цвета.

### Лабораторная работа 3

Реализовать вероятностный метод классификации точек, найти процент ложных тревог и ошибочных распознаваний. Вывести изображение с двумя "колоколами" вероятности, пересекающимися между собой.

### Лабораторная работа 4

Реализовать персептрон. Обучить его на выборке изображений и вынести вердикт по трём тестам.

### Лабораторная работа 5

Реализовать метод потенциалов для классификации точек, где генерируется разделяющая функция. Вывести изображение с функцией и точками разного цвета.

### Лабораторная работа 6

Реализовать метод иерархической классификации. Вывести классификацию.

### Лабораторная работа 7

Реализовать распознавание объектов при помощи синтаксических методов.

### Лабораторная работа 8

Реализовать метод автоматической генерации текстовой информации.

### Лабораторная работа 9

Реализовать многослойный персептрон.

## Использование

Запустить скомпилированные jar-файлы двойным нажатием ЛКМ, либо открыть консоль Windows в папке с jar-файлом и выполнить команду `java -jar JarFileName.jar`. 
