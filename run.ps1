param(
    [switch]$CompileOnly
)

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$srcDir = Join-Path $projectRoot "src"
$outDir = Join-Path $projectRoot "out"

$javaFiles = Get-ChildItem -Recurse -Filter *.java -Path $srcDir
if (-not $javaFiles) {
    Write-Error "No Java files found under $srcDir"
    exit 1
}

if (-not (Test-Path $outDir)) {
    New-Item -ItemType Directory -Path $outDir | Out-Null
}

javac -d $outDir $javaFiles.FullName
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

if ($CompileOnly) {
    Write-Host "Compilation successful."
    exit 0
}

java -cp $outDir com.schoolmgmt.App
