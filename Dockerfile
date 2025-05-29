# Single-stage build using existing JAR file
FROM image-registry.openshift-image-registry.svc:5000/tdbs-uat/openjdk-21:latest

# Set working directory
WORKDIR /app

# Copy the pre-built JAR from target directory
COPY target/*.jar app.jar

# Expose port 8080 (default Spring Boot port)
EXPOSE 8080

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 