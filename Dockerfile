# Single-stage build using existing JAR file
FROM default-route-openshift-image-registry.apps.ocp-dev.lcsd.hksarg/tdbs-uat/openjdk-21:latest

# Set working directory
WORKDIR /app

# Copy the pre-built JAR from target directory
COPY target/*.jar app.jar

# Expose port 8080 (default Spring Boot port)
EXPOSE 8080


# Run the application
ENTRYPOINT ["java","-jar","app.jar"] 