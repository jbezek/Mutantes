# Mutantes

Para el desarrollo de la API elegí Spring WebFlux y una base de datos MongoDB, de manera de tener una aplicación reactiva punta a punta para dar respuesta a la necesidad de soportar una carga alta de tráfico. Para cumplir con el code coverage mínimo de 80% se utilizó JaCoCo. Para el host de la API elegí Google Kubernetes Engine, más adelante en este documento se detallan los comandos ejecutados para poder levantar el cluster con los servicios de la API y la base Mongo. También se agrega en la raíz del proyecto un archivo docker-compose para poder levantar 3 contenedores con la API, la base Mongo (con un volumen para persistir los datos), y la documentación, para la que se utilizó Swagger.


## Descripción de la API por endpoint


#### GET /stats

Obtiene las estadísticas de los humanos verificados, y aquellos que fueron detectados como mutantes.


#### POST /mutant/

Verifica el ADN de un humano y lo guarda en Mongo. Se agregaron validaciones para chequear que el ADN se envie en el request y sea una matriz de NxN.


Se accede a la documentación detallada en Swagger ingresando en http://localhost:9000/ una vez ejecutado docker-compose.



## JUnit y JaCoCo

Se creó la clase MutantRestControllerTest para testear la API, utilizando la librería Mockito para mockear el repositorio, de manera de correr los tests sin afectar la base de datos.
Además se utiliza JaCoCo para realizar reportes con base en la métrica de code coverage, configurando en el pom.xml un límite mínimo de 80%. El resultado de los tests arroja un 91% de cobertura. El reporte se encuentra en /target/site/jacoco/index.html.



## Compilación y ejecución con Docker

Desde el directorio base del proyecto ejecutar docker-compose:

```bash
docker-compose up
```

con este comando se levantan los 3 contenedores con la API,  la base de datos y la documentación, mapeados a los puertos 8080, 27017 y 9000 respectivamente. 


## Hosting

Para hostear la API se eligió Google Kubernetes Engine. El servicio que expone la API se encuentra en http://35.199.175.204/. Los pasos para hostear la API fueron los siguientes:

Se creó un cluster.
Se corrieron los siguientes yaml para crear el volumen, el deploy a partir de la imagen mongo y el servicio visible en el cluster:

```bash
kubectl create -f mongodata-persistentvolumeclaim.yaml
kubectl create -f mongodb-deployment.yaml
kubectl create -f mongodb-service.yaml
```

La implementación de la aplicación se hizo utilizando las herramientas de línea de comandos gcloud y kubectl:

```bash
docker build -t gcr.io/mutantes-293021/mutants-ml-image:v1 . 

docker push gcr.io/mutantes-293021/mutants-ml-image:v1 

kubectl create deployment mutants-ml-image --image=gcr.io/mutantes-293021/mutants-ml-image:v1 

kubectl scale deployment mutants-ml-image --replicas=3  

kubectl autoscale deployment mutants-ml-image --cpu-percent=80 --min=1 --max=5 

kubectl expose deployment mutants-ml-image --name=mutants-ml-service --type=LoadBalancer --port 80 --target-port 8080
```


