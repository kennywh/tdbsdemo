# Single-stage build using existing JAR file
FROM default-route-openshift-image-registry.apps-crc.testing/tdbs/openjdk-21:1.21-2

# Set working directory
WORKDIR /app

# Copy the pre-built JAR from target directory
COPY target/app.jar app.jar

# Expose port 8080 (default Spring Boot port)
EXPOSE 8080


# Run the application
ENTRYPOINT ["java","-jar","app.jar"] 
