package se.yrgo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasicFunctionsTest {

    WebDriver driver;
    Duration fiveSeconds = Duration.ofSeconds(5);

    @BeforeEach
    void setupTest() {
        driver = new FirefoxDriver();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void checkTitle() {
        driver.manage().timeouts().implicitlyWait(fiveSeconds);

        driver.get("https://yrgo-amazing-todo-app.netlify.app/");

        assertEquals("Todo App", driver.getTitle());
    }

    @Test
    void homePageShowNotDoneList() {
        driver.manage().timeouts().implicitlyWait(fiveSeconds);
        driver.get("https://yrgo-amazing-todo-app.netlify.app/");

        WebElement todoList = driver.findElement(By.className("todolist"));
        assertNotEquals("todolist__done", todoList.getClass().getName());
    }

    @Test
    void donePageShowsDoneList() {
        driver.manage().timeouts().implicitlyWait(fiveSeconds);
        driver.get("https://yrgo-amazing-todo-app.netlify.app/");

        final var wait = new WebDriverWait(driver, fiveSeconds);

        final var edLink = Utils.find(driver, By.linkText("Done"));
        wait.until(ExpectedConditions.elementToBeClickable(edLink));
        edLink.click();

        wait.until(ExpectedConditions.urlContains("/done"));
        assertTrue(driver.getCurrentUrl().contains("/done"));

        List<WebElement> todos = driver.findElements(By.cssSelector(".todolist li"));

        for (WebElement todo : todos) {
            WebElement checkbox = todo.findElement(By.cssSelector("input[type='checkbox']"));
            assertTrue(checkbox.isSelected());
        }
    }
}

/**
 * Only todos marked as "done" should be displayed on the "done" page.
 * You can add a new todo, which will initially be marked as "not done."
 * A todo can be marked as "done" using a checkbox, and it will then move to the
 * "done" page.
 * A todo marked as "done" can be changed back to "not done" using a checkbox.
 * A todo marked as "done" can be permanently deleted using a button. Deletion
 * must be confirmed.
 * When the page is reloaded, the state of the website should be preserved.
 */