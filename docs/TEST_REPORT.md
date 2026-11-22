# Verification report

Verification on 2026-11-22 with OpenJDK 21, compiling sources with `javac --release 11` (Java 11 class-file target).

- **Compilation:** Java source and regression suite compiled successfully.
- **Compiler warnings:** No warnings reported by `javac --release 11 -Xlint:all` for the main sources.
- **Regression suite:** 14 groups passed; 19,862 assertions passed.
- **Demonstration:** `App` completed normally and printed all exercise examples.
- **Syntax checker:** Successfully checked `App.java`.
- **Maven:** A Maven executable was not available in the verification environment; the Maven lifecycle and GitHub Actions job were configured but not executed here.
- **Windows PowerShell:** The script was reviewed but not executed on Windows in this environment.

These tests cover important behavior, but do not prove that all possible errors are absent.
