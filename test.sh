set -e
javac -cp .:lib/hamcrest-core-1.3/junit-4.13.2.jar *.java
java -cp .:lib/hamcrest-core-1.3/junit-4.13.2.jar org.junit.runner.JUnitCore DocSearchTest