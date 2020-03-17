
# Istio by example

Steps tutorial and explanation: http://bit.ly/istiolab-ocp-gdoc


## 1. Clone
```
cd applications/
```

## 2. Build & Docker
```
export DOCKER_REGISTRY_PASSWORD=...
export DOCKER_REGISTRY_USER=...
echo "$DOCKER_REGISTRY_PASSWORD" | docker login -u "$DOCKER_REGISTRY_USER" --password-stdin
```
```
cd guestbook-service/
./mvnw clean package
docker build . -t $DOCKER_REGISTRY_USER/guestbook-service:latest
docker push $DOCKER_REGISTRY_USER/guestbook-service:latest
cd -
```
```
cd guestbook-ui/
./mvnw clean package
docker build . -t $DOCKER_REGISTRY_USER/guestbook-ui:1.0
docker push $DOCKER_REGISTRY_USER/guestbook-ui:1.0
cd -
```
`Change background color in 'guestbook-ui/src/main/resources/templates/index.html'`
```
cd guestbook-ui/
./mvnw clean package
docker build . -t $DOCKER_REGISTRY_USER/guestbook-ui:2.0
docker push $DOCKER_REGISTRY_USER/guestbook-ui:2.0
cd -
```
```
cd helloworld-service-java/
./mvnw clean package
docker build . -t $DOCKER_REGISTRY_USER/helloworld-service-java:latest
docker push $DOCKER_REGISTRY_USER/helloworld-service-java:latest
cd -
```
```
cd helloworld-service-go/
docker build . -t $DOCKER_REGISTRY_USER/helloworld-service-go:latest
docker push $DOCKER_REGISTRY_USER/helloworld-service-go:latest
cd -
```

## 3. Kubernetes
### v1
```
kubectl apply -f kubernetes-v1/permissive/
```
### v2
```
kubectl apply -f kubernetes-v2/permissive/
```
