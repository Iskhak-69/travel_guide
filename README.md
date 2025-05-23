# 🌍 Travel Guide - Путеводитель по Кыргызстану

## 📌 О проекте
**Travel Guide** — это современная платформа, разработанная для упрощения путешествий по Кыргызстану. Она предоставляет удобные инструменты для планирования поездок, поиска достопримечательностей, бронирования услуг и составления маршрутов.

### ❓ Почему это важно?
Туристы часто сталкиваются с нехваткой информации и сложностями при организации путешествий. **Travel Guide** решает эти проблемы, помогая путешественникам находить лучшие локации, планировать маршруты и упрощать бронирование.

---
## 🚀 Основные функции платформы
✅ **Путевые карты** — интерактивный поиск туристических мест и маршрутов.  
✅ **Бронирование услуг** — возможность бронировать отели, рестораны и другие сервисы.  
✅ **Информация о достопримечательностях** — описания мест, культурные особенности, лайфхаки.  
✅ **Рекомендации по маршрутам** — подбор популярных и тематических маршрутов ("Горные походы", "Исторические места" и др.).  
✅ **Простая навигация** — интуитивно понятный интерфейс, который делает использование платформы удобным.  

## 🔐 Настройка безопасности

Это приложение использует Spring Security с JWT для аутентификации без сохранения сессии (stateless) и OAuth2 для входа через социальные сети.

### 🔧 Реализованные функции
- **Аутентификация по логину и паролю**
- **Генерация JWT-токенов**
  - Access Token (токен доступа)
  - Refresh Token (токен обновления, хранится в базе данных)
- **Безопасная настройка срока действия токенов**
- **Контроль доступа на основе ролей**
- **Документация через Swagger UI**

---

## 🔄 Процесс аутентификации через JWT

1. **Регистрация**

```http
POST /trusted/auth/sign-up
Content-Type: application/json

{
  "email": "user@example.com",
  "username": "user123",
  "password": "securePassword123"
}

POST /trusted/auth/login
Content-Type: application/json

{
  "username": "user123",
  "password": "securePassword123"
}


{
  "token": "JWT_ACCESS_TOKEN",
  "email": "user@example.com",
  "refreshToken": "JWT_REFRESH_TOKEN"
}


GET /trusted/auth/user/1
Authorization: Bearer JWT_ACCESS_TOKEN

