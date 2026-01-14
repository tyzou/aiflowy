# 第一阶段：构建阶段
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# 复制 pom.xml 并下载依赖（利用 Docker 缓存）
COPY pom.xml .
COPY aiflowy-api/pom.xml aiflowy-api/
COPY aiflowy-commons/pom.xml aiflowy-commons/
COPY aiflowy-modules/pom.xml aiflowy-modules/
COPY aiflowy-starter/pom.xml aiflowy-starter/
COPY aiflowy-starter/aiflowy-starter-all/pom.xml aiflowy-starter/aiflowy-starter-all/

# 注意：这里需要复制所有模块的 pom.xml 才能正确解析依赖
# 如果模块很多，可能需要更精细的复制，但这里先简单处理
COPY . .

# 执行构建
RUN mvn clean package -DskipTests

# 第二阶段：运行阶段
FROM eclipse-temurin:17-jre

LABEL maintainer="Cennac <cennac@163.com>"

ARG VERSION=2.0.4
ARG SERVICE_NAME=aiflowy-starter-all
ARG SERVICE_PORT=8080

ENV VERSION ${VERSION}
ENV SERVICE_NAME ${SERVICE_NAME}
ENV SERVICE_PORT ${SERVICE_PORT}
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
ENV JAVA_OPTS=""
ENV TZ=Asia/Shanghai

WORKDIR /app

# 安装必要的字体和工具
RUN apt-get update && \
    apt-get install -y --no-install-recommends fonts-dejavu-core fontconfig && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && \
    rm -rf /var/lib/apt/lists/*

# 从构建阶段复制 jar 包
COPY --from=builder /build/aiflowy-starter/aiflowy-starter-all/target/${SERVICE_NAME}-*.jar app.jar

VOLUME /tmp
EXPOSE ${SERVICE_PORT}

ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar ./app.jar
