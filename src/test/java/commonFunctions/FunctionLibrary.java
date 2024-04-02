package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary 
{
public static WebDriver driver;
public static Properties conpro;
//method for launching browser
public static WebDriver startBrowser() throws Throwable
{
	conpro =new Properties();
	//load property file
	conpro.load(new FileInputStream("./PropertiesFile/Environment.Properties"));
	if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
	{
		driver =new ChromeDriver();
		driver.manage().window().maximize();
	}
	else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
	{
		driver =new FirefoxDriver();
	}
	else
	{
		Reporter.log("Browser value is not matching",true);
	}
	return driver;
	
}
//method for launching url
public static void openUrl()
{
	driver.get(conpro.getProperty("Url"));
}
//method for wait for any webelement
public static void waitForElement(String LocatorType,String LocatorValue,
		String TestData)
{
	WebDriverWait mywait =new WebDriverWait(driver,Duration.ofSeconds(Integer.parseInt(TestData)));
	if(LocatorType.equalsIgnoreCase("name"))
	{
//wait until element is visible
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
	}
}
//method for textboxes
public static void typeAction(String LocatorType,String LocatorValue,
		String TestData)
{
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).clear();
		driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).clear();
		driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).clear();
		driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
	}	
}
//method for buttons,radiobuttons,checkboxes,links and images
public static void clickAction(String LocatorType,String LocatorValue)
{
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).click();
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).click();
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
	}
}
//method for validate title
	public static void validateTitle(String Expected_title)
	{
		String Actual_title = driver.getTitle();
		try {
		Assert.assertEquals(Actual_title, Expected_title,"Title is not matching");
		}catch(AssertionError a)
		{
			System.out.println(a.getMessage());
		}
	}
//method for closing browser
	public static void closeBrowser()
	{
		driver.quit();
	}
	//method for generating Date
	public static String generateDate()
	{
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("YYYY_MM_DD hh_mm");
		return df.format(date);
	}
//method for listboxes
public static void dropDownAction(String LocatorType,String LocatorValue,
		String TestData)
{
	if(LocatorType.equalsIgnoreCase("id"))
	{
		int value =Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.id(LocatorValue)));
		element.selectByIndex(value);
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		int value =Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.name(LocatorValue)));
		element.selectByIndex(value);
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		int value =Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
		element.selectByIndex(value);
	}
}
//method for capturing stock number into notepad
public static void capturestock(String LocatorType,String LocatorValue) throws Throwable
{
	String stock_Num="";
	if(LocatorType.equalsIgnoreCase("id"))
	{
		stock_Num = driver.findElement(By.id(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		stock_Num = driver.findElement(By.name(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		stock_Num = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
	}
	FileWriter fw = new FileWriter("./CaptureData/stockNumber.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(stock_Num);
	bw.flush();
	bw.close();
	
}
//method for stock table validation
public static void stockTable() throws Throwable
{
	//read data from note pad
	FileReader fr = new FileReader("./CaptureData/stockNumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data =br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("Search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).sendKeys(Exp_Data);
	driver.findElement(By.xpath(conpro.getProperty("Search-button"))).click();
	Thread.sleep(3000);
	String Act_Data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
	Reporter.log(Act_Data+"-----------"+Exp_Data,true);
	try {
	Assert.assertEquals(Act_Data, Exp_Data,"Stock Number is Not Matching");
	}catch(AssertionError a)
	{
		System.out.println(a.getMessage());
	}
}
//method for capture supplier number into notepad
public static void capturesup(String Locator_Type,String Locator_Value) throws Throwable
{
	String supplierNum="";
	if(Locator_Type.equalsIgnoreCase("xpath"))
	{
		supplierNum = driver.findElement(By.xpath(Locator_Value)).getAttribute("value");
	}
	if(Locator_Type.equalsIgnoreCase("name"))
	{
		supplierNum = driver.findElement(By.name(Locator_Value)).getAttribute("value");
	}
	if(Locator_Type.equalsIgnoreCase("id"))
	{
		supplierNum = driver.findElement(By.id(Locator_Value)).getAttribute("value");
	}
	//write supplier number into notepad
	FileWriter fw = new FileWriter("./CaptureData/Supplier.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(supplierNum);
	bw.flush();
	bw.close();
}
//method for supplier table
public static void supplierTable() throws Throwable
{
	//read supplier number from notepad
	FileReader fr = new FileReader("./CaptureData/Supplier.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).isDisplayed())
		//click search panel if search textbox is not displayed
		driver.findElement(By.xpath(conpro.getProperty("Search-panel"))).click();
	    //clear text init textbox
	   driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).clear();
	   driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).sendKeys(Exp_Data);
	   Thread.sleep(3000);
		driver.findElement(By.xpath(conpro.getProperty("Search-button"))).click();
		Thread.sleep(3000);
		String Act_data = driver.findElement(By.xpath("//table[@class=table ewTable'']/tbody/tr[1]/td[6]/div/span/span")).getText();
		Reporter.log(Act_data+"-------"+Exp_Data,true);
		try {
		Assert.assertEquals(Act_data, Exp_Data,"Supplier is not matching");
		}catch(AssertionError a)
		{
			System.out.println(a.getMessage());
		}
}
//method for capture customer number into notepad
public static void capturecus(String Locator_Type,String Locator_Value) throws Throwable
{
	String customerNum="";
	if(Locator_Type.equalsIgnoreCase("xpath"))
	{
		customerNum = driver.findElement(By.xpath(Locator_Value)).getAttribute("value");
	}
	if(Locator_Type.equalsIgnoreCase("name"))
	{
		customerNum = driver.findElement(By.name(Locator_Value)).getAttribute("value");
	}
	if(Locator_Type.equalsIgnoreCase("id"))
	{
		customerNum = driver.findElement(By.id(Locator_Value)).getAttribute("value");
	}
	//write supplier number into notepad
	FileWriter fw = new FileWriter("./CaptureData/Supplier.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(customerNum);
	bw.flush();
	bw.close();
}
//method for customer table
public static void customerTable() throws Throwable
{
	//read customer number from notepad
	FileReader fr = new FileReader("./CaptureData/customer.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).isDisplayed())
		//click search panel if search textbox is not displayed
		driver.findElement(By.xpath(conpro.getProperty("Search-panel"))).click();
	    //clear text init textbox
	   driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).clear();
	   driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).sendKeys(Exp_Data);
	   Thread.sleep(3000);
		driver.findElement(By.xpath(conpro.getProperty("Search-button"))).click();
		Thread.sleep(3000);
		String Act_data = driver.findElement(By.xpath("//table[@class=table ewTable'']/tbody/tr[1]/td[5]/div/span/span")).getText();
		Reporter.log(Act_data+"-------"+Exp_Data,true);
		try {
		Assert.assertEquals(Act_data, Exp_Data,"Customer is not matching");
		}catch(AssertionError a)
		{
			System.out.println(a.getMessage());
		}
}
}
	

















