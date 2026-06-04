# CaseCareer

Мобильное приложение для студентов, ищущих кейсы от компаний. Позволяет просматривать кейсы, откликаться на них, отслеживать свои отклики и управлять профилем.

Kotlin Multiplatform проект с таргетами Android и iOS.

## Команда

- Тарышкина Ксения, 11-304
- Сагадиева Дилара, 11-304

## Стек

- **Kotlin Multiplatform** — общая бизнес-логика для Android и iOS
- **Jetpack Compose** — UI на Android
- **SwiftUI** — UI на iOS
- **Ktor** — HTTP-клиент
- **Koin** — DI
- **SQLDelight** — локальная база данных
- **MVI** — архитектура (State / Event / Effect + CommonViewModel)
- **Firebase Analytics + Crashlytics**

## Функциональность

- Авторизация и регистрация
- Лента кейсов от компаний
- Детальный просмотр кейса
- Форма отклика (с поддержкой NDA-кейсов)
- Мои отклики
- Профиль пользователя

## Запуск Android

```shell
./gradlew :composeApp:assembleDebug
```

Или запустить через Run в Android Studio с подключённым эмулятором / устройством.

## Запуск iOS

Открыть `iosApp/iosApp.xcodeproj` в Xcode и нажать Run (⌘R).

## Медиа

Папка `media/` содержит:
- `screencasts/` — скринкаст работы приложения
- `firebase/` — демонстрация работы Firebase Analytics (DebugView + Events)
- `local_CI/` — скриншоты локальных прогонов CI (ktlint + SwiftLint)

