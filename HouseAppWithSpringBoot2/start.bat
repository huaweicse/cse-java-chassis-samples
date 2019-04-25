set CURRENT_DIR=%cd%

cd %CURRENT_DIR%\account-service\target
start java -jar account-service-0.0.1-SNAPSHOT.jar

ping 100.1.1.2 -n 1 -w 10000 > nul

cd %CURRENT_DIR%\customer-service\target
start java -jar customer-service-0.0.1-SNAPSHOT.jar

ping 100.1.1.2 -n 1 -w 10000 > nul

cd %CURRENT_DIR%\customer-website\target
start java -jar customer-website-0.0.1-SNAPSHOT.jar

ping 100.1.1.2 -n 1 -w 10000 > nul

cd %CURRENT_DIR%\product-service\target
start java -jar product-service-0.0.1-SNAPSHOT.jar

ping 100.1.1.2 -n 1 -w 10000 > nul

cd %CURRENT_DIR%\user-service\target
start java -jar user-service-0.0.1-SNAPSHOT.jar

ping 100.1.1.2 -n 1 -w 10000 > nul

cd %CURRENT_DIR%\edge-service\target
start java -jar edge-service-0.0.1-SNAPSHOT.jar

cd %CURRENT_DIR%
