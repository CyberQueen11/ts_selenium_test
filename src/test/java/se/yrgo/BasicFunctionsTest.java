package se.yrgo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import se.yrgo.pageobjects.*;
import se.yrgo.utils.Utils;

public class BasicFunctionsTest {

    private WebDriver driver;
    private HomePage homePage;
    private DonePage donePage;

    @BeforeEach
    void setupTest() {
        FirefoxOptions options = new FirefoxOptions();
        options.setAcceptInsecureCerts(true);
        driver = new FirefoxDriver(options);
        homePage = new HomePage(driver);
        donePage = new DonePage(driver);
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void checkTitle() {
        homePage.open();
        assertEquals("Todo App", driver.getTitle());
    }

    /*
     * @Test
     * void homePageShowsNotDoneList() {
     * driver.manage().window().maximize();
     * // Inject test data
     * ((JavascriptExecutor) driver)
     * .executeScript("localStorage.setItem('todolist', '[{\"name\":\"Item 4\",\"done\":false}]');"
     * );
     * driver.navigate().refresh();
     * 
     * List<WebElement> todos = homePage.getTodoList();
     * assertEquals(1, todos.size());
     * assertEquals("Todo 1", todos.get(0).getText());
     * }
     */

    @Test
    void donePageShowsDoneList() {
        homePage.open();
        Utils.clickWhenReady(driver, By.linkText("Done"));
        assertTrue(driver.getCurrentUrl().contains("/done"));

        donePage.getDoneTodos().forEach(todo -> {
            WebElement checkbox = donePage.getCheckboxForDoneTodo(todo);
            assertTrue(checkbox.isSelected());
        });
    }

    @Test
    void checkNewTodoWithUncheckedBox() {
        homePage.open();
        WebElement inputField = homePage.getInputField();
        WebElement submitButton = homePage.getSubmitButton();

        String newTodoText = "Test to do";
        inputField.sendKeys(newTodoText);
        Utils.clickWhenReady(driver, submitButton);

        WebElement lastTodo = homePage.getTodoElement(newTodoText);
        assertNotNull(lastTodo);

        WebElement checkbox = homePage.getCheckboxForTodo(lastTodo);
        assertFalse(checkbox.isSelected());
    }

    @Test
    void markAsDoneThenMoveToDonePage() {
        homePage.open();
        WebElement inputField = homePage.getInputField();
        WebElement submitButton = homePage.getSubmitButton();

        String newTodoText = "Test to do";
        inputField.sendKeys(newTodoText);
        Utils.clickWhenReady(driver, submitButton);

        WebElement lastTodo = homePage.getTodoElement(newTodoText);
        WebElement checkbox = homePage.getCheckboxForTodo(lastTodo);
        Utils.clickWhenReady(driver, checkbox);

        Utils.clickWhenReady(driver, By.linkText("Done"));
        assertTrue(driver.getCurrentUrl().contains("/done"));

        donePage.getDoneTodos().forEach(todo -> {
            WebElement checkboxInDone = donePage.getCheckboxForDoneTodo(todo);
            assertTrue(checkboxInDone.isSelected());
        });
    }

    @Test
    void deleteDoneTodoAndConfirmDeletion() {
        homePage.open();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement inputField = homePage.getInputField();
        WebElement submitButton = homePage.getSubmitButton();

        String newTodoText = "Test todo";
        inputField.sendKeys(newTodoText);
        Utils.clickWhenReady(driver, submitButton);

        WebElement lastTodo = homePage.getTodoElement(newTodoText);
        WebElement checkbox = homePage.getCheckboxForTodo(lastTodo);
        Utils.clickWhenReady(driver, checkbox);

        Utils.clickWhenReady(driver, By.linkText("Done"));

        List<WebElement> doneTodos = donePage.getDoneTodos();
        doneTodos.stream()
                .filter(todo -> todo.getText().equals(newTodoText))
                .findFirst()
                .ifPresent(todo -> {
                    WebElement deleteBtn = todo.findElement(By.className("todo__done__remove"));
                    Utils.clickWhenReady(driver, deleteBtn);
                    wait.until(ExpectedConditions.alertIsPresent());
                    Alert alert = driver.switchTo().alert();
                    alert.accept();
                });

        doneTodos = donePage.getDoneTodos();
        assertFalse(doneTodos.stream()
                .anyMatch(todo -> todo.getText().equals(newTodoText)));
    }

    @Test
    void stateIsPreservedAfterReload() {
        homePage.open();
        WebElement inputField = homePage.getInputField();
        WebElement submitButton = homePage.getSubmitButton();

        String newTodoText = "Test todo";
        inputField.sendKeys(newTodoText);
        Utils.clickWhenReady(driver, submitButton);

        WebElement lastTodo = homePage.getTodoElement(newTodoText);
        WebElement checkbox = homePage.getCheckboxForTodo(lastTodo);
        Utils.clickWhenReady(driver, checkbox);

        Utils.clickWhenReady(driver, By.linkText("Done"));
        driver.navigate().refresh();

        WebElement reloadedTodo = homePage.getTodoElement(newTodoText);
        assertNotNull(reloadedTodo);

        WebElement reloadedCheckbox = homePage.getCheckboxForTodo(reloadedTodo);
        assertTrue(reloadedCheckbox.isSelected());
    }
}
