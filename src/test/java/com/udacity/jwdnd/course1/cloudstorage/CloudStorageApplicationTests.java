package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialFormObject;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CloudStorageApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	private CredentialService credentialService;

	@Autowired
	private EncryptionService encryptionService;

	private WebDriver driver;


	@BeforeAll
	static void beforeAll() {

		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		//this.driver = new SafariDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}



	@Test
	@Order(1)
	public void unauthorizedUserAccess(){
		driver.get("http://localhost:"+ port +"/signup");// must be able to access
		Assertions.assertEquals("Sign Up",driver.getTitle());
		driver.get("http://localhost:"+ port +"/login");// must be able to access
		Assertions.assertEquals("Login",driver.getTitle());
		driver.get("http://localhost:"+ port +"/home"); //shouldn't be able to access
		Assertions.assertNotEquals("Home",driver.getTitle());
	}

	@Test
	@Order(2)
	public void loginUsersignUp(){
		String user = "user1";
		String password = "password";
		//sign up process
		driver.get("http://localhost:"+ port +"/signup");
		WebElement signupbtn = driver.findElement(By.id("submit-signup-button"));
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement fname = driver.findElement(By.id("inputFirstName"));
		WebElement lname = driver.findElement(By.id("inputLastName"));
		WebElement psw = driver.findElement(By.id("inputPassword"));
		fname.sendKeys("first");
		lname.sendKeys("last");
		username.sendKeys(user);
		psw.sendKeys(password);
		signupbtn.click();
		//login flow
		driver.get("http://localhost:" + port + "/login");
		WebElement loginbtn = driver.findElement(By.id("login-submit-button"));
		WebElement username_l = driver.findElement(By.id("inputUsername"));
		WebElement psw_l = driver.findElement(By.id("inputPassword"));
		username_l.sendKeys(user);
		psw_l.sendKeys(password);
		loginbtn.click();
		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	@Order(3)
	public void loginUserSignUpLogout(){
		String user = "user1";
		String password = "password";
		//sign up process
		driver.get("http://localhost:"+ port +"/signup");
		WebElement signupbtn = driver.findElement(By.id("submit-signup-button"));
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement fname = driver.findElement(By.id("inputFirstName"));
		WebElement lname = driver.findElement(By.id("inputLastName"));
		WebElement psw = driver.findElement(By.id("inputPassword"));
		fname.sendKeys("first");
		lname.sendKeys("last");
		username.sendKeys(user);
		psw.sendKeys(password);
		signupbtn.click();
		//login flow
		driver.get("http://localhost:" + port + "/login");
		WebElement loginbtn = driver.findElement(By.id("login-submit-button"));
		WebElement username_l = driver.findElement(By.id("inputUsername"));
		WebElement psw_l = driver.findElement(By.id("inputPassword"));
		username_l.sendKeys(user);
		psw_l.sendKeys(password);
		loginbtn.click();
		// should be able to access home
		Assertions.assertEquals("Home", driver.getTitle());
		//log out
		WebElement logoutbtn = driver.findElement(By.id("logout-button"));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();",logoutbtn);
		//logoutbtn.click();
		Assertions.assertNotEquals("Home",driver.getTitle());
		//verify homepage is no longer accessible
		driver.get("http://localhost:"+ port + "/home");
		Assertions.assertEquals("Login",driver.getTitle());
	}

	@Test
	@Order(4)
	public void noteCreation(){
		loginUsersignUp();
		String notetitle = "Title 1";
		String notedescription = "MY NOTE DESCRIPTION ........";

		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(notesTab));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();",notesTab);

		WebElement openNoteModal = driver.findElement(By.id("open-note-modal"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(openNoteModal));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();",openNoteModal);


		WebElement noteTitle = driver.findElement(By.id("note-title"));
		WebElement noteDescriptn = driver.findElement(By.id("note-description"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(noteTitle));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(noteDescriptn));
		noteTitle.sendKeys(notetitle);
		noteDescriptn.sendKeys(notedescription);

		WebElement saveNoteButton = driver.findElement(By.id("save-note-button"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(saveNoteButton));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();",saveNoteButton);
		WebElement notesTab_l = driver.findElement(By.id("nav-notes-tab"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(notesTab_l));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();",notesTab_l);


		WebElement noteDescriptionDisplay = driver.findElement(By.id("note-description-display"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(noteDescriptionDisplay));


		WebElement noteTitle_d = driver.findElement(By.id("note-title-display"));
		WebElement noteDescription_d = driver.findElement(By.id("note-description-display"));
		Assertions.assertEquals(notetitle,noteTitle_d.getText());
		Assertions.assertEquals(notedescription.trim(),noteDescription_d.getText());
	}

	@Test
	@Order(6)
	public void editNotes(){
		String notetitle_u = "Updated title";
		String notrdescription_u = "MY UPDATED DESC ......... ";
		noteCreation();

		WebElement clickToEditButton = driver.findElement(By.id("note-edit-button"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(clickToEditButton));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();",clickToEditButton);


		WebElement noteEditModal = driver.findElement(By.id("noteEditModal"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(noteEditModal));


		WebElement editNoteTitle = driver.findElement(By.id("editNote-title"));
		WebElement editNoteDescription = driver.findElement(By.id("editNote-description"));
		//editNoteTitle.sendKeys(notetitle_u);
		//editNoteDescription.sendKeys(notrdescription_u);
		((JavascriptExecutor)driver).executeScript("arguments[0].value='" + notetitle_u +"';",editNoteTitle);
		((JavascriptExecutor)driver).executeScript("arguments[0].value='" + notrdescription_u + "';",editNoteDescription);

		WebElement saveEditNoteButton = driver.findElement(By.id("edit-note-button"));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();",saveEditNoteButton);

		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(notesTab));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();",notesTab);

		WebElement noteDescriptionDisplay = driver.findElement(By.id("note-description-display"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(noteDescriptionDisplay));

		WebElement noteTitle_d = driver.findElement(By.id("note-title-display"));
		WebElement noteDescription_d = driver.findElement(By.id("note-description-display"));
		Assertions.assertEquals(notetitle_u,noteTitle_d.getText());
		Assertions.assertEquals(notrdescription_u.trim(),noteDescription_d.getText());
	}

	@Test
	@Order(5)
	public void deleteNotes(){
		noteCreation();

		WebElement noteDeleteButton = driver.findElement(By.id("note-delete-button"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(noteDeleteButton));
		WebElement noteTitleDisplay = driver.findElement(By.id("note-title-display"));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();",noteDeleteButton);

		WebElement notesTab_n = driver.findElement(By.id("nav-notes-tab"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(notesTab_n));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();",notesTab_n);


		Assertions.assertThrows(StaleElementReferenceException.class, noteTitleDisplay::getText);

	}

	@Test
	@Order(7)
	public void createCredentials(){
		Credential credential = new Credential();
		credential.setUrl("http://mywebsite.com");
		credential.setUsername("User");
		credential.setPassword("psww");
		List<Credential> listOfCredentials = new ArrayList<>();
		listOfCredentials.add(credential);

		loginUsersignUp();


		WebElement navCredentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(navCredentialsTab));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();",navCredentialsTab);


		WebElement openCredentialsModal = driver.findElement(By.id("open-credentials-modal"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(openCredentialsModal));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();",openCredentialsModal);


		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		WebElement submitCredential = driver.findElement(By.id("credential-submit"));

		((JavascriptExecutor)driver).executeScript("arguments[0].value='" + credential.getUrl() + "';",credentialUrl);
		((JavascriptExecutor)driver).executeScript("arguments[0].value='" + credential.getUsername() + "';", credentialUsername);
		((JavascriptExecutor)driver).executeScript("arguments[0].value='" + credential.getPassword() + "';", credentialPassword);
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", submitCredential);

		WebElement navCredentialsTab_l = driver.findElement(By.id("nav-credentials-tab"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(navCredentialsTab_l));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();",navCredentialsTab_l);

		WebElement credentialPasswordDisplayed = driver.findElement(By.id("credential-table-password"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(credentialPasswordDisplayed));

		List<Credential> createdCredentials = credentialService.getAllCredentialsWithoutId();

		int count = driver.findElements(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr")).size();

		for(int i = 0; i < count;i++){
			WebElement table;
			table = driver.findElement(By.id("credentialTable"));
			new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(table));
			WebElement credentialPassword_l = table.findElement(By.xpath("/html/body/div/div[2]/div/div[4]/div[1]/table/tbody/tr[" + (i+1) + "]/td[3]"));
			Assertions.assertEquals(credentialPassword_l.getText(),createdCredentials.get(i).getPassword());
		}

	}

	@Test
	@Order(9)
	public void editCredentials(){
		CredentialFormObject credential = new CredentialFormObject();
		credential.setCredentialUrl("http://mynewwebsite.com");
		credential.setCredentialUsername("Usernew");
		credential.setCredentialPassword("pswwnn");


		createCredentials();

		List<String> decryptedCredentialPasswords = credentialService.getAllCredentialsWithoutId()
				.stream()
				.map(element -> encryptionService.decryptValue(element.getPassword(),element.getKey()))
				.collect(Collectors.toList());

		List<CredentialFormObject> editedCredentials = new ArrayList<>();
		editedCredentials.add(credential);

		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(credentialsTab));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();",credentialsTab);

		int count = driver.findElements(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr")).size();

		for(int i = 0;i < count;i++){
			WebElement editBtn = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[4]/div[1]/table/tbody/tr[" + (i + 1) + "]/td[1]/button"));
			new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(editBtn));
			editBtn.click();

			WebElement credentialEditPassword = driver.findElement(By.id("credentialEdit-password"));
			Assertions.assertEquals(credentialEditPassword.getAttribute("value"),decryptedCredentialPasswords.get(i));

			WebElement credentialEditUrl = driver.findElement(By.id("credentialEdit-url"));
			new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(credentialEditUrl));
			WebElement credentialEditUsername = driver.findElement(By.id("credentialEdit-username"));
			new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(credentialEditUsername));
			((JavascriptExecutor)driver).executeScript("arguments[0].value='" + credential.getCredentialUrl() + "';", credentialEditUrl);
			((JavascriptExecutor)driver).executeScript("arguments[0].value='" + credential.getCredentialUsername() + "';", credentialEditUsername);
			((JavascriptExecutor)driver).executeScript("arguments[0].value='" + credential.getCredentialPassword() + "';", credentialEditPassword);


			WebElement saveCredentialChanges = driver.findElement(By.id("save-edit-credential"));
			new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(saveCredentialChanges));
			((JavascriptExecutor)driver).executeScript("arguments[0].click();",saveCredentialChanges);

			WebElement navCredentialsTab_l = driver.findElement(By.id("nav-credentials-tab"));
			new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(navCredentialsTab_l));
			((JavascriptExecutor)driver).executeScript("arguments[0].click();",navCredentialsTab_l);

			WebElement table = driver.findElement(By.id("credentialTable"));
			new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(table));
			WebElement credentialUrl = table.findElement(By.xpath("/html/body/div/div[2]/div/div[4]/div[1]/table/tbody/tr[" + (i+1) +"]/th"));
			new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(credentialUrl));
			Assertions.assertEquals(credentialUrl.getText(),editedCredentials.get(i).getCredentialUrl());
		}
	}

	@Test
	@Order(8)
	public void deleteCredentials(){
		//createCredentials();
		loginUsersignUp();
		WebElement navCredentialsTab_l = driver.findElement(By.id("nav-credentials-tab"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(navCredentialsTab_l));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();",navCredentialsTab_l);


		WebElement deleteCredential = driver.findElement(By.id("delete-credential"));
		new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOf(deleteCredential));
		WebElement credentialUrlDisplayed =  driver.findElement(By.id("credential-table-url"));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", deleteCredential);

		List<Credential> deletedcreden = credentialService.getAllCredentialsWithoutId();

		Assertions.assertTrue(deletedcreden.isEmpty());


		Assertions.assertThrows(StaleElementReferenceException.class,() -> {
			credentialUrlDisplayed.getText();
		});

	}
}
