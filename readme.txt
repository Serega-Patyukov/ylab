    5 задание
Исходники - src/main/java/ru/patyukov/ylab/zadanie5
Файл статистики и файлы истории - src/main/resources/static/file/zadanie5
HTML файлы - src/main/resources/templates/zadanie5
Тесты - src/test/java/ru/patyukov/ylab/zadanie5

    Переписал структуру игры.
Теперь есть игровой движок.
Движок работает не зависимо от интерфейса игры. То есть он работает одинаково, как при консольной, так и при графической реализации интерфейса игры.
Данные получает одними методами в любой реализации. И обрабатывает их одинаково.

    Игровой движок
Лежит в пакете - src/main/java/ru/patyukov/ylab/zadanie5/game
В пакете model - лежит класс Gameplay, объект которого, хранит историю игры.
В пакете parser - лежат классы для парсинга файлов xml и json.
Главный класс движка - GameXO.
Классы корневого пакета движка:
    Cell - клетка;
    Field - поле;
    GameResult - результат игры;
    GameXO - ядро движка;
    Player - игрок;
    StatisticsPlayer - статистика игроков;
    Step - ход;


    Консольная игра
Лежит в пакете src/main/java/ru/patyukov/ylab/zadanie5/console
Консольная игра состоит из одного класса TestGame
Метод main() класса этого класса запускает игру.

    Веб игра
Написана с использованием Spring Boot
Лежит в пакете src/main/java/ru/patyukov/ylab/zadanie5/springboot/controller
Веб игра состоит из двух контроллеров
Класс запуска игры src/main/java/ru/patyukov/ylab/YlabApplication.java
Начальная страница игры в браузере http://localhost:8080/gameplay
