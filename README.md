### User Subscriptions Management Service
##### Author:
1. Muh. Kemal lathif Galih Putra (2206081225)
2. Agastya Kenzo Nayandra - 2006535905

##### Design Pattern:
Decorator Design Pattern

Design Pattern ini digunakan dalam implementasi pricing strategy atas
perbedaan subscription plan yang ada. Dengan menggunakan design pattern ini, kita dapat dengan mudah menambahkan plan baru tanpa harus mengubah code yang sudah ada.

#### Software Architecure
Microservices

Software Architecture yang kami gunakan adalah microservices sesuai dengan beberapa
service yang dapat dikategorikan sendiri sendiri. Dengan menggunakan microservices, kita dapat dengan mudah mengelola service yang ada tanpa harus mengubah service yang lain.

Kami juga akan menggunakan pendekatan Restful API dalam mengimplementasikan service ini. Dan API gateway untuk client bisa berinteraksi dengan service yang ada.

#### Highlevel Network Design
Restful API

Kami akan menggunakan Restful API dalam mengimplementasikan service ini. 
Dengan menggunakan Restful API, kita dapat dengan mudah mengelola service yang ada dan client dapat berinteraksi dengan service yang ada.
Untuk saya (Muh. Kemal Lathif Galih Putra, akan mengerjakan fitur riwayat subscription box dan administrasi langganan subs box), dalam kasus ini
saya akan menggunakan Restful API untuk mengimplementasikan service ini. Seperti ketika mengambil data riwayat subscription box, mengambil data administrasi langganan subs box, dan lain-lain.

Saya juga akan menggunakan restful api utuk melakukan filtering dalam mendapatkan riwayat subs box dan mengacc permintaan langganan subs box yang ada.
Terima kasih.

#### Monitoring (Lokal)
Monitoring dilakukan dengan Prometheus untuk scrapping data dan Grafana untuk menampilkan data dari Service kami.

metrics yang digunakan adalah:
- CPU Usage: Menampilkan penggunaan CPU
- JVM Thread Count: Menampikan Jumlah Thread yang sedang berjalan
- HTTP Request Duration: Menampilkan waktu rata-rata waktu yang dibutuhkan untuk memproses HTTP Request
- HTTP Request Error: Menampilkan jumlah error yang terjadi pada HTTP Request


Screenshot:
![03FC1ABD-F406-4DA9-9E55-26BDE4188C66_1_201_a](https://github.com/AdproA11/subscription-management-service/assets/52792716/fb898f0f-e595-414f-b93a-0eb7c3cc5b62)

#### Profiling setup
Profilling dilakukan dengan memanfaatkan Prometheus untuk mendapatkan logs dan metrics dari service kami. 

Disini kami mencoba untuk melakukan improvement pada fungsi getAll() pada SubscriptionService untuk mendatkan Box dari service Box-item-management yang dikerjakan oleh Mario. Namun karena kami belum dapat menghubungkan antara satu service dengan lainya, kami mencoba untuk mensimulasikan dengan membuat replika service Box-item-management secara lokal.

Disini kami melakukan komparasi dengan membuat dua fungsi untuk getAll(). 
- Di fungsi pertama, getAll() dilakukan secara synchronous
- Di fungsi kedua, getAll() dilakukan secara asynchronous dengan memanfaatkan CompletableFuture dari java
- Kami berharap dengan implementasi getAll() secara asynchronous, akan terdapat peningkatan speed up saat service kami mencoba untuk me-retreive Box dari Service Box-item-management.
- Aplikasi akan berjalan pada port berikut

```
port dari subscription-management-service = 8080
port dari box-item-management-service = 8081
```

- Berikut adalah contoh dari API yang mereturn JSON dari http://localhost:8081/api/box/all
  
```
[{"id":1,"name":"Real Madrid Box","description":"Random Real Madrid Merchandise","price":250.0,"items":[{"id":1,"name":"Real Madrid Home 2023/2024"},{"id":2,"name":"Real Madrid Away 2023/2024"}]},{"id":2,"name":"Manchester United Box","description":"Random Manchester United Merchandise","price":500.0,"items":[{"id":3,"name":"Manchester United Home 2023/2024"},{"id":4,"name":"Manchester United Away 2023/2024"}]},{"id":3,"name":"Barcelona Box","description":"Random Barcelona Merchandise","price":750.0,"items":[{"id":5,"name":"Barcelona Home 2023/2024"},{"id":6,"name":"Barcelona Away 2023/2024"}]}]
```
- Kode pada Service & Controller
  
```
# Di SubscrptionService.java

    public List<SubscriptionBox> getAllBoxes() {
        String url = "http://localhost:8081/api/box/all"; //fetch boxes from box-item-services
        ResponseEntity<List<SubscriptionBox>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<SubscriptionBox> boxes = response.getBody();
        if (boxes != null) {
            boxRepo.saveAll(boxes);
        }
        return boxes;
    }

    @Async
    public CompletableFuture<List<SubscriptionBox>> getAllBoxesAsync() {
        String url = "http://localhost:8081/api/box/all";
        ResponseEntity<List<SubscriptionBox>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<SubscriptionBox> boxes = response.getBody();
        if (boxes != null) {
            boxRepo.saveAll(boxes);
        }
        return CompletableFuture.completedFuture(boxes);
    }

# Di SubscriptionController.java

    @GetMapping("/all")
    public ResponseEntity<List<SubscriptionBox>> getAllSubscriptionBoxes() {
        List<SubscriptionBox> boxes = subscriptionService.getAllBoxes();
        return ResponseEntity.ok(boxes);
    }

    @GetMapping("/all-async")
    public CompletableFuture<ResponseEntity<List<SubscriptionBox>>> getAllSubscriptionBoxesAsync() {
        return subscriptionService.getAllBoxesAsync()
                .thenApply(ResponseEntity::ok);
    }
```
#### Proses profiling
- Service subscription-management-service & box-item-management-service akan di run secara lokal 
- Dilakukan akses ke endpoint http://localhost:8080/api/subscriptions/all & http://localhost:8080/api/subscriptions/all-async yang kurang lebih akan mereturn JSON yang sama seperti pada endpoint http://localhost:8081/api/box/all tadi diawal.
- Pengecekan dilakukan dengan memanfaatkan prometheus pada endpoint http://localhost:8080/actuator/prometheus untuk menganalisis lebih dalam metrics yang berkaitan dengan http_server_requests_seconds.
- Akan dilakukan akses API masing-masing endpoint http://localhost:8080/api/subscriptions/all & http://localhost:8080/api/subscriptions/all-async tadi sebanyak 3 kali dan akan dilakukan sebanyak 3 kali percobaan juga untuk mendapatkan hasil yang lebih akurat.

#### Hasil Profiling
- first attempt
```
# TYPE http_server_requests_seconds summary
http_server_requests_seconds_count{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all-async",} 3.0
http_server_requests_seconds_sum{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all-async",} 0.113312375
http_server_requests_seconds_count{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all",} 3.0
http_server_requests_seconds_sum{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all",} 0.456437583
# HELP http_server_requests_seconds_max  
# TYPE http_server_requests_seconds_max gauge
http_server_requests_seconds_max{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all-async",} 0.05675125
http_server_requests_seconds_max{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all",} 0.406349833
```

- second attempt
```
# TYPE http_server_requests_seconds summary
http_server_requests_seconds_count{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all-async",} 3.0
http_server_requests_seconds_sum{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all-async",} 0.160323333
http_server_requests_seconds_count{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all",} 3.0
http_server_requests_seconds_sum{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all",} 0.248590959
# HELP http_server_requests_seconds_max  
# TYPE http_server_requests_seconds_max gauge
http_server_requests_seconds_max{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all-async",} 0.105246625
http_server_requests_seconds_max{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all",} 0.201006292
```

- third attempt

```
# TYPE http_server_requests_seconds summary
http_server_requests_seconds_count{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all-async",} 3.0
http_server_requests_seconds_sum{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all-async",} 0.102669793
http_server_requests_seconds_count{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all",} 3.0
http_server_requests_seconds_sum{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all",} 0.323081459
# HELP http_server_requests_seconds_max  
# TYPE http_server_requests_seconds_max gauge
http_server_requests_seconds_max{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all-async",} 0.038492792
http_server_requests_seconds_max{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/api/subscriptions/all",} 0.275689292
```

#### Kesimpulan profiling
- Dari ketiga percobaan, sepertinya operasi getAll() secara asynchronus dengan memanfaatkan CompletableFuture lebih cepat 1,5 hingga 4 kali lebih cepat dibandingkan dengan operasi getAll() yang dilakukan secara synchronous
- Sepertinya hal ini terjadi karena dengan mengimplementasi CompletableFuture secara asynchronous, operasi untuk I/O ke API/database akan dilakukan pada thread yang berbeda dan main thread yang tadi hanya digunakan khusus untuk melakukan pemanggilan http request.
- Hal ini tentunya akan lebih cepat dan efisien dibanding dengan pemanggilan getAll() biasa secara synchronous contohnya yang dimana main thread akan menghandle http request dan menghandle proses I/O ke API/database secara bersamaan, sehingga main thread akan diblokir hingga operasi I/O selesai terlebih dahulu.

