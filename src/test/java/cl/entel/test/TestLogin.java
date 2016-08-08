package cl.entel.test;

import java.net.URL;
import java.io.File;
import java.net.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import io.appium.java_client.android.*;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.saucelabs.saucerest.*;
import cl.entel.test.util.*;

public class TestLogin {

	private static final String USER = "saucelabs.user";
	private static final String KEY = "saucelabs.key";
	private static final String HOST = "appium.host";
	private static final String PORT = "appium.port";

	private static final int TIMEOUT_SECONDS = 90;

	private static final String APK = "test.apk";
	private static final String PKG = "cl.entel.appswlsdesa";
	private static final String ACTIVITY = "cl.entel.appswlsdesa.SplashscreenActivity";

	private String url;
	private WebDriver driver;
	private WebDriverWait waiting;
	private SauceREST notifier;

	private Logger log;
	private MyProperties my;

	public TestLogin() {
		this.log = LogManager.getLogger(TestLogin.class);
		this.my = new MyProperties(log);
	}

	@Before
	public void setUp() throws Exception {
		url = "http://" + my.property(USER) + ":" + my.property(KEY);
		url += "@" + my.property(HOST) + ":" + my.property(PORT) + "/wd/hub";
		notifier = new SauceREST(my.property(USER), my.property(KEY));
	}

	@Test
	public void testH004() throws Exception {
		log.info("Conectando con servidor Appium: " + url);
		
		DesiredCapabilities device = new DesiredCapabilities();
		
		testH004For(android("Samsung Galaxy S4 Emulator", device, "4.4", "H004")); 
		testH004For(android("LG Nexus 4 Emulator", device, "4.4", "H004")); 
	}

	@After
	public void tearDown() throws Exception {
	}

	public void testH004For(WebDriver driver) throws Exception {
		String job = (((RemoteWebDriver) driver).getSessionId()).toString();
		this.driver = driver;
		this.waiting = new WebDriverWait(driver, TIMEOUT_SECONDS);
		
		// Saltar splash screen
		wait(By.xpath("//android.widget.Button"));
		tap(By.xpath("//android.widget.Button"));

		// Log in
		wait(By.id("cl.entel.appswlsdesa:id/et_phone"));
		wait(By.id("cl.entel.appswlsdesa:id/et_rut"));
		wait(By.id("cl.entel.appswlsdesa:id/et_key"));
		wait(By.id("cl.entel.appswlsdesa:id/btn_login"));

		typewrite(By.id("cl.entel.appswlsdesa:id/et_phone"), "965827579");
		typewrite(By.id("cl.entel.appswlsdesa:id/et_rut"), "5002451");
		typewrite(By.id("cl.entel.appswlsdesa:id/et_key"), "4297");

		tap(By.id("cl.entel.appswlsdesa:id/btn_login"));

		// Autorizar envío de información
		wait(By.xpath("//android.widget.Button[contains(@text,'Sí')]"));
		tap(By.xpath("//android.widget.Button[contains(@text,'Sí')]"));

		// Verificar que ingresamos al WebView de la página principal
		wait(By.id("cl.entel.appswlsdesa:id/entelWebView"));

		Thread.sleep(5000);

		notifier.jobPassed(job);
		driver.quit();
	}
	
	/**
	 * Construye instancia de dispositivo móvil remoto.
	 */
	private WebDriver android(
		String platform, DesiredCapabilities dc, String version, String name, String automation
	) throws MalformedURLException { 
		dc.setCapability("platformName", "Android");
		dc.setCapability("deviceName", platform);
		dc.setCapability("platformVersion", version);
		if(automation != null)
			dc.setCapability("automationName", automation);
		dc.setCapability("app", "sauce-storage:" + APK);
		// dc.setCapability("browserName", "");
		dc.setCapability("deviceOrientation", "portrait");
		dc.setCapability("appiumVersion", "1.5.3");
		dc.setCapability("appPackage", PKG);
		dc.setCapability("name",  name + " [" + platform + " " + version + "]");
		// dc.setCapability("appActivity", ACTIVITY);  // FIXME
		return new AndroidDriver<>(new URL(url), dc);
	}

	/**
	 * Construye instancia de dispositivo móvil remoto.
	 */
	private WebDriver android(
		String platform, DesiredCapabilities dc, String version, String name
	) throws MalformedURLException { 
		return android(platform, dc, version, name, null);
	}

	private void wait(By element) {
		waiting.until(ExpectedConditions.presenceOfElementLocated(element));
	}

	private void typewrite(By element, String text) {
		driver.findElement(element).sendKeys(text);
	}

	private void tap(By element) {
		driver.findElement(element).click();
	}

}
