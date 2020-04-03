package com.automation.tests;

import com.automation.utilities.BrowserUtils;
import com.automation.utilities.DriverFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HomeWork4 {
    private WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().version("79").setup();
//        driver = DriverFactory.createADriver("chrome");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(false);
        driver = new ChromeDriver(chromeOptions);
//        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void daysTest() {
        driver.get("http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCheckBox");
        List<WebElement> checkBoxes = driver.findElements(By.xpath("//td//span//input"));
        List<WebElement> checkBoxesText = driver.findElements(By.xpath("//label[contains(@id, 'gwt-debug-cwCheckBox')]"));
        Random rand = new Random();
        int cnt = 0;
        while (cnt < 3) {
            int randomDayNumber = rand.nextInt(checkBoxes.size());
            if (checkBoxes.get(randomDayNumber).isEnabled()) {
                checkBoxes.get(randomDayNumber).click();
                if (checkBoxesText.get(randomDayNumber).getText().equals("Friday")) {
                    cnt++;
                }
                System.out.println(checkBoxesText.get(randomDayNumber).getText());
                checkBoxes.get(randomDayNumber).click();
            }
        }
    }

    @Test
    public void todaysDateTest() {
        driver.get("http://practice.cybertekschool.com/dropdown");
        WebElement yearElement = driver.findElement(By.id("year"));
        WebElement monthElement = driver.findElement(By.id("month"));
        WebElement dayElement = driver.findElement(By.id("day"));

        Select yearSelect = new Select(yearElement);
        Select monthSelect = new Select(monthElement);
        Select daySelect = new Select(dayElement);

        String yearText = yearSelect.getFirstSelectedOption().getText();
        String monthText = monthSelect.getFirstSelectedOption().getText();
        String dayText = daySelect.getFirstSelectedOption().getText();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMMMd");
        Date todaysDate = new Date();
        String expectedDate = simpleDateFormat.format(todaysDate);
        String actualDate = yearText + monthText + dayText;

        Assert.assertEquals(actualDate, expectedDate);

    }

    @Test
    public void yearsMonthsDaysTest() {
        driver.get("http://practice.cybertekschool.com/dropdown");
        BrowserUtils.wait(2);
        WebElement year = driver.findElement(By.id("year"));
        WebElement month = driver.findElement(By.id("month"));
        WebElement day = driver.findElement(By.id("day"));

        Select yearSelect = new Select(year);
        Select monthSelect = new Select(month);
        Select daySelect = new Select(day);

        Random rand = new Random();
        int randomYear = rand.nextInt(yearSelect.getOptions().size());

        yearSelect.selectByIndex(randomYear);

        Map<String, String> dayMap = new LinkedHashMap<>();
        dayMap.put("January", "31");
        dayMap.put("February", "28");
        dayMap.put("March", "31");
        dayMap.put("April", "30");
        dayMap.put("May", "31");
        dayMap.put("June", "30");
        dayMap.put("July", "31");
        dayMap.put("August", "31");
        dayMap.put("September", "30");
        dayMap.put("October", "31");
        dayMap.put("November", "30");
        dayMap.put("December", "31");

        if (randomYear % 400 == 0 || (randomYear % 4 == 0 && randomYear % 100 != 0))
            dayMap.put("February", "29");

        int i = 0;
        for (String each : dayMap.keySet()) {
            monthSelect.selectByValue(String.valueOf(i));
            BrowserUtils.wait(1);
            List<WebElement> dayList = driver.findElements(By.xpath("//select[@id='day']//option"));
            String actual = String.valueOf(dayList.size());
            String expected = dayMap.get(each);
            Assert.assertEquals(actual, expected);
            i++;
        }
    }

    @Test
    public void departmentSortTest() {
        driver.get("https://www.amazon.com");
        WebElement dropDownLabel = driver.findElement(By.className("nav-search-label"));
        String actual = dropDownLabel.getText();
        Assert.assertEquals(actual, "All");

        WebElement departmentDropDown = driver.findElement(By.id("searchDropdownBox"));
        Select select = new Select(departmentDropDown);
        List<WebElement> list = select.getOptions();

        boolean isNotSorted = false;
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).getText().charAt(0) > list.get(i + 1).getText().charAt(0)) {
                isNotSorted = true;
                break;
            }
        }
        Assert.assertTrue(isNotSorted);
    }

    @Test
    public void mainDepartmentsTest() {
        driver.get("https://www.amazon.com/gp/site-directory");
//        BrowserUtils.wait(2);
        WebElement departmentsDropdown = driver.findElement(By.id("searchDropdownBox"));
        Select departmentSelect = new Select(departmentsDropdown);
        List<WebElement> departmentList = departmentSelect.getOptions();
        List<String> departmentListText = new ArrayList<>();
        for (WebElement webElement : departmentList) {
            departmentListText.add(webElement.getText());
        }
        List<WebElement> mainDepartmentList = driver.findElements(By.xpath("//h2"));
        List<String> mainDepartmentText = new ArrayList<>();
        for (WebElement webElement : mainDepartmentList) {
            mainDepartmentText.add(webElement.getText());
        }
        Assert.assertTrue(departmentListText.contains(mainDepartmentText));
    }

    @Test
    public void linksTest() {
        driver.get("https://www.w3schools.com/");
//        BrowserUtils.wait(3);
        List<WebElement> links = driver.findElements(By.tagName("a"));
        for (WebElement each : links) {
            if (each.isDisplayed()) {
                System.out.println(each.getText());
                System.out.println(each.getAttribute("href"));
            }
        }
    }

    @Test
    public void validLinksTest() throws Exception {
        driver.get("https://www.selenium.dev/documentation/en/");
//        BrowserUtils.wait(2);
        List<WebElement> links = driver.findElements(By.tagName("a"));
        for (WebElement each : links) {
            String URLText = each.getAttribute("href");

            try {
                URL url = new URL(URLText);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.connect();
                Assert.assertEquals(httpURLConnection.getResponseCode(), 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void cartTest() {
        driver.get("https://amazon.com");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("wooden spoon", Keys.ENTER);
        List<WebElement> productList = driver.findElements(By.xpath("//span[@class='a-price']/span[@class='a-offscreen']"));

        Random rand = new Random();
        int randomNumber = rand.nextInt(productList.size());
        if (randomNumber == 0)
            randomNumber = 1;

        WebElement randomProductNameElement = driver.findElement(By.xpath("(//span[@class='a-price']//*[@class='a-offscreen'])[" + randomNumber + "]/../../../../../..//*[@class='a-size-base-plus a-color-base a-text-normal']"));
        String randomProductName = randomProductNameElement.getText();

        String randomProductPrice = "$" + driver.findElement(By.xpath("(//span[@class='a-price']/span[2]/span[2])[" + randomNumber + "]")).getText() +
                "." + driver.findElement(By.xpath("(//span[@class='a-price-fraction'])[" + randomNumber + "]")).getText();
        randomProductNameElement.click();
        BrowserUtils.wait(3);

        Assert.assertEquals(driver.findElement(By.xpath("//span[@id='a-autoid-0-announce']//span[@class='a-dropdown-prompt']")).getText(), "1");
        Assert.assertEquals(driver.findElement(By.id("productTitle")).getText().trim(), randomProductName);
        Assert.assertEquals(driver.findElement(By.id("price_inside_buybox")).getText().trim(), randomProductPrice);
        Assert.assertTrue(driver.findElement(By.id("add-to-cart-button")).isDisplayed());
    }

    @Test
    public void primeTest() {
        driver.get("https://amazon.com");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("wooden spoon", Keys.ENTER);

        String productName = driver.findElement(By.xpath("(//*[@class='a-icon a-icon-prime a-icon-medium' and @aria-label='Amazon Prime']/../../../../../..//span[@class='a-size-base-plus a-color-base a-text-normal'])[1]")).getText();
        WebElement checkBox = driver.findElement(By.xpath("(//i[@class='a-icon a-icon-checkbox'])[1]"));
        checkBox.click();
        String productNameAfterCheckBox = driver.findElement(By.xpath("(//*[@class='a-icon a-icon-prime a-icon-medium' and @aria-label='Amazon Prime']/../../../../../..//span[@class='a-size-base-plus a-color-base a-text-normal'])[1]")).getText();

        Assert.assertEquals(productName, productNameAfterCheckBox);

        List<WebElement> brands = driver.findElements(By.xpath("//li[contains(@id, 'p_89')]//div[@class='a-checkbox a-checkbox-fancy s-navigation-checkbox aok-float-left']"));

        brands.get(brands.size() - 1).click();

        String productNameAfterBrand = driver.findElement(By.xpath("(//*[@class='a-icon a-icon-prime a-icon-medium' and @aria-label='Amazon Prime']/../../../../../..//span[@class='a-size-base-plus a-color-base a-text-normal'])[1]")).getText();

        Assert.assertNotEquals(productNameAfterCheckBox, productNameAfterBrand);


    }

    @Test
    public void moreSpoonsTest(){
        driver.get("https://amazon.com");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("wooden spoon", Keys.ENTER);
        List<WebElement> brands = driver.findElements(By.xpath("//li[contains(@id, 'p_89')]//span[@class='a-size-base a-color-base']"));
        List<String> brandsText = new ArrayList<>();
        for (WebElement webElement : brands) {
            brandsText.add(webElement.getText());
        }
        WebElement checkBox = driver.findElement(By.xpath("(//i[@class='a-icon a-icon-checkbox'])[1]"));
        checkBox.click();
        List<WebElement> brandsAfterPrime = driver.findElements(By.xpath("//li[contains(@id, 'p_89')]//span[@class='a-size-base a-color-base']"));
        List<String> brandsAfterPrimeText = new ArrayList<>();
        for (WebElement webElement : brandsAfterPrime) {
            brandsAfterPrimeText.add(webElement.getText());
        }
        Assert.assertEquals(brandsAfterPrimeText, brandsText);
    }

    @Test
    public void cheapSpoonsTest(){
        // I don't see Under $25 option available
    }

    @AfterMethod
    public void teardown() {
//         if webDriver object is alive
        if (driver != null) {
            // close the browser, close session
            driver.quit();
            // destroy webDriver object for sure
            driver = null;
        }
    }

}
