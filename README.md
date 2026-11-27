# Практическая №3 — визуализатор алгоритмов сортировки

Тема №16. Приложение на Java Swing анимирует сортировку одного и того же массива пузырьком, быстрым способом или слиянием. Доступны генерация массива, пауза и настройка задержки.

## Запуск

Требуются JDK 17+ и Maven. Для графического режима нужен рабочий стол.

```sh
mvn clean package
java -jar target/sort-visualizer-1.0.0.jar
java -jar target/sort-visualizer-1.0.0.jar --self-test
```

Для запуска используются Swing и стандартная библиотека JDK. Maven Compiler Plugin задаёт Java 17, Maven Shade Plugin добавляет точку входа и упаковывает Apache Commons Lang 3.17.0, используемый для копирования кадров.

- `ru.sortviz.algorithm` — алгоритмы, интерфейс стратегии и снимки массива.
- `ru.sortviz.app` — окно Swing и воспроизведение кадров.
- [Отчёт Word](Practice-3-Muslimov.docx)
