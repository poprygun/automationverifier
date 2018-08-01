package io.microsamples.utilz;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@Slf4j
public class App {

    private static ChromeDriverService service;

    public static void main(String[] args) throws IOException {

        File driverFile = args.length > 0 ? new File(args[0]): new File("chromedriver");

        loadSeliniumService(driverFile);

        WebDriver driver = loadChromeDriver();

        driver.get("http://google.com/ncr");
        driver.findElement(By.name("q")).sendKeys("cheese" + Keys.ENTER);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement firstResult = wait.until(presenceOfElementLocated(By.cssSelector("h3>a")));
        if (firstResult.getText().toLowerCase().contains("cheese")) {
            log.info("Chrome driver functionality verified.  Search result --> {}", firstResult.getText());
            System.exit(0);
        }

        throw new RuntimeException("Driver configuration error.");
    }

    private static WebDriver loadChromeDriver() {
        ChromeOptions caps = new ChromeOptions();
        caps.setHeadless(true);
        return new RemoteWebDriver(service.getUrl(), caps);
    }

    private static void loadSeliniumService(File driverFile) throws IOException {
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(driverFile)
                .usingAnyFreePort()
                .build();
        service.start();
    }
}
