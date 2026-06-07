# TODO Application — Backend 2 Examenopdracht

Een Spring Boot REST API voor het bijhouden van TODO's per gebruiker, met Spring Security, RabbitMQ audit logging en JWT authenticatie.

## Applicatie starten

### Via IntelliJ IDEA

1. Ga naar **Run → Edit Configurations**
2. Klik op **+** en kies **Spring Boot**
3. Stel de **Main class** in op `com.UCLL.TODO.TodoApplication`
4. Stel bij **Active profiles** in: `dev` of `prod`
5. Voor het `prod` profile: voeg onder **Environment variables** toe:
   - `POSTGRES-HOST=localhost`
   - `POSTGRES-PORT=5433`
   - `POSTGRES-USER=todo-user`
   - `POSTGRES-PWD=todo-password`

### Via Maven (command line)

#### Development (H2 in-memory database)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Productie (PostgreSQL)

Start eerst de PostgreSQL Docker container:

```bash
docker run --name postgres-todo-db -p 5433:5432 \
  -e POSTGRES_USER=todo-user \
  -e POSTGRES_PASSWORD=todo-password \
  -e POSTGRES_DB=todo \
  postgres
```

Start daarna de applicatie:

```bash
POSTGRES-HOST=localhost \
POSTGRES-PORT=5433 \
POSTGRES-USER=todo-user \
POSTGRES-PWD=todo-password \
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

De applicatie start op `http://localhost:8080`.

### RabbitMQ starten

```bash
docker compose up -d
```

RabbitMQ draait op `localhost:5672` met:
- **Username:** myuser
- **Password:** secret

## API Documentatie

Na het starten is Swagger UI beschikbaar op:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI specificatie:

```
http://localhost:8080/v3/api-docs
```

## Test gebruikers

### Admin gebruiker (automatisch aangemaakt bij opstarten)

| Veld | Waarde |
|---|---|
| Email | admin@todo.com |
| Wachtwoord | admin |
| Rol | ADMIN |

### Gewone gebruiker aanmaken

Stuur een `POST` request naar `/api/v2/users`:

```json
{
  "firstName": "Daniel",
  "lastName": "Fernandez",
  "email": "daniel@gmail.com",
  "password": "password"
}
```

## Authenticatie

### Basic Auth

Alle beveiligde endpoints ondersteunen Basic Authentication via de `Authorization` header.

### JWT

Vraag een token op via:

```
POST /api/v2/sessions
```

```json
{
  "email": "daniel@gmail.com",
  "password": "password"
}
```

Gebruik de token als `Bearer` token in de `Authorization` header.

## API Overzicht

### Gebruikers (`/api/v2/users`)

| Methode | Endpoint | Beschrijving | Auth |
|---|---|---|---|
| POST | `/api/v2/users` | Registreren | Publiek |
| GET | `/api/v2/users/me` | Eigen profiel | User |
| PUT | `/api/v2/users` | Profiel updaten | User |
| DELETE | `/api/v2/users` | Profiel verwijderen | User |
| GET | `/api/v2/users` | Alle users | Admin |
| GET | `/api/v2/users/{id}` | User op id | Admin |

### TODO's (`/api/v2/todos`)

| Methode | Endpoint | Beschrijving | Auth |
|---|---|---|---|
| GET | `/api/v2/todos` | Eigen todos | User |
| POST | `/api/v2/todos` | Todo aanmaken | User |
| PUT | `/api/v2/todos/{id}` | Todo updaten | User (eigenaar) |
| DELETE | `/api/v2/todos/{id}` | Todo verwijderen | User (eigenaar) |

### Sessies (`/api/v2/sessions`)

| Methode | Endpoint | Beschrijving | Auth |
|---|---|---|---|
| POST | `/api/v2/sessions` | JWT token opvragen | Publiek |

## Architectuur

```
controller/
  dto/           # Request/Response records
config/          # Security, RabbitMQ configuratie
model/
  messaging/     # RabbitMQ berichten
service/         # Business logica
repository/
  jpa/           # Spring Data JPA interfaces
exception/       # Custom exceptions
```

## Testen

```bash
mvn test
```

De testen omvatten:
- Unit testen (service laag)
- HTTP integratietesten (@WebMvcTest)
- Database integratietesten (@DataJpaTest)
- Component testen (@SpringBootTest)
