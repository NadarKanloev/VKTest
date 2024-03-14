# VKTest
Тестовое задание на стажировку ВК
Для запуска проекта используйте Docker Compose. Выполните следующие команды:

```bash
docker-compose up
```
### [Коллекция запросов POSTMAN](https://www.postman.com/restless-escape-517248/workspace/habraggreagot/collection/27427106-8bc72017-aadf-4883-81a8-e00d90e30008?action=share&creator=27427106)

## Документация

В проекте также имеется подробная документация в формате JavaDoc, встроенная непосредственно в исходный код. Для более подробной информации о классах, методах и их использовании, обратитесь к JavaDoc комментариям в коде.

Кроме того, доступна документация Swagger, которая предоставляет описание API и позволяет взаимодействовать с ним в удобном формате. Вы можете найти Swagger документацию по следующей ссылке:
[http://localhost:8080/swagger-ui/index.html#](http://localhost:8080/swagger-ui/index.html#/)
![image](https://github.com/NadarKanloev/VKTest/assets/44449982/c0ffa557-b5f2-42ca-93d0-2474bda3533b)

## Использование баз данных

В проекте используются следующие базы данных:

- **Apache Cassandra**:
  Для хранения аудитов используется Apache Cassandra. Аудиты хранят id, получен ли запрос из кэша, ошибки(если есть), выполняемый метод, параметры запроса, статус ответа, секция, где выполнялся запрос, ответ сервера, время по МСК, id пользователя и его роль

- **Redis**:
  Redis используется в качестве кэш-хранилища. 

- **PostgreSQL**:
  Для хранения данных о пользователях используется PostgreSQL.

  ## Ролевая модель

В приложении реализована следующая ролевая модель:

- **ROLE_ADMIN**:
  Роль администратора. Администраторы имеют полный доступ ко всем функциям системы.

- **ROLE_POSTS_VIEWER**:
  Роль для просмотра постов. Пользователи с этой ролью могут только просматривать посты, но не могут их создавать или редактировать.

- **ROLE_POSTS_EDITOR**:
  Роль для редактирования постов. Пользователи с этой ролью могут просматривать и редактировать посты.

- **ROLE_USER**:
  Роль для просмотра пользователей через API.

- **ROLE_ALBUMS**:
  Роль для управления альбомами. Пользователи с этой ролью могут просматривать, создавать и редактировать альбомы.

  Выполнено с помощью **Spring Security** + **JWT**

  Роли пользователей меняются в базе данных. По умолчанию в образе Постгреса идут 5 пользователей - по 1 на каждую роль. Пароли хранятся в зашифрованном виде. Пароль для каждого - это username. Перед запросом надо авторизироваться и предоставить полученный JWT-токен

Все контроллеры в проекте покрыты тестами для обеспечения надежной работы приложения. Для написания и запуска тестов использовались следующие библиотеки:

- **JUnit**

- **SpringBootTest**

- **Mockito**
![image](https://github.com/NadarKanloev/VKTest/assets/44449982/81f17a13-65a4-4988-a327-972cb1519941)

## Docker образы

Образы для Spring-приложения, Cassandra и PostgreSQL доступны на моём Docker Hub:

- **Spring-приложение**: [Ссылка на Docker образ Spring-приложения](https://hub.docker.com/repository/docker/veventumt/vktest/general)
- **Cassandra**: [Ссылка на Docker образ Cassandra](hub.docker.com/repository/docker/veventumt/mypg/general)
- **PostgreSQL**: [Ссылка на Docker образ PostgreSQL](https://hub.docker.com/repository/docker/veventumt/vk-cassandra/general)
  
В качестве redis-контейнера использовался последний официальный образ[Ссылка на Docker образ Redis]([https://hub.docker.com/repository/docker/veventumt/vk-cassandra/general](https://hub.docker.com/layers/library/redis/latest/images/sha256-2e791e49d89aa881c7c8c6fa80dd14ea503c6d05b7aec285e7899682a1a7a7f3?context=explore)https://hub.docker.com/layers/library/redis/latest/images/sha256-2e791e49d89aa881c7c8c6fa80dd14ea503c6d05b7aec285e7899682a1a7a7f3?context=explore)
