# Zaawansowana Platforma do Organizacji Wydarzeń (Backend)

## 🚀 Przegląd Projektu

Jest to backend platformy do organizacji i zarządzania wydarzeniami, **aktualnie w aktywnej fazie rozwoju**. Projekt został zrealizowany jako aplikacja monolityczna oparta na frameworku Spring Boot. Aplikacja backendowa demonstruje rozwiązania w zakresie technologii Java/Spring, od modelowania danych po implementację kluczowych funkcjonalności API REST z uwzględnieniem bezpieczeństwa i obsługi błędów.
## ✨ Kluczowe Funkcjonalności

*   **Moduł Uwierzytelniania i Użytkowników:**
    *   Rejestracja nowego użytkownika domyślną rolą `ROLE_USER`.
    *   Logowanie z generowaniem tokenu JWT.
    *   Podstawowe zarządzanie rolami użytkownika - dostępne dla `ROLE_ADMIN`.
*   **Moduł Wydarzeń:**
    *   Tworzenie nowego wydarzenia przez `ROLE_ORGANIZER` / `ROLE_ADMIN`.
    *   Pobieranie paginowanej listy wydarzeń.
    *   Pobieranie szczegółów konkretnego wydarzenia.
*   **Moduł Rejestracji na Wydarzenia:**
    *   Zapis zalogowanego użytkownika na wydarzenie.
    *   Pobieranie listy rejestracji zalogowanego użytkownika.

## 🛠️ Stack Technologiczny

*   **Język i Framework:** Java 21, Spring Boot 3+
*   **API:** Spring Web MVC
*   **Dane:** Spring Data JPA, Hibernate
*   **Baza Danych:** PostgreSQL
*   **Migracje:** Liquibase
*   **Bezpieczeństwo:** Spring Security, JWT
*   **Walidacja:** Hibernate Validator
*   **Mapowanie:** MapStruct
*   **Narzędzia Pomocnicze:** Lombok
*   **Testowanie:** JUnit, Mockito, AssertJ

## 🧪 Testowanie

*   **Testy Jednostkowe:** Pokrycie dla mapperów, logiki `UserDetailsServiceImpl`, `JwtServiceImpl` oraz modelu `UserPrincipal`.
*   **(Planowane/W Trakcie) Testy Integracyjne:** Dla kontrolerów i przepływów API z użyciem MockMvc.