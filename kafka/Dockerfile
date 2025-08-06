FROM maven:3.9.5-eclipse-temurin-17

WORKDIR /kafka

COPY . .


RUN mvn clean install
CMD ["sh", "-c", "mvn exec:java -Dexec.mainClass=StreamProducer -Dexec.args=5 & mvn exec:java -Dexec.mainClass=StreamConsumer; wait"]
