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

Improvement:
Untuk sekarang belum banyak yang dapat disimpulkan terkait dengan peningkatan performance, rencana kedepan-nya akan dilakukan simulasi dengan Apache JMeter
agar dapat menampilkan lebih banyak insights terkait dengan perfromance dari service yang kita kerjakan dan peningkatan-peningkatan selanjutnya yang perlu dilakukan.
