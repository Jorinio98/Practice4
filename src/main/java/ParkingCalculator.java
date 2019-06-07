
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ParkingCalculator {



    @DataProvider(parallel = true)
    public Object[][] dp() {
        return new Object[][]{
                {"Short-Term Parking","16:00","PM","3/28/2019","18:00","AM","3/30/2019","$ 52.00","(1 Days, 14 Hours, 0 Minutes)"},
                {"Economy Parking","16:00","PM","3/28/2019","18:00","AM","3/29/2019","$ 9.00","(0 Days, 14 Hours, 0 Minutes)"},
                {"Long-Term Surface Parking","17:00","AM","3/28/2019","18:00","AM","4/1/2019","$ 42.00","(4 Days, 1 Hours, 0 Minutes)"},
                {"Long-Term Garage Parking","12:00","AM","3/28/2019","10:00","PM","4/1/2019","$ 60.00","(4 Days, 22 Hours, 0 Minutes)"},
                {"Valet Parking","12:00","AM","3/28/2019","10:00","PM","4/4/2019","$ 240.00","(7 Days, 22 Hours, 0 Minutes)"},
                {"Economy Parking", "12:00", "AM", "10/15/2018", "12:00", "PM", "10/15/2018", "$ 9.00", "(0 Days, 12 Hours, 0 Minutes)"},


        };
    }

    @Test(dataProvider = "dp", threadPoolSize = 3)
    public void test(String typeParking, String initTime, String initHalf, String initDate, String finTime, String finHalf, String finDate, String calCost, String parkingDate) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Desktop\\Java Tascks\\Selenium\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.get("http://adam.goucher.ca/parkcalc/index.php");

//        WebElement chooseALot = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose a Lot')]]//select[@id='Lot']"));
//        Select chooseALotSelect = new Select(chooseALot);

        Select chooseALotSelect = new Select(driver.findElement(By.xpath("//tr[td[contains(text(),'Choose a Lot')]]//select[@id='Lot']")));
        chooseALotSelect.selectByVisibleText(typeParking);

        WebElement startTime = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose Entry Date and Time')]]//input[@name='EntryTime']"));
        List<WebElement> startDateAmPm = driver.findElements(By.xpath("//tr[td[contains(text(),'Choose Entry Date and Time')]]//input[@name='EntryTimeAMPM']"));
        WebElement startDate = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose Entry Date and Time')]]//input[@name='EntryDate']"));

        startTime.clear();
        startTime.sendKeys(initTime);
        selectRadioValue(startDateAmPm, initHalf);
        startDate.clear();
        startDate.sendKeys(initDate);


        WebElement endTime = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose Leaving Date and Time')]]//input[@name='ExitTime']"));
        List<WebElement> endDateAmPm = driver.findElements(By.xpath("//tr[td[contains(text(),'Choose Leaving Date and Time')]]//input[@name='ExitTimeAMPM']"));
        WebElement endDate = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose Leaving Date and Time')]]//input[@name='ExitDate']"));


        endTime.clear();
        endTime.sendKeys(finTime);
        selectRadioValue(endDateAmPm, finHalf);
        endDate.clear();
        endDate.sendKeys(finDate);


        WebElement submitBtn = driver.findElement(By.xpath("//input[@name='Submit' and @value='Calculate']"));

        submitBtn.click();

        Thread.sleep(1000);

        WebElement costValue = driver.findElement(By.xpath("//tr[contains(.,'COST')]/td//b[contains(text(),'$')]"));
        WebElement calculatedTime = driver.findElement(By.xpath("//tr[contains(.,'COST')]/td//b[contains(text(),'Days')]"));

        Assert.assertEquals(costValue.getText(),calCost);
        Assert.assertEquals(calculatedTime.getText().trim(), parkingDate);
    }

    public void selectRadioValue(List<WebElement> list, String selectValue){
        for(WebElement elem:list){
            String paramValue = elem.getAttribute("value");
            if (StringUtils.equals(selectValue,paramValue)){
                elem.click();
                return;
            }
        }
    }
}