# DelayZero – Backend

Repositorio del **backend del proyecto DelayZero**, desarrollado por el **Grupo 9** durante el **Hackathon ONE II**. Este servicio expone una **API REST** para predecir la probabilidad de retraso de un vuelo a partir de datos históricos.


## 1. Cómo ejecutar el proyecto

### Requisitos previos

* Java 17 o superior
* Maven 3.8+
* Git

### Pasos de ejecución

```bash
# Clonar el repositorio
git clone https://github.com/ORG/delayzero-backend.git
cd delayzero-backend

# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

Por defecto, la API quedará disponible en:

```
http://localhost:8080
```

---

## 2. Dependencias y versiones

Principales herramientas y tecnologías utilizadas:

* **Java:** 17
* **Spring Boot:** 3.x
* **Maven:** 3.8+
* **Spring Web:** para la creación de la API REST
* **Spring Validation:** validación de datos de entrada
* **JUnit 5 / Mockito:** pruebas unitarias

*(Las versiones exactas pueden consultarse en el archivo `pom.xml`)*



## 3. Ejemplo de petición y respuesta

### Petición

**Endpoint:** `POST /api/v1/prediction`

```json
{
"aerolinea": "AZ",
"origen": "GIG",
"destino": "GRU",
"fecha_partida": "2025-11-10T14:30:00",
"distancia_km": 350
}
```

### Respuesta

```json
{
"prevision": "Retrasado",
"probabilidad": 0.78
}
```



## 4. Dataset utilizado

El modelo de predicción se basa en **datos históricos de vuelos**, que incluyen variables como:

* Aerolínea
* Aeropuerto de origen y destino
* Hora de salida
* Día de la semana
* Estado del vuelo (a tiempo / retrasado)

El dataset puede provenir de fuentes públicas como:

Ejemplo de referencia:

> [https://www.kaggle.com/datasets/usdot/flight-delays](https://www.kaggle.com/datasets/usdot/flight-delays)



## 5. Notas finales

Este proyecto fue desarrollado con fines **educativos y demostrativos**, y sirve como base para futuras mejoras como integración con modelos más avanzados, despliegue en la nube y monitoreo en tiempo real.

