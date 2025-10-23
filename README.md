# Android-приложение для отображения списка Покемоном с возможностью фильтрации и поиска по названию
Приложение использует [PokeApi](https://pokeapi.co) для получения данных о Покемонов из таких экранов:  
1. Экран списка Покемонов, который поддерживает поиск по названию  
2. Экран детальной информации о Покемоне  
3. Экран фильтрации
Также поддержано кэширование списка и пагинация, приложение построено по принципам Clean Architecture.

## Стек технологий:
**Архитектура:** MVVM, Clean Architecture  
**Асинхронность:** Kotlin Flow, Coroutines  
**Сеть:** Retrofit, OkHttp  
**Хранение данных:** Room  
**DI:** Dagger Hilt  
**UI:** XML, ViewBinding, Navigation Component, Material Design  

## Демонстрация работы
<p align="center">
  <img src="./docs/pokemon_demo_recording.gif" width="320" alt="Демонстрация работы приложения" />
</p>

## Скриншоты
<p align="center">
  <img src="./docs/screenshots/pokemon_list_screenshot.png" width="230" alt="Экран списка покемонов" />
<img src="./docs/screenshots/pokemon_list_with_search_screenshot.png" width="230" alt="Экран списка покемонов с примененным поиском" />
  <img src="./docs/screenshots/pokemon_details_screenshot.png" width="230" alt="Экран деталей покемона" />
  <img src="./docs/screenshots/filters_screenshot.png" width="230" alt="Экран фильтров" />
</p>

## Автор
Алина Акимова  
Android-разработчик  
[GitHub](https://github.com/malinochkaaa)

