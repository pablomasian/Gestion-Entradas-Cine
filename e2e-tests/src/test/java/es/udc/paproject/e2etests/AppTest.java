package es.udc.paproject.e2etests;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class AppTest {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void login(String userName, String password) {
        driver.get("http://localhost:5173");

        WebElement loginLink = driver.findElement(By.id("loginLink"));
        loginLink.click();

        WebElement userNameInput = driver.findElement(By.id("userName"));
        userNameInput.clear();
        userNameInput.sendKeys(userName);

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.clear();
        passwordInput.sendKeys(password);

        WebElement loginButton = driver.findElement(By.id("loginButton"));
        loginButton.click();

        WebElement userDropdown = driver.findElement(By.id("user-dropdown"));
        assertTrue(userDropdown.getText().contains(userName));
    }

    @Test
    public void testLogin() {
        login("testviewer", "pa2526");
    }
    @Test
    public void testSessionDetails() {
        login("testviewer", "pa2526");

        Select billboardDate = new Select(driver.findElement(By.id("billboardDate")));
        billboardDate.selectByIndex(1);

        List<WebElement> movieLinks = driver.findElements(By.cssSelector("table tbody tr td:first-child a"));
        WebElement firstMovieLink = movieLinks.get(0);
        String movieTitle = firstMovieLink.getText();

        List<WebElement> sessionLinks = driver.findElements(By.cssSelector("table tbody tr td:nth-child(2) a"));
        WebElement firstSessionLink = sessionLinks.get(0);
        String sessionTime = firstSessionLink.getText();

        firstSessionLink.click();

        assertTrue(driver.findElement(By.id("movieTitle")).isDisplayed());
        assertTrue(driver.findElement(By.id("sessionDate")).isDisplayed());
        assertTrue(driver.findElement(By.id("runtime")).isDisplayed());
        assertTrue(driver.findElement(By.id("room")).isDisplayed());
        assertTrue(driver.findElement(By.id("price")).isDisplayed());
        assertTrue(driver.findElement(By.id("availableSeats")).isDisplayed());
        assertTrue(driver.findElement(By.id("buyForm")).isDisplayed());
        assertTrue(driver.findElement(By.id("quantity")).isDisplayed());
        assertTrue(driver.findElement(By.id("creditCardNumber")).isDisplayed());
        assertTrue(driver.findElement(By.id("buyBotton")).isDisplayed());

        assertEquals(movieTitle, driver.findElement(By.id("movieTitle")).getText());
        assertTrue(driver.findElement(By.id("sessionDate")).getText().contains(sessionTime));
    }

    // Busca una sesión con localidades disponibles para comprar por si una se queda sin al hacer el test varias veces seguidas
    private long findBuyableSessionId() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        String billboardUrl = "http://localhost:8080/catalog/billboard?date=" + LocalDate.now().plusDays(1);
        String billboardJson = client.send(HttpRequest.newBuilder(URI.create(billboardUrl))
                .GET().build(), HttpResponse.BodyHandlers.ofString()).body();

        Matcher sessionMatcher = Pattern.compile("\\\"sessionId\\\":(\\d+)").matcher(billboardJson);
        while (sessionMatcher.find()) {
            long sessionId = Long.parseLong(sessionMatcher.group(1));
            String sessionJson = client.send(HttpRequest.newBuilder(
                            URI.create("http://localhost:8080/catalog/sessions/" + sessionId))
                    .GET().build(), HttpResponse.BodyHandlers.ofString()).body();
            Matcher localitiesMatcher = Pattern.compile("\\\"localitiesLeft\\\":(\\d+)").matcher(sessionJson);
            if (localitiesMatcher.find() && Integer.parseInt(localitiesMatcher.group(1)) > 0) {
                return sessionId;
            }
        }

        throw new IllegalStateException("No hay sesiones con localidades disponibles para comprar");
    }

    @Test
    public void testBuyTickets() throws IOException, InterruptedException {
        login("testviewer", "pa2526");

        long sessionId = findBuyableSessionId();
        driver.get("http://localhost:5173/catalog/session-details/" + sessionId);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("movieTitle")));
        String movieTitle = driver.findElement(By.id("movieTitle")).getText();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quantity")));
        WebElement qty = driver.findElement(By.id("quantity"));
        new Select(qty).selectByValue("2");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("creditCardNumber")));
        WebElement card = driver.findElement(By.id("creditCardNumber"));
        card.clear();
        card.sendKeys("4111111111111111");

        wait.until(ExpectedConditions.elementToBeClickable(By.id("buyBotton")));
        driver.findElement(By.id("buyBotton")).click();

        // esperar pantalla de éxito y tomar id de compra (ajusta selector si es distinto)
        wait.until(ExpectedConditions.urlContains("/catalog/buy-success"));
        String currentUrl = driver.getCurrentUrl();
        String purchaseId = currentUrl.substring(currentUrl.lastIndexOf('/') + 1);

        // ir a historial de compras y verificar
        driver.get("http://localhost:5173/orders");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody tr")));
        WebElement firstPurchaseRow = driver.findElement(By.cssSelector("table tbody tr"));
        String rowText = firstPurchaseRow.getText();
        assertTrue(rowText.contains(purchaseId));
        assertTrue(rowText.contains(movieTitle));
    }

    @Test
    public void testDeliveringTickets(){
        String id = "1", creditCard = "5555444433332222";

        login("testticketseller", "pa2526");

        WebElement userProfile = driver.findElement(By.id("user-dropdown"));

        userProfile.click();

        WebElement deliverSection = driver.findElement(By.id("deliver-tickets"));
        deliverSection.click();

        for(int i = 0; i < 2; i++) {
            WebElement orderIdInput = driver.findElement(By.id("orderId"));
            orderIdInput.clear();
            orderIdInput.sendKeys(id);

            WebElement creditCardInput = driver.findElement(By.id("creditCardNumber"));
            creditCardInput.clear();
            creditCardInput.sendKeys(creditCard);

            WebElement deliverButton = driver.findElement(By.id("deliverButton"));
            deliverButton.click();

            if(i == 0){
                WebElement successMessage = driver.findElement(By.id("success"));
                assertTrue(successMessage.isDisplayed());
            }
            else{
                WebElement errorMessage = driver.findElement(By.id("error"));
                assertTrue(errorMessage.isDisplayed());
                assertTrue(errorMessage.getText().contains("entregadas")
                        || errorMessage.getText().contains("delivered"));
            }
        }
    }
}
