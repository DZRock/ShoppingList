FROM bellsoft/liberica-openjdk-alpine:latest

WORKDIR app
COPY target/scala-2.13/ShoppingListBot.jar app.jar

#ENTRYPOINT ["java", "-jar", "app.jar", "$TOKEN"]
CMD java -jar app.jar $TOKEN