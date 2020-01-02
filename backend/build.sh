#!/usr/bin/env bash
mvnw clean -Pnative
mvnw package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t greenknight15/non:v2 .
docker push greenknight15/non:v2

# mvnw clean -Pnativemvnw clean -Pnative
# mvnw compile quarkus:dev -Ddebug=5006

# mvnw package -Pnative
# mvnw verify -Pnative
# mvnw package -Pnative -Dquarkus.native.container-build=true

# docker build -f src/main/docker/Dockerfile.native -t greenknight15/non .
# docker build -f src/main/docker/Dockerfile.multistage -t greenknight15/non .
# docker run -i --rm -p 8080:8080 greenknight15/non

# local
# docker build -f src/main/docker/Dockerfile.native -t greenknight15/non .

# oc new-build --binary --name=non-backend -l app=non-backend
# oc patch bc/non-backend -p '{"spec":{"strategy":{"dockerStrategy":{"dockerfilePath":"src/main/docker/Dockerfile.native"}}}}'
# oc start-build non-backend --from-dir=. --follow
# oc new-app --image-stream=greenknight15/non:latest
# oc expose service non-backend

# oc get route
# oc apply -f src/main/resources/k8s

# https://jjbeto.com/blog/2019/12/07/build-native-app-with-quarkus/
# https://www.baeldung.com/quarkus-io
# https://quarkus.io/guides/maven-tooling
# https://quarkus.io/guides/building-native-image#producing-a-native-executable
# https://github.com/quarkusio/quarkus-quickstarts
# https://quarkus.io/guides/rest-json
# https://quarkus.io/guides/mongodb