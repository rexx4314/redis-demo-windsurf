# Redis 데모 Spring Boot 애플리케이션

> 이 프로젝트는 Windsurf AI에서 생성되었습니다. (redis-api-test.http 제외)

Spring Boot와 Redis 연동을 종합적으로 보여주는 데모 애플리케이션입니다. localhost와 Testcontainers 기반 테스트 접근 방식을 모두 지원합니다.

## 📋 개요

이 프로젝트는 Spring Data Redis를 사용하여 Redis와 Spring Boot를 연동하는 방법을 보여줍니다:
- 기본 Redis 연산 (CRUD)
- JSON을 통한 객체 직렬화
- TTL (Time To Live) 기능
- 다양한 테스트 전략 (localhost, Testcontainers, 내장형)
- 프로덕션 환경 준비 구성

## 🚀 주요 기능

- **Java 17** 기반 **Spring Boot 3.2.0**
- **Spring Data Redis**를 통한 **Redis 연동**
- 복잡한 객체를 위한 **JSON 직렬화**
- 자동 키 만료를 위한 **TTL 지원**
- **다중 테스트 전략**:
  - Localhost Redis 테스트
  - Docker를 통한 Testcontainers
  - 우아한 폴백 테스트
- 모든 Redis 연산을 위한 **포괄적인 테스트 커버리지**

## 🛠️ 기술 스택

| 기술 | 버전 | 설명 |
|-------------|---------|-------------|
| Java | 17 | 런타임 환경 |
| Spring Boot | 3.2.0 | 애플리케이션 프레임워크 |
| Spring Data Redis | 3.2.0 | Redis 연동 |
| Gradle | 8.5 | 빌드 도구 |
| JUnit | 5 | 테스트 프레임워크 |
| Testcontainers | 1.19.3 | 컨테이너 기반 테스트 |
| Redis | 7-alpine | 인메모리 데이터 저장소 |

## 📁 프로젝트 구조

```
redis-demo-windsurf/
├── src/
│   ├── main/
│   │   ├── java/com/example/redisdemo/
│   │   │   ├── RedisDemoApplication.java     # 메인 애플리케이션 클래스
│   │   │   ├── config/
│   │   │   │   └── RedisConfig.java          # Redis 설정
│   │   │   └── service/
│   │   │       └── RedisService.java         # Redis 연산 서비스
│   │   └── resources/
│   │       └── application.properties        # 애플리케이션 설정
│   └── test/
│       └── java/com/example/redisdemo/
│           ├── RedisDemoApplicationTests.java           # 기본 컨텍스트 테스트
│           ├── RedisLocalhostIntegrationTest.java       # Localhost Redis 테스트
│           ├── RedisTestcontainersIntegrationTest.java   # Testcontainers 테스트
│           └── RedisEmbeddedIntegrationTest.java         # 내장형/폴백 테스트
├── gradle/                                        # Gradle 래퍼
├── build.gradle                                   # 빌드 설정
├── settings.gradle                                # 프로젝트 설정
└── README.md                                      # 이 파일
```

## 🏃‍♂️ 빠른 시작

### 사전 요구사항

- **Java 17** 이상
- **Gradle 8.5** (래퍼 포함)
- **Redis 서버** (localhost 테스트용, 선택사항)
- **Docker** (Testcontainers 테스트용, 선택사항)

### 설치 방법

1. **저장소 복제** (해당하는 경우):
   ```bash
   git clone <repository-url>
   cd redis-demo-windsurf
   ```

2. **프로젝트 빌드**:
   ```bash
   ./gradlew build
   ```

3. **애플리케이션 실행**:
   ```bash
   ./gradlew bootRun
   ```

애플리케이션이 기본적으로 `http://localhost:18092`에서 시작됩니다.

## 🔧 설정

### Redis 설정

이 애플리케이션은 다양한 Redis 연결 설정을 지원합니다:

#### 기본 설정 (`application.properties`)
```properties
spring.application.name=redis-demo-windsurf
server.port=18092
```

#### 환경 기반 설정

Redis 연결 속성은 다음을 통해 설정할 수 있습니다:
- 환경 변수
- 명령줄 인수
- `application.properties` 파일

**환경 변수 예시**:
```bash
export SPRING_REDIS_HOST=localhost
export SPRING_REDIS_PORT=6379
export SPRING_REDIS_PASSWORD=your-password
```

## 🧪 테스트

이 프로젝트는 다양한 시나리오를 위한 포괄적인 테스트 전략을 포함합니다:

### 1. 기본 테스트
Redis 가용 여부와 관계없이 항상 실행:
```bash
./gradlew test
```

### 2. Localhost Redis 테스트
localhost:6379에서 실행 중인 Redis에 대해 테스트:
```bash
REDIS_LOCALHOST_TEST=true ./gradlew test
```

**사전 요구사항**:
- localhost:6379에서 실행 중인 Redis 서버
- 인증 불필요 (또는 적절히 구성됨)

### 3. Testcontainers 테스트
Docker를 사용하여 Redis 컨테이너를 자동으로 시작:
```bash
DOCKER_AVAILABLE=true ./gradlew test
```

**사전 요구사항**:
- 설치 및 실행 중인 Docker
- 컨테이너 작업을 위한 충분한 메모리

### 4. 내장형/폴백 테스트
Redis 사용 불가 시 우아하게 처리:
```bash
./gradlew test
```

이 테스트들은 Redis를 사용할 수 없을 때 건너뛰기 위해 JUnit 가정을 사용합니다.

### 테스트 커버리지

| 테스트 유형 |涵盖 연산 | Redis 필요 여부 |
|-----------|-------------------|----------------|
| 기본 | Spring 컨텍스트 로딩 | ❌ 아니오 |
| Localhost | 모든 Redis 연산 | ✅ 예 |
| Testcontainers | 모든 Redis 연산 | ✅ 예 (Docker) |
| 내장형 | 모든 Redis 연산 | ⚠️ 선택사항 |

#### 테스트된 Redis 연산

- ✅ **기본 연산**: `set()`, `get()`, `delete()`, `hasKey()`
- ✅ **TTL 연산**: `expire()`, `getExpire()`, 타임아웃과 함께 `set()`
- ✅ **복잡한 객체**: JSON 직렬화/역직렬화
- ✅ **연결 유효성 검사**: Ping 테스트 및 오류 처리
- ✅ **엣지 케이스**: 누락된 키, 만료된 키, 연결 실패

## 📖 사용 예제

### Redis 서비스 사용법

```java
@Autowired
private RedisService redisService;

// 기본 연산
redisService.set("user:1001", "John Doe");
String name = (String) redisService.get("user:1001");

// 복잡한 객체
User user = new User("Alice", 25);
redisService.set("user:1002", user);
User retrievedUser = (User) redisService.get("user:1002");

// TTL 연산
redisService.set("session:abc123", sessionData, 30, TimeUnit.MINUTES);
boolean exists = redisService.hasKey("session:abc123");
long ttl = redisService.getExpire("session:abc123");

// 정리
redisService.delete("user:1001");
```

### 설정 커스터마이징

```java
@Configuration
public class CustomRedisConfig {
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // 커스텀 직렬화기
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        return template;
    }
}
```

## 🔍 API 엔드포인트

이 데모 프로젝트는 REST API보다는 Redis 연동에 중점을 둡니다. 그러나 쉽게 REST 엔드포인트를 추가할 수 있습니다:

```java
@RestController
@RequestMapping("/api/redis")
public class RedisController {
    
    @Autowired
    private RedisService redisService;
    
    @GetMapping("/{key}")
    public ResponseEntity<Object> getValue(@PathVariable String key) {
        Object value = redisService.get(key);
        return value != null ? ResponseEntity.ok(value) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{key}")
    public ResponseEntity<Void> setValue(@PathVariable String key, @RequestBody Object value) {
        redisService.set(key, value);
        return ResponseEntity.ok().build();
    }
}
```

## 🐳 Docker 배포

### Docker Compose 사용

`docker-compose.yml` 생성:

```yaml
version: '3.8'
services:
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    command: redis-server --appendonly yes

  app:
    build: .
    ports:
      - "18092:18092"
    depends_on:
      - redis
    environment:
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PORT=6379

volumes:
  redis_data:
```

실행:
```bash
docker-compose up -d
```

## 📊 성능 고려사항

### Redis 모범 사례

1. **연결 풀링**: 적절한 풀 크기 구성
2. **직렬화**: 효율적인 직렬화기 사용 (JSON, Protobuf)
3. **키 명명**: 일관된 계층적 키 패턴 사용
4. **TTL 관리**: 적절한 만료 시간 설정
5. **메모리 관리**: Redis 메모리 사용량 모니터링

### 설정 튜닝

```properties
# 연결 풀 설정
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.min-idle=0
spring.redis.lettuce.pool.max-wait=-1ms

# 타임아웃 설정
spring.redis.timeout=2000ms
spring.redis.connect-timeout=2000ms
```

## 🚨 문제 해결

### 일반적인 문제

#### 1. 연결 거부
**문제**: `redis.clients.jedis.exceptions.JedisConnectionException: Connection refused`

**해결책**:
- Redis 서버 실행 중인지 확인
- 호스트와 포트 설정 확인
- 방화벽 설정 확인

#### 2. Testcontainers 문제
**문제**: Docker 관련 테스트 실패

**해결책**:
- Docker 실행 중인지 확인
- Docker 데몬 연결 확인
- 충분한 디스크 공간 확인

#### 3. 직렬화 문제
**문제**: `SerializationException`

**해결책**:
- 객체에 기본 생성자가 있는지 확인
- 적절한 getter/setter 추가
- 클래스패스 일관성 확인

### 디버그 모드

디버그 로깅 활성화:
```bash
./gradlew test --debug
```

또는 `application.properties`에 추가:
```properties
logging.level.org.springframework.data.redis=DEBUG
logging.level.redis.clients.jedis=DEBUG
```

## 🤝 기여

1. 저장소 포크
2. 기능 브랜치 생성 (`git checkout -b feature/amazing-feature`)
3. 변경사항 커밋 (`git commit -m 'Add amazing feature'`)
4. 브랜치 푸시 (`git push origin feature/amazing-feature`)
5. Pull Request 열기

## 📝 라이선스

이 프로젝트는 MIT 라이선스 하에 라이선스가 부여됩니다 - [LICENSE](LICENSE) 파일을 참조하세요.

## 🙏 감사의 말

- [Spring Data Redis](https://spring.io/projects/spring-data-redis) - Redis 연동 프레임워크
- [Testcontainers](https://www.testcontainers.org/) - 컨테이너 기반 테스트
- [Redis](https://redis.io/) - 인메모리 데이터 구조 저장소

## 📞 지원

질문과 지원이 필요하시면:
- 저장소에 이슈 생성
- [Spring Data Redis 문서](https://docs.spring.io/spring-data/redis/docs/current/reference/html/) 확인
- [Redis 문서](https://redis.io/documentation) 참조

---

**행복한 코딩! 🚀**
