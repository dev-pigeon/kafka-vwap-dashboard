FROM maven:3.9.5-eclipse-temurin-17

WORKDIR /kafka

COPY . .

RUN mvn clean install
CMD ["sh", "-c", "mvn exec:java -Dexec.mainClass=com.example.Producer & mvn exec:java -Dexec.mainClass=com.example.Consumer; wait"]
