# CaseCareer

CaseCareer — мобильная платформа, которая соединяет студентов с реальными задачами от компаний. Компании публикуют кейсы: практические задания с описанием, вознаграждением и дедлайном. Студенты просматривают ленту, выбирают интересные задачи и отправляют отклики с сопроводительным письмом и ссылкой на решение.

Ключевая идея — дать студентам возможность нарабатывать портфолио на реальных задачах ещё во время учёбы, а компаниям — находить мотивированных исполнителей без сложного найма.

## Команда

- Тарышкина Ксения, 11-304
- Сагадиева Дилара, 11-304

---

## Технический стек

| Слой | Технология |
|---|---|
| Общая логика | Kotlin Multiplatform |
| UI Android | Jetpack Compose |
| UI iOS | SwiftUI |
| Сеть | Ktor Client |
| DI | Koin |
| Локальная БД | SQLDelight |
| Хранение токенов | Multiplatform Settings |
| Аналитика и мониторинг | Firebase Analytics + Crashlytics |
| Линтеры | ktlint (Kotlin), SwiftLint (Swift) |
| CI | GitLab CI |

---

## Функционал

- **Авторизация и регистрация** — вход в аккаунт, регистрация с указанием навыков, портфолио и социальных сетей
- **Лента кейсов** — постраничная загрузка, поиск по названию; карточка кейса с вознаграждением, дедлайном и NDA-меткой
- **Детальный просмотр кейса** — полное описание, информация о компании, кнопка отклика
- **Форма отклика** — многошаговая форма: подписание NDA (если требуется) → сопроводительное письмо + ссылка на решение → экран успеха
- **Мои отклики** — список отправленных откликов со статусами (Отправлен / На проверке / Принято / Отклонено)
- **Профиль** — аватар, рейтинг, навыки, портфолио; выход и удаление аккаунта

---

## Архитектура

Проект построен на **Clean Architecture** + **MVI** (Model-View-Intent).

```
shared/
├── core/               — сеть, DI, локальная БД, CommonViewModel
└── feature/
    ├── auth/           — data / domain / presentation
    ├── feed/           — data / domain / presentation
    ├── response/       — data / domain / presentation
    ├── mycases/        — data / domain / presentation
    └── profile/        — data / domain / presentation

composeApp/             — Android UI (Jetpack Compose)
iosApp/                 — iOS UI (SwiftUI)
```

Каждая фича следует паттерну **State / Event / Effect**:
- `State` — текущее состояние экрана
- `Event` — действия пользователя
- `Effect` — одноразовые побочные эффекты (навигация, показ диалога)

`CommonViewModel` реализован через `expect/actual` — на Android использует `androidx.lifecycle.ViewModel`, на iOS — собственную реализацию с `CoroutineScope`.

---

## Используемые API

API разработано командой бэкенда. В период разработки мобильного приложения бэкенд не был задеплоен, поэтому все запросы тестировались через **Mockoon** — локальный мок-сервер, настроенный по контрактам API.

| Эндпоинт | Метод | Описание |
|---|---|---|
| `/api/v1/auth/login/` | POST | Авторизация |
| `/api/v1/auth/register/user/` | POST | Регистрация студента |
| `/api/v1/auth/token/refresh/` | POST | Обновление токена |
| `/api/v1/cases/` | GET | Список кейсов (с пагинацией и поиском) |
| `/api/v1/cases/{id}/` | GET | Детали кейса |
| `/api/v1/profiles/me/` | GET / PATCH | Профиль текущего пользователя |
| `/api/v1/solutions/` | POST | Отправка отклика |
| `/api/v1/solutions/my/` | GET | Мои отклики |

Аутентификация — JWT (access + refresh токены). Токены хранятся локально через Multiplatform Settings.

---

## Модули

```
CaseCareer-KMP/
├── shared/                     — KMP-модуль с общей логикой
│   └── src/
│       ├── commonMain/         — платформо-независимый код
│       │   ├── core/           — сеть (Ktor), DI (Koin), БД (SQLDelight), CommonViewModel
│       │   └── feature/        — бизнес-логика фич
│       ├── androidMain/        — Android-реализации (HttpEngine, ViewModel)
│       └── iosMain/            — iOS-реализации (HttpEngine, ViewModel, CommonFlow)
├── composeApp/                 — Android-приложение
│   └── src/androidMain/
│       ├── feature/            — экраны на Jetpack Compose
│       ├── designSystem/       — цвета, типографика, тема
│       └── navigation/         — граф навигации
└── iosApp/                     — iOS-приложение
    └── iosApp/
        ├── feature/            — экраны на SwiftUI + ViewModelWrapper
        ├── core/               — дизайн-система, форматтеры, утилиты
        └── navigation/         — RootView, MainTabView
```

---

## Запуск

**Android**
```shell
./gradlew :composeApp:assembleDebug
```
Или Run в Android Studio с подключённым эмулятором.

**iOS**

Открыть `iosApp/iosApp.xcodeproj` в Xcode → Run (⌘R).

---

## Медиа

[Google Drive — материалы проекта](https://drive.google.com/drive/folders/1JttI-uYKW9y8HPjg0tSyK-xWd_CH7Id2?usp=share_link)

- `screencasts/` — скринкаст работы приложения
- `firebase/` — демонстрация Firebase Analytics (DebugView + Events)
- `local CI/` — скриншоты локальных прогонов CI (ktlint + SwiftLint)
- `mockoon/` — конфигурация мок-сервера и логи запросов
- `CaseCareer (о проекте).pdf` — презентация с описанием идеи проекта
