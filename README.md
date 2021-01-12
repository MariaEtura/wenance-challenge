# Wenance Challenge by Maria Etura

El ejercicio enviado constó de realizar las siguientes apis:
| Ref | API | Descripción |
| -----| ------ | ------ |
|1.a|GET `/btcars/getPrice/{date}`| {date} es la fecha de referencia para conocer el precio del btc en pesos argentinos en la dicha fecha, el formato debe ser "yyyy-MM-dd HH:mm:ss"|
|1.b|POST `/btcars/averagePrice`|Conocer el promedio de precio entre dos timestamps ingresados por parametros. Body: `{"initDate": "yyyy-MM-dd HH:mm:ss"},"endDate": "yyyy-MM-dd HH:mm:ss"}`|
|1.c|POST `/btcars/searchPrices`| Devolver en forma paginada los datos almacenados con o sin filtro de timestamp. Body: `{"initDate": "yyyy-MM-dd HH:mm:ss"},"endDate": "yyyy-MM-dd HH:mm:ss", page: 1, size: 10}`|

# Consulta a la API REST de buenbit

  - Se realizó un cron que se ejecuta cada 10 segundos, para la llamada se utilizó restTemplate

# Base de datos

- Utilice mongoDB y para el test es una base de datos de test particular

# Como ejecutar la app?

- Al estar dockerizada basta con ejecutar el comando
```docker-compose up --build``` y queda expuesto en el puerto 8080 con una bd mongo dockerizada
