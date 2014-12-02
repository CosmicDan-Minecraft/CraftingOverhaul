:: IDEA doesn't want to copy resources to the same classpath entry as the compiled 
:: sources when running, but insists on them going in their own classpath entry.
:: Forge does not like this. Dodgy script to copy missing resources (and lang assets)
:: to the same classpath entry as the compiled source.

:: equivalent of linux 'cd dirname'
CD %~dp0
:: copy resources
xcopy src\main\resources build\classes\main /E /I /Y
:: copy lang assets
xcopy src\main\java\assets build\classes\main\assets /E /I /Y
