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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasicFunctionsTest {

    WebDriver driver;
    Duration fiveSeconds = Duration.ofSeconds(5);
    String checkboxSelector = "input[type='checkbox']";
    String inputField = "input[type='text']";
    String submitBtn = "input[type='submit']";
    String listSelector = ".todolist li";

    @BeforeEach
    void setupTest() {
        driver = new FirefoxDriver();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    void goToLink() {
        driver.manage().timeouts().implicitlyWait(fiveSeconds);
        driver.get("https://yrgo-amazing-todo-app.netlify.app/");
    }

    @Test
    void checkTitle() {
        goToLink();
        assertEquals("Todo App", driver.getTitle());
    }

    @Test
    void homePageShowNotDoneList() {
        goToLink();
        WebElement todoList = driver.findElement(By.className("todolist"));
        assertNotEquals("todolist__done", todoList.getClass().getName());
    }

    @Test
    void donePageShowsDoneList() {
        goToLink();
        final var wait = new WebDriverWait(driver, fiveSeconds);

        // Click Done link
        final var doneLink = Utils.find(driver, By.linkText("Done"));
        wait.until(ExpectedConditions.elementToBeClickable(doneLink));
        doneLink.click();

        wait.until(ExpectedConditions.urlContains("/done"));
        assertTrue(driver.getCurrentUrl().contains("/done"));

        List<WebElement> todos = driver.findElements(By.cssSelector(listSelector));

        for (WebElement todo : todos) {
            WebElement checkbox = todo.findElement(By.cssSelector(checkboxSelector));
            assertTrue(checkbox.isSelected());
        }
    }

    @Test
    void checkNewTodoWithUncheckedBox() {
        goToLink();
        final var wait = new WebDriverWait(driver, fiveSeconds);

        WebElement input = driver.findElement(By.cssSelector(inputField));
        WebElement submit = driver.findElement(By.cssSelector(submitBtn));

        // Insert new todo
        String newToDotext = "Test to do";
        input.sendKeys(newToDotext);
        wait.until(CustomConditions.elementHasBeenClicked(submit));

        // Find new todo
        List<WebElement> todos = driver.findElements(By.cssSelector(listSelector));
        WebElement lastTodo = todos.get(todos.size() - 1);
        String lastTodoText = lastTodo.getText();

        assertEquals(newToDotext, lastTodoText);

        // Check that box is unchecked
        WebElement checkbox = lastTodo.findElement(By.cssSelector(checkboxSelector));
        assertFalse(checkbox.isSelected());
    }

    @Test
    void markAsDoneThenMoveToDonePage() {
        goToLink();
        final var wait = new WebDriverWait(driver, fiveSeconds);

        WebElement input = driver.findElement(By.cssSelector(inputField));
        WebElement submit = driver.findElement(By.cssSelector(submitBtn));

        // Insert new todo
        String newToDotext = "Test to do";
        input.sendKeys(newToDotext);
        wait.until(CustomConditions.elementHasBeenClicked(submit));

        // Check the new todo box
        List<WebElement> todos = driver.findElements(By.cssSelector(listSelector));
        WebElement lastTodo = todos.get(todos.size() - 1);
        WebElement checkbox = lastTodo.findElement(By.cssSelector(checkboxSelector));
        wait.until(CustomConditions.elementHasBeenClicked(checkbox));

        // Click Done link
        final var doneLink = Utils.find(driver, By.linkText("Done"));
        wait.until(d -> {
            doneLink.click();
            return doneLink;
        });
        wait.until(d -> d.getCurrentUrl().contains("/done"));

        // Check done todo list
        List<WebElement> doneTodos = driver.findElements(By.cssSelector(listSelector));
        boolean isTodoInDoneLIst = doneTodos.stream()
                .map(todo -> todo.getText())
                .anyMatch(text -> text.equals(newToDotext));

        assertTrue(isTodoInDoneLIst);
    }

    @Test
    void doneTodoCanBeUnmarked() {
        goToLink();
        final var wait = new WebDriverWait(driver, fiveSeconds);

        WebElement input = driver.findElement(By.cssSelector(inputField));
        WebElement submit = driver.findElement(By.cssSelector(submitBtn));

        // Insert new todo
        String newTodotext = "Test to do";
        input.sendKeys(newTodotext);
        wait.until(CustomConditions.elementHasBeenClicked(submit));

        // Find and check new todo box
        List<WebElement> todos = driver.findElements(By.cssSelector(listSelector));
        WebElement lastTodo = todos.get(todos.size() - 1);
        WebElement checkbox = lastTodo.findElement(By.cssSelector(checkboxSelector));
        wait.until(CustomConditions.elementHasBeenClicked(checkbox));

        // Click Done link
        final var doneLink = Utils.find(driver, By.linkText("Done"));
        wait.until(d -> {
            doneLink.click();
            return doneLink;
        });
        wait.until(d -> d.getCurrentUrl().contains("/done"));

        // Find and uncheck new done todo
        List<WebElement> doneTodos = driver.findElements(By.cssSelector(listSelector));
        for (WebElement doneTodo : doneTodos) {
            if (doneTodo.getText().equals(newTodotext)) {
                checkbox = doneTodo.findElement(By.cssSelector(checkboxSelector));
                wait.until(CustomConditions.elementHasBeenClicked(checkbox));
                break;
            }
        }

        // Click Todo link
        final var todoLink = Utils.find(driver, By.linkText("Todo"));
        wait.until(d -> {
            todoLink.click();
            return todoLink;
        });
        wait.until(d -> d.getCurrentUrl().contains("/"));

        todos = driver.findElements(By.cssSelector(listSelector));
        lastTodo = todos.get(todos.size() - 1);
        checkbox = lastTodo.findElement(By.cssSelector(checkboxSelector));
        assertFalse(checkbox.isSelected());

    }

    @Test
    void deleteDoneTodoAndConfirmDeletion() {
        goToLink();
        final var wait = new WebDriverWait(driver, fiveSeconds);

        WebElement input = driver.findElement(By.cssSelector(inputField));
        WebElement submit = driver.findElement(By.cssSelector(submitBtn));

        // Insert new todo
        String newTodoText = "Test todo";
        input.sendKeys(newTodoText);
        wait.until(CustomConditions.elementHasBeenClicked(submit));

        // Find and check new todo
        List<WebElement> todos = driver.findElements(By.cssSelector(listSelector));
        WebElement lastTodo = todos.get(todos.size() - 1);
        WebElement checkbox = lastTodo.findElement(By.cssSelector(checkboxSelector));
        wait.until(CustomConditions.elementHasBeenClicked(checkbox));

        // Click Done link
        final var doneLink = Utils.find(driver, By.linkText("Done"));
        wait.until(d -> {
            doneLink.click();
            return doneLink;
        });
        wait.until(d -> d.getCurrentUrl().contains("/done"));

        // Find the done todo and delete it
        List<WebElement> doneTodos = driver.findElements(By.cssSelector(listSelector));
        for (WebElement doneTodo : doneTodos) {
            if (doneTodo.getText().equals(newTodoText)) {
                WebElement deleteBtn = doneTodo.findElement(By.className("todo__done__remove"));
                wait.until(CustomConditions.elementHasBeenClicked(deleteBtn));

                // Handle confirmation
                Alert confirmationDialog = wait.until(ExpectedConditions.alertIsPresent());
                confirmationDialog.accept();
                break;
            }
        }

        doneTodos = driver.findElements(By.cssSelector(listSelector));
        boolean isTodoStillPresent = doneTodos.stream()
                .anyMatch(todo -> todo.getText().equals(newTodoText));

        assertFalse(isTodoStillPresent);

    }

    @Test
    void stateIsPreservedAfterReload() {
        goToLink();
        final var wait = new WebDriverWait(driver, fiveSeconds);

        WebElement input = driver.findElement(By.cssSelector(inputField));
        WebElement submit = driver.findElement(By.cssSelector(submitBtn));

        // Insert new todo
        String newTodoText = "Test todo";
        input.sendKeys(newTodoText);
        wait.until(CustomConditions.elementHasBeenClicked(submit));

        // Find and check new todo
        List<WebElement> todos = driver.findElements(By.cssSelector(listSelector));
        WebElement lastTodo = todos.get(todos.size() - 1);
        WebElement checkbox = lastTodo.findElement(By.cssSelector(checkboxSelector));
        wait.until(CustomConditions.elementHasBeenClicked(checkbox));

        // Click Done link
        final var doneLink = Utils.find(driver, By.linkText("Done"));
        wait.until(d -> {
            doneLink.click();
            return doneLink;
        });
        wait.until(d -> d.getCurrentUrl().contains("/done"));

        driver.navigate().refresh();

        todos = driver.findElements(By.cssSelector(listSelector));
        lastTodo = todos.get(todos.size() - 1);
        checkbox = lastTodo.findElement(By.cssSelector(checkboxSelector));
        // check that the todo is still present in the list
        boolean isTodoPresent = todos.stream()
                .anyMatch(todo -> todo.getText().equals(newTodoText));

        assertTrue(isTodoPresent);
        assertTrue(checkbox.isSelected());
    }

}

/**
 * When the page is reloaded, the state of the website should be preserved.
 */