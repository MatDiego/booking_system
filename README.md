# Platforma do Organizacji Wydarzeń (Backend)

Backend platformy do organizacji i zarządzania wydarzeniami, zrealizowany jako aplikacja monolityczna w oparciu o framework Spring Boot.

---

## 🚀 Przegląd Projektu

Projekt jest aplikacją backendową demonstrującą rozwiązania w ekosystemie Java/Spring.  
Obejmuje pełen cykl życia zasobów:

- modelowanie danych i migracje schematu,
- implementację API REST,
- mechanizmy bezpieczeństwa i walidacji.

Architektura aplikacji jest warstwowa.

---

## ✨ Kluczowe Funkcjonalności

Aplikacja obsługuje trzy główne role użytkowników (`USER`, `ORGANIZER`, `ADMIN`) z dedykowanymi uprawnieniami.

### Dla Wszystkich Użytkowników (Publiczne)

- Rejestracja nowego konta.
- Logowanie i uzyskiwanie tokenu uwierzytelniającego JWT.

### Dla Zalogowanych Użytkowników (`ROLE_USER`)

- Przeglądanie listy wydarzeń z paginacją i filtrowaniem.
- Wyświetlanie szczegółów wydarzenia.
- Zapisywanie się na wydarzenie.
- Przeglądanie własnych rejestracji.
- Anulowanie rejestracji.

### Dla Organizatorów (`ROLE_ORGANIZER`)

- Wszystkie uprawnienia zwykłego użytkownika.
- Tworzenie wydarzeń.
- Edycja (`PATCH`) i anulowanie własnych wydarzeń.
- Przeglądanie uczestników zapisanych na własne wydarzenia.

### Dla Administratorów (`ROLE_ADMIN`)

- Wszystkie uprawnienia organizatora, ale w odniesieniu do **każdego** wydarzenia.
- Zarządzanie rolami użytkowników.

---

## 🛠️ Stos Technologiczny

- **Język i Framework**: Java 21, Spring Boot 3+
- **API**: Spring Web MVC
- **Bezpieczeństwo**: Spring Security 6, JWT
- **Dane**: Spring Data JPA, Hibernate
- **Baza Danych**: PostgreSQL
- **Migracje**: Liquibase
- **Walidacja**: Hibernate Validator
- **Mapowanie**: MapStruct
- **Narzędzia**: Lombok
- **Testowanie**: JUnit 5, Mockito, AssertJ
- **Konteneryzacja**: Docker

---

## 📖 Dokumentacja API

Poniżej znajduje się lista głównych endpointów API.

### Autentykacja
| Metoda HTTP | Endpoint | Opis | Wymagane Uprawnienia |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Rejestracja nowego użytkownika. | Publiczny |
| `POST` | `/api/auth/login` | Logowanie i otrzymanie tokenu JWT. | Publiczny |

### Wydarzenia (Events)
| Metoda HTTP | Endpoint | Opis | Wymagane Uprawnienia |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/events` | Tworzy nowe wydarzenie. | `ORGANIZER`, `ADMIN` |
| `GET` | `/api/events` | Zwraca paginowaną listę wszystkich wydarzeń. | `AUTHENTICATED` |
| `GET` | `/api/events/{eventId}` | Zwraca szczegóły jednego wydarzenia. | `AUTHENTICATED` |
| `GET` | `/api/events/search` | Zwraca filtrowaną listę wydarzeń. | `AUTHENTICATED` |
| `GET` | `/api/events/my-events` | Zwraca listę wydarzeń stworzonych przez zalogowanego organizatora. | `ORGANIZER`, `ADMIN` |
| `PATCH` | `/api/events/{id}` | Częściowa aktualizacja wydarzenia. | Właściciel lub `ADMIN` |
| `PUT` | `/api/events/{id}/cancel` | Anuluje (dezaktywuje) wydarzenie. | Właściciel lub `ADMIN` |
| `GET` | `/api/events/{id}/participants`| Zwraca listę uczestników danego wydarzenia. | Właściciel lub `ADMIN` |

### Rejestracje (Registrations)
| Metoda HTTP | Endpoint | Opis | Wymagane Uprawnienia |
| :--- | :--- | :--- | :--- |
| `POST`| `/api/registrations/events/{eventId}` | Zapisuje zalogowanego użytkownika na wydarzenie. | `AUTHENTICATED` |
| `GET` | `/api/registrations/my-registrations` | Zwraca listę rejestracji zalogowanego użytkownika. | `AUTHENTICATED` |
| `PUT` | `/api/registrations/{id}/cancel` | Anulowanie własnej rejestracji przez użytkownika. | Właściciel rejestracji |

### Zarządzanie (Admin)
| Metoda HTTP | Endpoint | Opis | Wymagane Uprawnienia |
| :--- | :--- | :--- | :--- |
| `PUT` | `/api/users/{userId}/roles` | Modyfikuje role przypisane do użytkownika. | `ADMIN` |

## 🗃️ Baza Danych
Projekt wykorzystuje PostgreSQL jako system zarządzania bazą danych. Ewolucja schematu bazy danych jest zarządzana za pomocą narzędzia **Liquibase**. Wszystkie zmiany w strukturze są zdefiniowane jako `changeset`-y w plikach `changelog`.

## 🧪 Testowanie
* **Testy Jednostkowe:** Pokrycie dla mapperów, logiki `UserDetailsServiceImpl`, `JwtServiceImpl` oraz modelu `UserPrincipal`.
* **(Planowane/W Trakcie) Testy Integracyjne:** Dla kontrolerów i przepływów API z użyciem MockMvc.

## 🏁 Uruchomienie Projektu

Wykorzystując dockera.

### Wymagania

* Zainstalowany Docker i Docker Compose.
* Zainstalowany Git.

### Konfiguracja

Projekt wykorzystuje plik `.env` do zarządzania zmiennymi środowiskowymi, co pozwala na łatwą konfigurację bez modyfikacji plików śledzonych przez Git.

1.  W głównym katalogu projektu stwórz plik o nazwie `.env`.
2.  Skopiuj do niego poniższą zawartość i dostosuj wartości do swoich potrzeb.

    ```env
    # Konfiguracja Bazy Danych
    DB_NAME=db_name
    DB_USERNAME=username
    DB_PASSWORD=password

    # Konfiguracja JWT
    JWT_SECRET_KEY=sekret_jwt
    JWT_EXPIRATION_MS=86400000
    ```

### Uruchomienie

1.  Sklonuj repozytorium na swój lokalny komputer:
    ```bash
    git clone https://github.com/MatDiego/booking_system.git
    ```

2.  Przejdź do katalogu z projektem:
    ```bash
    cd <NAZWA_KATALOGU_PROJEKTU>
    ```

3.  Upewnij się, że stworzyłeś i skonfigurowałeś plik `.env` zgodnie z instrukcją w sekcji "Konfiguracja".

4.  Uruchom całą aplikację za pomocą jednej komendy:
    ```bash
    docker-compose up --build
    ```

Aplikacja będzie dostępna pod adresem `http://localhost:8080`.