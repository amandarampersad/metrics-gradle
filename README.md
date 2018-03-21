# metrics-gradle
Gradle version of metrics project

REQUIREMENTS: JDK 8

To build and deploy follow these steps:
1. Navigate to root of project directory (where build.gradle sits) via cmd or Terminal
2. Run the command "./gradlew clean build"
3. After successful build, run the command "java -jar build/libs/metrics-1.0-SNAPSHOT.jar"
4. Navigate to "http://localhost:8080" to verify application is running - Whitelist Error page appears by default

Relevant classes:
1. All API calls are located in MetricController.java - API methods are commented with time and space complexity
2. Test cases are located in MetricContollerTestCase.java
