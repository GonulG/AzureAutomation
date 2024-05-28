import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.awt.event.KeyEvent;

public class AzureAutomation {

    // DOSYA YOLU:
    String filePath = "C:\\Users\\qonul\\OneDrive\\Desktop\\OtomasyonKlasor\\OtomasyonDosya.docx";

    public static void main(String[] args) throws InterruptedException, AWTException {

        //CHROME

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        //Notifications Close
        options.addArguments("--incognito");
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-notifications");
        options.addArguments("--acceptInsecureCerts");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("chrome.switches","--disable-extensions");
        options.setExperimentalOption("useAutomationExtension", false);
        options.merge(desiredCapabilities);

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(options);

        uploadDocToAzure(driver);

    }

    private static void uploadDocToAzure(WebDriver driver) throws InterruptedException, AWTException {
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        driver.get("https://portal.azure.com");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));


        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("i0116")));
        email.sendKeys("useremail@cloudcan.com");

        WebElement nextBtn = driver.findElement(By.id("idSIButton9"));
        nextBtn.click();

        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("i0118")));
        password.sendKeys("userpassword");


        WebElement signInBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idSIButton9")));
        signInBtn.click();

        Thread.sleep(10000);


        WebElement rememberMeNoButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idBtn_Back")));
        rememberMeNoButton.click();


        WebElement storageAccountsButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[role='gridcell']:nth-child(2)")));
        storageAccountsButton.click();

        WebElement ccGptStorageLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//a[text()='cloudcangptstorage'])[2]")));
        ccGptStorageLink.click();

        WebElement dataStorageCollapseMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[aria-label='Veri depolama alanı grubunu aç/kapat']")));
        dataStorageCollapseMenu.click();

        WebElement containerOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-telemetryname='Menu-containersList']")));
        containerOption.click();

        WebElement ccContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='cloudcantest']")));
        ccContainer.click();


        WebElement ilkUploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[aria-label='Karşıya yükle']")));
        ilkUploadButton.click();

        Thread.sleep(5000);

        //IFRAME DEĞİŞTİ
        WebElement iframe2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("UploadBlobs.ReactView")));
        driver.switchTo().frame(iframe2);

        Thread.sleep(5000);

        // Dosyamın DRop edileceği Alan:
        WebElement dragDropBoxTarget = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[class='ms-Stack uploadBox-141']")));

        /*
        DRAG AND DROP ÇALIŞMAZSA EĞER UPLOAD BUTONU İLE DOSYA YÜKLEMEYİ DENEYECEĞİM:
        //WebElement uploadElementButton = driver.findElement(By.xpath("//button[text()='Dosyalara gözat']"));
        //uploadElementButton.click();
        //uploadElementButton.sendKeys(folderPath);
         */

        //FILE TÜRÜNDE JS INPUT CREATE ETTİM
        String jsCode = "var input = document.createElement('input');"
                + "input.type = 'file';"
                + "input.style.visibility='visible';"
                + "input.style.display = 'block';" // make the element block level so it has a size and is visible
                + "input.style.opacity = '1';"      // make the element transparent so it's not visible to the user
                + "input.style.height = '20px';"     // give the element a size
                + "input.style.width = '20px';"
                + "input.style.overflow = 'hidden';"// prevent the element from affecting layout
                + "document.body.appendChild(input);";
        ((JavascriptExecutor) driver).executeScript(jsCode);

        // DOSYAYI ÇEKTİĞİM YER OLAN   INPUT ELEMENTİNE FILE PATH'İNİ GÖNDERDİM
        WebElement inputFileSource = driver.findElement(By.cssSelector("input[type='file']"));
        inputFileSource.sendKeys("C:\\Users\\qonul\\OneDrive\\Desktop\\OtomasyonKlasor\\OtomasyonDosya.docx");

        Thread.sleep(3000);

        Actions actions = new Actions(driver);
        actions.dragAndDrop(inputFileSource,dragDropBoxTarget ).build().perform();

        Thread.sleep(3000);

        WebElement overwriteCheckBox = driver.findElement(By.id("checkbox-3"));
        overwriteCheckBox.click();

        WebElement uploadFileButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id__29")));
        uploadFileButton.click();
    }
}



