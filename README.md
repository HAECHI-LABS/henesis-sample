# HaechiLabs-Onboarding-Assignment
 HenesisAPI 를 활용한 거래소 시스템 구축

## Environment
> Language      : JAVA (JDK 14)<br/>
> Framework     : Spring Boot 2.3.4.RELEASED <br/>
> BuildTool     : Gradle<br/>
> Messaging     : RabbitMQ<br/>
> ORM           : JpaRepository<br/>
> IDE           : IntelliJ<br/>

<br/>

## Structure
  ```
    └─ application
        └─ ExchangeApplicationService
        └─ MonitoringApplicationService
    └─ config
        └─ AsyncThreadConfig
        └─ HenesisWalletConfig
        └─ HenesisWalletProperties
        └─ MapperConfig
        └─ RestTemplateConfig
        └─ SchedulerConfig
    └─ domain
        └─ ExchangeService
        └─ MonitoringService
        └─ FlushedTx
        └─ UserWallet
        └─ Details
        └─ MasterWalletBalance
    └─ infra
        └─ ExchangeServiceImpl
        └─ HenesisWalletSerivce
        └─ MonitoringSterviceImpl
    └─ web
        └─ ExchangeController
        └─ MonitoringCotroller
  ```

<br/>

## Guide-line
**Enclave API 컨테이너 생성** <br>
```
docker run -d -e NODE_ENV=test -p 3000:3000 haechi/sdk-enclave:stable npm start
```

**PostgreSQL 컨테이너 생성** <br>
```
docker run --name postgres -e POSTGRES_PASSWORD=1234 -d -p 5433:5433 postgres
```

<br>

## Problem

<br>

## Solution

<br><br>

## Timeline
**Notion 참고**
<br><br>


## Impression

<br><br>
## References
<br><br>