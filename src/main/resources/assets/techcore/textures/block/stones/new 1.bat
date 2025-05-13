@echo off
setlocal enabledelayedexpansion

REM Recursively rename all files that start with 'bricks_' and end with '.png'
for /r %%f in (bricks_*.png) do (
    set "filename=%%~nxf"
    setlocal enabledelayedexpansion

    REM Extract parts before and after the underscore
    for /f "tokens=1,2 delims=_" %%a in ("!filename:.png=!") do (
        set "newname=%%b_%%a.png"
        echo Renaming: !filename! → !newname!
        ren "%%f" "!newname!"
    )

    endlocal
)
