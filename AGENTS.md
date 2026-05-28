# AGENTS.md - AI Agent Guide for Orders Desk

## ⚠️ CRITICAL RULES - READ FIRST

### Git Operations - USER CONTROLS ALL GIT ACTIONS
**NEVER automatically commit or push code to git without explicit user permission!**

**Rules for Git Operations**:
1. ❌ **DO NOT** run `git add` without user approval
2. ❌ **DO NOT** run `git commit` without user approval  
3. ❌ **DO NOT** run `git push` without user approval
4. ❌ **DO NOT** run `git pull` or `git merge` without user approval
5. ✅ **ALWAYS ask the user first** before any git operation
6. ✅ **Show what will be committed** and wait for user confirmation
7. ✅ **Explain the changes** before committing

**Allowed Git Operations (informational only)**:
- ✅ `git status` - Check repository status
- ✅ `git diff` - Show file changes
- ✅ `git log` - View commit history
- ✅ `git branch` - List branches
- ✅ Any read-only git command

**How to Handle Code Changes**:
1. Make the code changes in files
2. Show the user what was changed with `git diff`
3. **STOP and ask**: "Would you like me to commit and push these changes?"
4. **WAIT for explicit user permission**
5. Only proceed if user says YES

**Example - CORRECT Workflow**:
```
Agent: I've updated the Dockerfile to fix the image issue.
Agent: Running git diff to show changes...
Agent: [shows diff output]
Agent: Would you like me to commit and push these changes to git?
User: yes
Agent: [now runs git add, git commit, git push]
```

**Example - INCORRECT Workflow** ❌:
```
Agent: I've updated the Dockerfile.
Agent: [automatically runs git add, git commit, git push without asking]
❌ NEVER DO THIS!
```

**User maintains full control over**:
- What gets committed
- Commit messages
- When to push to remote
- Which branch to push to
- Merge decisions

## Project Overview
**Orders Desk** is a Spring Boot 3.3.4 food-selling application with a monolithic backend architecture. The system manages products, categories, customers, and orders through RESTful APIs. The architecture is intentionally simple to support future modularization.

**Key Stack**: Spring Boot, PostgreSQL, Liquibase, MapStruct, Caffeine Cache, Lombok, JPA/Hibernate

## Architecture Patterns

### Layered Structure
The project follows strict **4-layer architecture**:
- **Controllers** (`controller/`): REST endpoints with `@RestController`, `@RequestMapping`, and `@CrossOrigin` for CORS
- **Services** (`service/`): Business logic layer with interface + implementation pattern; implementations use `@Service` + `@RequiredArgsConstructor`
- **Repositories** (`repository/`): Spring Data JPA interfaces extending `JpaRepository<Entity, Long>`
- **Models** (`model/`): JPA entities with Lombok annotations (`@Data`, `@AllArgsConstructor`, `@RequiredArgsConstructor`, `@ToString`)

**Example Pattern**:
```
ProductCategoriesController → ProductCategoryService (interface) 
  → ProductCategoryServiceImpl → ProductCategoryRepository
```

### DTO Layer with MapStruct Mapping
DTOs are explicit separate objects (`dto/`) mapped via **MapStruct** (`mappers/`):
- Use `@Mapper(componentModel = "spring")` for auto-registration
- Define simple `toEntity()` and `toDto()` methods
- Services always work with DTOs in public methods; repositories with entities
- Example: `ProductCategoryServiceImpl.create()` accepts `ProductCategoryDto`, converts to entity, then back to DTO

### Database Design
- **Liquibase** manages all schema changes via XML changesets (`db/changelog/changeset/`)
- Master changelog includes all changesets via `<includeAll path="db/changelog/changeset/"/>`
- Entities use `@Entity @Table(name = "...")` with identity-based ID generation
- Timestamps use `LocalDateTime` with `insertable = false, updatable = false` (database defaults)
- Relationships: `@ManyToOne` with `@JoinColumn` (e.g., Product → ProductCategory)

### Caching Strategy
- **Caffeine** cache implementation enabled via `@EnableCaching` in `OrdersApplication`
- Config: `maximumSize=500, expireAfterWrite=5m` (see `application.properties`)
- Usage: `@Cacheable(value = "productCategories")` on read methods; `@CacheEvict(value = "productCategories", allEntries = true)` on write
- Cache names must be registered in `spring.cache.cache-names`

## Critical Developer Workflows

### Local Development Setup
```bash
# Start PostgreSQL via Docker Compose (required - no fallback DB)
docker-compose up -d

# Build project
./gradlew clean build

# Run application
./gradlew bootRun
```

**Application runs on port 8080** by default. Database connection uses `jdbc:postgresql://postgres:5432/mydatabase` (configured in `application.properties`).

### Adding New Entity & CRUD Endpoint
1. Create model in `model/` with JPA annotations + Lombok
2. Create corresponding DTO in `dto/`
3. Create `FooRepository extends JpaRepository<Foo, Long>` in `repository/`
4. Create `FooService` interface in `service/` with methods
5. Create `FooServiceImpl` in `service/impl/` implementing interface with `@Service`, `@RequiredArgsConstructor`, `@Slf4j`
6. Use `FooMapper` interface with `@Mapper(componentModel = "spring")` for conversions
7. Create `FooController` with `@RestController @RequestMapping("/api/v1/foos") @CrossOrigin @RequiredArgsConstructor`
8. Add Liquibase changeset in `db/changelog/changeset/` (auto-included)

### Testing Patterns
- **Unit tests**: Use `@ExtendWith(MockitoExtension.class)` + `@Mock` + `@InjectMocks` for service layer isolation
- **Integration tests**: Use `@SpringBootTest` for full context loading
- Test class naming: `*Test` suffix (e.g., `CustomerServiceTest`)
- Repository tests use Mockito; service tests verify interactions with mocked repositories

### Running Tests
```bash
./gradlew test
```

## Project-Specific Conventions

### Naming Conventions
- **Controllers**: Plural resource names (`ProductCategoriesController`, `/api/v1/product-categories`)
- **API versioning**: Always use `/api/v1/` prefix
- **Services**: Suffix with `Service` (interface) / `ServiceImpl` (implementation)
- **Repositories**: Suffix with `Repository` (e.g., `ProductCategoryRepository`)
- **DTOs**: Suffix with `Dto` (e.g., `ProductCategoryDto`)
- **Mappers**: Suffix with `Mapper` (e.g., `ProductCategoryMapper`)

### Lombok Usage Rules
- **@RequiredArgsConstructor**: Always used for dependency injection in services/controllers (replaces `@Autowired`)
- **@Data**: Used on entities and DTOs for boilerplate
- **@Slf4j**: Used on service implementations for logging
- **@AllArgsConstructor** + **@RequiredArgsConstructor**: Both on model entities for flexibility
- **@ToString**: Used on entities for debugging

### Java Version & Compatibility
- **Java 17** (defined in `build.gradle` via `JavaLanguageVersion.of(17)`)
- Uses Jakarta EE (`jakarta.persistence.*` not `javax.persistence.*`)
- Records not used; traditional classes with Lombok

### API Response Format
- **Success responses**: HTTP 200 with DTO or List<DTO>
- **Creation responses**: HTTP 201 with created DTO (implicit in Spring)
- **No explicit DTOs for error handling** - use Spring defaults
- **CORS enabled** on all controllers via `@CrossOrigin`

## Integration Points & External Dependencies

### Database Connection
- **PostgreSQL 15** only (see `docker-compose.yml`)
- Connection string in `application.properties`: `jdbc:postgresql://postgres:5432/mydatabase`
- Auto-schema updates via Hibernate + Liquibase migration

### Gradle Build System
- Spring Boot 3.3.4 with dependency management
- Key dependencies:
  - `spring-boot-starter-data-jpa` (ORM)
  - `spring-boot-starter-web` (REST)
  - `spring-boot-starter-cache` (caching)
  - `mapstruct:1.6.3` + processor (annotation-driven mapping)
  - `liquibase-core` (migrations)
  - `assertj:3.6.1` (assertions in tests)

### Service Dependencies (from Build)
- **No external APIs integrated yet** (project is catalog/order system only)
- **DevTools** enabled for hot-reload development
- **Docker Compose support** auto-loads PostgreSQL on startup

## Common Pitfalls for AI Agents

1. **🚨 NEVER auto-commit or push to git** - ALWAYS ask user permission first (see Critical Rules at top)
2. **Always use DTOs in service public methods** - Never expose entities directly
3. **Mapper generation**: Use `@Mapper(componentModel = "spring")` to ensure Spring autowiring
4. **Cache invalidation**: When implementing create/update/delete, use `@CacheEvict` on same methods
5. **Database timestamps**: Always set `insertable = false, updatable = false` for created_at fields
6. **Liquibase changesets**: New schemas MUST have XML changesets; don't rely on Hibernate DDL alone
7. **Controller CORS**: Add `@CrossOrigin` to all new controllers for Telegram mini-app access
8. **API versioning**: New endpoints go to `/api/v1/*` - versioning is project-wide standard
9. **Service interfaces required**: Don't create service classes directly; always use interface + impl pattern

## Directory Map for Key Components

```
src/main/java/com/ta/orders/
  ├── controller/           # REST endpoints
  ├── service/              # Business logic interfaces
  │   └── impl/             # Service implementations
  ├── repository/           # Data access layer (Spring Data JPA)
  ├── model/                # JPA entities
  ├── dto/                  # DTOs for API/service boundaries
  ├── mappers/              # MapStruct converters
  └── OrdersApplication.java  # Spring Boot entry point with @EnableCaching

src/main/resources/
  ├── application.properties  # Database, cache, JPA config
  └── db/changelog/          # Liquibase migration files
```

