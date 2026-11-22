$ErrorActionPreference = "Stop"
$root = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$output = Join-Path $root ".build"
New-Item -ItemType Directory -Path $output -Force | Out-Null
$main = @(Get-ChildItem (Join-Path $root "src/main/java") -Filter *.java -Recurse | ForEach-Object FullName)
if ($main.Count -eq 0) { throw "No Java source files found." }
& javac --release 11 -encoding UTF-8 -d $output @main
if ($LASTEXITCODE -ne 0) { throw "Main source compilation failed." }
$tests = @(Get-ChildItem (Join-Path $root "src/test/java") -Filter *.java -Recurse | ForEach-Object FullName)
& javac --release 11 -encoding UTF-8 -cp $output -d $output @tests
if ($LASTEXITCODE -ne 0) { throw "Test source compilation failed." }
& java -cp $output datastructures.RegressionSuite
if ($LASTEXITCODE -ne 0) { throw "Regression tests failed." }
