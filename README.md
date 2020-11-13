# Henesis를 사용한 거래소 디지털 자산 입출금 시스템  

### Architecture
TODO

### Requirements
- JAVA (JDK 1.8)
- Spring Boot
- Gradle
- Docker

### Step
1. 환경 설정 <br>
    Henesis 사용을 위해 [application.yaml](./src/main/resources/application.yaml)에서 환경 설정을 합니다.
    
2. 도커 컴포즈 실행 <br>
    예제에서 제공되는 [도커 컴포즈](./environments/docker-compose.yaml)에는 DB(postgesql), Henesis와 연동을 위한 [Enclave API](https://app.gitbook.com/@henesis/s/henesis-wallet/integrate-with-api/installation-and-operation) 가 세팅되어 있습니다. 
    ```shell script
    $ docker-compose -f environments/docker-compose.yaml up -d
    ```

3. 빌드 <br>
   Gradle을 이용해 프로젝트를 빌드하고 실행 가능한 Jar 파일을 생성합니다. Jar 파일은 `/build/libs` 폴더에 생성됩니다.
    ```shell script
   $ chmod +x ./gradlew
   $ ./gradlew clean
   $ ./gradlew build
   $ ./gradlew bootJar
    ```

4. 실행 <br>
    Jar 파일을 실행합니다. 
    ```shell script
    $ java -jar henesis-example.jar
    ```
    
    최초 실행 시에는 초기 데이터 세팅을 위해 init profile을 추가합니다.
    ```shell script
    $ java -jar -Dspring.profiles.active=init henesis-example.jar
    ```    
  
### API
Jar 파일을 실행 후 Swagger(http://localhost:port/swagger-ui.html#/)에서 API 스펙을 확인할 수 있습니다. 
port는 기본 `8080`이고 [application.yaml](./src/main/resources/application.yaml) 에서 설정 된 `server.port` 에서 변경하실 수 있습니다.

### Tutorial
#### 입출금
1. 수수료 지갑에 충분한 수수료를 채워 넣습니다.
2. deposit address 생성 <br>
   `POST /api/exchange/deposit-addresses`
3. deposit address 조회 후 status가 ACTIVE가 될 때 까지 기다립니다. <br>
   `GET /api/exchange/deposit-addresses/{depositAddressId}`
4. 생성한 deposit address로 입금
5. 입금 된 deposit address에서 출금 <br>
   `POST /api/exchange/deposit-addresses/{depositAddressId}/transfer`
6. 입출금 내역을 확인합니다 <br>
   `GET /api/exchange/transfers/{transferId}`
7. 잔고가 차감되었는지 확인합니다. <br>
   `GET /api/exchange/deposit-addresses/{depositAddressId}/balances`

#### 집금
1. 수수료 지갑에 충분한 수수료를 채워 넣습니다.
2. 생성한 deposit address로 입금합니다.
3. 집금합니다. <br>
   `POST /api/exchange/flush`
4. 입출금 내역을 확인합니다 <br>
   `GET /api/exchange/transfers/{transferId}`
5. [헤네시스 대시보드](https://app.wallet.henesis.io) 에서 마스터 지갑에 정상적으로 flush 되었는지 확인합니다. <br>
    집금은 온체인 상 마스터 지갑으로 출금되지만 오프체인(DB)에서는 deposit address에서 차감되지 않습니다.

> 비트코인은 집금 API를 지원하지 않습니다.