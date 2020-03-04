
# FROM golang:1.12-stretch AS gobuilder
FROM golang:1.14.0-alpine AS gobuilder

WORKDIR /go/src/github.com/bygui86/helloworld-service-go
COPY . .

ENV GO111MODULE=on
ENV CGO_ENABLED=0
ENV GOOS=linux

RUN go build -a -installsuffix cgo -o /bin/app .

# ---

FROM alpine

RUN apk update --no-cache
RUN apk add --no-cache bash
RUN apk add --no-cache curl

WORKDIR /usr/bin/
COPY health-check.sh /usr/bin/health-check.sh
RUN chmod +x /usr/bin/health-check.sh

COPY --from=gobuilder /bin/app .

EXPOSE 8080
EXPOSE 8090
EXPOSE 8091

USER 1001

ENTRYPOINT "/usr/bin/app"
