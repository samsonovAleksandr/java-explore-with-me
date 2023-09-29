# Explore with me
Учебный проект Яндекс.Практикум

## Описание 
![spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![postgres](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white
) ![ide](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white
) ![java](https://img.shields.io/badge/Java11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
) ![markdown](https://img.shields.io/badge/Markdown-000000?style=for-the-badge&logo=markdown&logoColor=white
) ![junit](https://img.shields.io/badge/junit5-DC143C?style=for-the-badge&logo=junit5&logoColor=white
) ![maven](https://img.shields.io/badge/Apache_Maven-008000?style=for-the-badge&logo=apachemaven&logoColor=white) ![docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![rest](https://img.shields.io/badge/REST-FFA500?style=for-the-badge&logo=airbrakedotio&logoColor=white) ![postman](https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)

Сервис-афиша для размещения информации о событиях(от выставки до похода в кино), на котором пользователи могут находить компанию для участия в них.

## Структура

Сервис состоит из двух модулей:
1. [Основной сервис] — отвечает за обработку информации, которая связана с событиями.
    - Основной функционал
        * Public API - доступен без регистрации
            * просмотр событий и подборок событий
            * просмотр категорий
        * Private API - доступен только зарегистрированным пользователям
            * Добавление и редактирование события
            * Работа с запросами на участии в событие
            * Подача/отмена запроса на участие в событии
            * Просмотр информации о запросах на участие
            * Получение полной информации о событиях текущего пользователя
        * Admin API - доступен администратору сервиса
            * Добавление/изменение/удаление категорий
            * Добавление/удаление пользователей
            * Создание/редактирование/удаление подборок событий
            * Получение информации о пользователе
            * Обновление информации о событии
    - Дополнительный функционал
        * [Подписка на друзей]

2. [Сервис статистики] — хранит количество просмотров и позволяет делать различные выборки.

## Схема Базы Данных
![db](ewm-service/src/main/resources/Untitled.png)

