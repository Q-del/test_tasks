import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class SqlEditorPage {

    private WebDriver driver;
    public SqlEditorPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//td[text()='Giovanni Rovelli']/../td[4]")
    private WebElement rovelliAddress;
    @FindBy(xpath = "//button[contains(normalize-space(.), 'Run SQL')]")
    private WebElement runSqlBtn;
    @FindBy(xpath = "//div[@id='resultSQL']//div[contains(text(), 'Number of Records')]")
    private WebElement numberOfRecords;
    @FindBy(xpath = "//td[text()='1']/../td")
    private List<WebElement> firstRow;

    public SqlEditorPage runSql() {
        runSqlBtn.click();
        return this;
    }
    public String getRovelliAddress() {
        return rovelliAddress.getText();
    }
    public SqlEditorPage typeSqlStatement(String statement) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(statement);
        return this;
    }
    public String getNumberOfRecords() {
        return numberOfRecords.getText().replaceAll("[^0-9]", "");
    }
}
