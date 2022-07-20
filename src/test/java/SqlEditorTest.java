import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class SqlEditorTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private SqlEditorPage sqlEditorPage;

    @Before
    public void setUp() {
        Path driverPath = Paths.get("driver", "chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", driverPath.toString());
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        wait = (new WebDriverWait(driver, Duration.ofSeconds(10)));
        driver.manage().window().maximize();
        driver.get("https://www.w3schools.com/sql/trysql.asp?filename=trysql_select_all");
        sqlEditorPage = PageFactory.initElements(driver, SqlEditorPage.class);
    }
    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void checkContactNameHasAddress() {
        sqlEditorPage.runSql();
        Assert.assertEquals("Via Ludovico il Moro 22", sqlEditorPage.getRovelliAddress());
    }
    @Test
    public void checkCountOfRows() {
        sqlEditorPage.typeSqlStatement("window.editor.setValue(\"SELECT * FROM Customers WHERE City='London';\")");
        sqlEditorPage.runSql();
        System.out.println(sqlEditorPage.getNumberOfRecords());
        Assert.assertEquals("6", sqlEditorPage.getNumberOfRecords());
    }
    @Test
    public void addNewRow() {
        sqlEditorPage.runSql();
        String currentNumber = sqlEditorPage.getNumberOfRecords();
        sqlEditorPage.typeSqlStatement("window.editor.setValue(\"INSERT INTO Customers VALUES (444, 'Roma', 'Kz', 'Usova Str', 'Tomsk', 634034, 'Ru');\")");
        sqlEditorPage.runSql();
        sqlEditorPage.typeSqlStatement("window.editor.setValue(\"SELECT * FROM Customers;\")");
        sqlEditorPage.runSql();
        Assert.assertEquals(Integer.parseInt(currentNumber)+1, Integer.parseInt(sqlEditorPage.getNumberOfRecords()));
    }
    @Test
    public void checkRowsAfterUpdate() {
        sqlEditorPage.typeSqlStatement("window.editor.setValue(\"UPDATE Customers SET CustomerName='Roma', ContactName='Ts', Address='Usova', City='Tomsk', PostalCode=634034, Country='RU' WHERE CustomerID=1;\")");
        sqlEditorPage.runSql();
        wait.until(ExpectedConditions.textToBe(By.xpath("//div[@id='resultSQL']//div[contains(text(), 'You')]"), "You have made changes to the database. Rows affected: 1"));
        sqlEditorPage.typeSqlStatement("window.editor.setValue(\"SELECT * FROM Customers;\")");
        sqlEditorPage.runSql();
        String myValues[] = {"Roma", "Ts", "Usova", "Tomsk", "634034", "RU"};
        List<WebElement> e = driver.findElements(By.xpath("//td[text()='1']/../td"));
        for(int i = 1; i <= myValues.length; i++ ) {
            Assert.assertEquals(myValues[i-1], e.get(i).getText());
        }
    }
    @Test
    public void checkErrorForEmptyStatement() {
        sqlEditorPage.typeSqlStatement("window.editor.setValue('')");
        sqlEditorPage.runSql();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String error = alert.getText();
        Assert.assertEquals("Error 1: could not execute statement (0 not an error)", error);
    }
}
