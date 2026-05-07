$base   = "c:\Users\N E\OneDrive\Desktop\AJAVA Project\ajava\flight-app-main\FlightApp"
$tcZip  = "c:\Users\N E\OneDrive\Desktop\AJAVA Project\ajava\tomcat.zip"
$tcDest = "c:\Users\N E\OneDrive\Desktop\AJAVA Project\ajava\tomcat"

# Step 1: Download Apache Tomcat 10.1.54
Write-Host "=== Downloading Apache Tomcat 10.1.54 ===" -ForegroundColor Cyan
$tcUrl = "https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.54/bin/apache-tomcat-10.1.54-windows-x64.zip"
(New-Object Net.WebClient).DownloadFile($tcUrl, $tcZip)
Write-Host "Tomcat downloaded OK" -ForegroundColor Green

# Step 2: Extract Tomcat
Write-Host "=== Extracting Tomcat ===" -ForegroundColor Cyan
if (Test-Path $tcDest) { Remove-Item $tcDest -Recurse -Force }
Expand-Archive -Path $tcZip -DestinationPath $tcDest -Force
Write-Host "Tomcat extracted" -ForegroundColor Green

# Step 3: Find Tomcat home folder
$tomcatHome = (Get-ChildItem $tcDest | Where-Object { $_.PSIsContainer } | Select-Object -First 1).FullName
Write-Host "Tomcat home: $tomcatHome" -ForegroundColor Green

# Step 4: Deploy FlightApp into Tomcat webapps
Write-Host "=== Deploying FlightApp ===" -ForegroundColor Cyan
$webapps = "$tomcatHome\webapps\FlightApp"
if (Test-Path $webapps) { Remove-Item $webapps -Recurse -Force }
Copy-Item -Path $base -Destination $webapps -Recurse -Force
Write-Host "Deployed to: $webapps" -ForegroundColor Green

# Step 5: Start Tomcat
Write-Host "=== Starting Tomcat ===" -ForegroundColor Cyan
$startup = "$tomcatHome\bin\startup.bat"
Start-Process "cmd.exe" -ArgumentList "/c `"$startup`"" -WorkingDirectory "$tomcatHome\bin"
Start-Sleep -Seconds 4

Write-Host ""
Write-Host "============================" -ForegroundColor Yellow
Write-Host " Tomcat started!" -ForegroundColor Green
Write-Host " Open: http://localhost:8080/FlightApp/" -ForegroundColor Yellow
Write-Host "============================" -ForegroundColor Yellow
