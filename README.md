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