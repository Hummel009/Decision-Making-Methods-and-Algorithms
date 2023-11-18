Мои лабораторные работы для BSUIR/БГУИР (белорусский государственный университет информатики и радиоэлектроники).

Предмет - MiAPR/МиАПР (системное программирование).

## Общая информация

Каждая папка в этом репозитории - проект Gradle, который должен быть открыт через IntelliJ IDEA. Проконтролируйте, чтобы версии Gradle JVM и JDK соответствовали указанным ниже.

| Технология | Версия  | Пояснение                                    | Примечание                                                       |
|------------|---------|----------------------------------------------|------------------------------------------------------------------|
| Gradle     | 8.4-bin | Версия системы автоматической сборки         | -                                                                |
| Gradle JVM | 17.0.9  | Версия Java, используемая для запуска Gradle | [Настраивается в переменных средах ОС (JAVA_HOME и Path)][Link1] |
| Kotlin     | 1.9.20  | Версия Kotlin, используемая в проекте        | -                                                                |
| JDK        | 17.0.9  | Версия SDK, используемая в проекте           | [Настраивается в IntelliJ IDEA (Project Structure)][Link2]       |

[Link1]: https://java-lessons.ru/first-steps/java-home#:~:text=Теперь%20щёлкните%20правой%20кнопкой
[Link2]: https://www.jetbrains.com/help/idea/sdk.html#change-module-sdk

## Условия работ

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
