# Stage 1: Compile Java Source
FROM openjdk:17-jdk-slim AS builder

WORKDIR /build

# Copy the entire project
COPY . .

# Create classes directory
RUN mkdir -p flight-app-main/FlightApp/WEB-INF/classes

# Download Servlet API for compilation (Tomcat 10 uses Jakarta Servlet 5.0)
ADD https://repo1.maven.org/maven2/jakarta/servlet/jakarta.servlet-api/5.0.0/jakarta.servlet-api-5.0.0.jar /lib/servlet-api.jar

# Compile all Java files
RUN find flight-app-main/FlightApp/src -name "*.java" > sources.txt && \
    javac -d flight-app-main/FlightApp/WEB-INF/classes \
          -cp "/lib/servlet-api.jar:flight-app-main/FlightApp/WEB-INF/lib/*" \
          @sources.txt

# Stage 2: Run in Tomcat
FROM tomcat:10.1-jdk17

# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy our compiled app as the ROOT application
COPY --from=builder /build/flight-app-main/FlightApp /usr/local/tomcat/webapps/ROOT

# Render uses a dynamic port ($PORT). We'll update Tomcat's server.xml on startup.
EXPOSE 8080

CMD ["sh", "-c", "sed -i 's/port=\"8080\"/port=\"'${PORT:-8080}'\"/g' conf/server.xml && catalina.sh run"]
