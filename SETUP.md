# Rezervasyon Yönetim Sistemi - Kurulum Kılavuzu

## 🚀 Uygulamayı Ayağa Kaldırma Adımları

### 1. Önkoşullar

Sisteminizde aşağıdakilerin kurulu olduğundan emin olun:
- **Java 21** (OpenJDK veya Oracle JDK)
- **Maven 3.6+**
- **Docker & Docker Compose**
- **Git**

### 2. Projeyi Klonlama

```bash
git clone <repository-url>
cd reservation-http-api
```

### 3. MongoDB Database Kurulumu

#### Option A: Docker Compose ile (Önerilen)

```bash
# MongoDB ve Mongo Express'i başlat
docker-compose up -d

# Servislerin durumunu kontrol et
docker-compose ps

# Logları görüntüle
docker-compose logs -f mongodb
```

#### Option B: Local MongoDB Kurulumu

Eğer Docker kullanmak istemiyorsanız:

1. MongoDB'yi [resmi siteden](https://www.mongodb.com/try/download/community) indirin
2. MongoDB'yi başlatın
3. `mongodb-init.js` scriptini çalıştırın:

```bash
mongosh mongodb://localhost:27017/reservation_db mongodb-init.js
```

### 4. Environment Variables

Uygulama aşağıdaki environment variable'ları kullanır:

#### Production Environment:
```bash
export SPRING_PROFILES_ACTIVE=prod
export SERVER_PORT=8080
export MONGODB_URI=mongodb://admin:admin123@localhost:27017/reservation_db?authSource=admin
export MONGODB_DATABASE=reservation_db
```

#### Development Environment (Default):
```bash
export SPRING_PROFILES_ACTIVE=dev
# Diğer değişkenler config/application.yaml'dan okunur
```

### 5. Uygulamayı Başlatma

#### Maven ile:
```bash
# Dependencies'leri yükle
mvn clean install

# Uygulamayı başlat
mvn spring-boot:run
```

#### JAR dosyası ile:
```bash
# JAR dosyasını oluştur
mvn clean package

# JAR'ı çalıştır
java -jar target/reservation-http-api-0.0.1-SNAPSHOT.jar
```

### 6. Uygulama Erişim Bilgileri

Uygulama başarıyla başladıktan sonra:

#### API Endpoints:
- **Base URL**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api-docs`

#### Database Erişimi:
- **MongoDB**: `mongodb://admin:admin123@localhost:27017/reservation_db`
- **Mongo Express**: `http://localhost:8081`
  - Username: `admin`
  - Password: `admin123`

### 7. Health Check

Uygulama sağlığını kontrol edin:

```bash
curl http://localhost:8080/actuator/health
```

## 📊 Database Yapısı

### Otomatik Oluşturulan Collections:

1. **users** - Kullanıcılar
2. **owners** - İş sahipleri  
3. **businesses** - İşletmeler
4. **reservations** - Rezervasyonlar
5. **reservation_settings** - İşletme ayarları
6. **business_availability** - İşletme müsaitlik kuralları

### Otomatik Oluşturulan Index'ler:

#### Users Collection:
- `email` (unique)
- `gsm`

#### Owners Collection:
- `email` (unique)
- `gsm` 
- `ownerType`

#### Businesses Collection:
- `name` (text search)
- `owner.id`
- `location.googleId`

#### Reservations Collection:
- `user.id`
- `business.id`
- `reservationDate`
- `status`
- `business.id + reservationDate + timeSlot.startTime` (unique compound)

#### Reservation Settings Collection:
- `businessId` (unique)

#### Business Availability Collection:
- `businessId`
- `availabilityType`
- `dayOfWeek`
- `specificDate`

## 🔧 Troubleshooting

### MongoDB Bağlantı Sorunları:

```bash
# MongoDB container'ının çalıştığını kontrol edin
docker ps | grep mongodb

# MongoDB loglarını kontrol edin  
docker logs reservation-mongodb

# MongoDB'ye bağlanmayı test edin
mongosh mongodb://admin:admin123@localhost:27017/reservation_db
```

### Uygulama Başlatma Sorunları:

```bash
# Java versiyonunu kontrol edin
java -version

# Maven versiyonunu kontrol edin
mvn -version

# Portun kullanımda olup olmadığını kontrol edin
lsof -i :8080
```

### Index Oluşturma Sorunları:

Eğer otomatik index oluşturma çalışmıyorsa:

```bash
# MongoDB'ye bağlan
mongosh mongodb://admin:admin123@localhost:27017/reservation_db

# Init scriptini manuel çalıştır
load('mongodb-init.js')
```

## 🧪 Test Etme

### API Test:

```bash
# Swagger UI üzerinden test edin
open http://localhost:8080/swagger-ui.html

# Curl ile test edin
curl -X GET http://localhost:8080/api/users
```

### Database Test:

```bash
# Mongo Express üzerinden test edin
open http://localhost:8081

# Mongosh ile test edin
mongosh mongodb://admin:admin123@localhost:27017/reservation_db
db.users.find()
```

## 📝 Notlar

- **Otomatik Index Oluşturma**: Spring Boot entity'lerdeki `@Indexed` annotation'ları sayesinde otomatik olarak index'ler oluşturulur.
- **DDL Yok**: MongoDB NoSQL olduğu için DDL schema'ları yoktur. Collections ilk document insert edildiğinde otomatik oluşturulur.
- **Data Validation**: Entity validation'ları MongoDB'ye kaydetme öncesinde çalışır.
- **Connection Pooling**: Spring Boot otomatik olarak MongoDB connection pool'u yönetir.

## 🆘 Support

Sorun yaşarsanız:
1. Bu dokümandaki troubleshooting bölümünü kontrol edin
2. Application loglarını kontrol edin
3. MongoDB loglarını kontrol edin
4. Swagger UI'dan API'leri test edin 