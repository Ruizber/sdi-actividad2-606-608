package com.uniovi;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.text.ParseException;

import com.uniovi.util.SeleniumUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTests {

	// En Windows (Debe ser la versión 65.0.1 y desactivar las actualizacioens
	// automáticas)):
	static String PathFirefox64 = "C:\\Archivos de programa\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver022 = "C:\\Users\\Fernando Ruiz\\Desktop\\3º Curso 2º Semestre\\SDI\\Lab 5\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver024win64.exe";
	// Común a Windows y a MACOSX
	static WebDriver driver = getDriver(PathFirefox64, Geckdriver022);
	static String URL = "http://localhost:8081";
	static String URLApi = "http://localhost:8081/cliente.html";
	// static String URLRemota = "http://52.39.96.241:8090";

	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	@Before
	public void setUp() throws Exception {
		driver.navigate().to(URL);
	}

	@After
	public void tearDown() throws Exception {
		driver.manage().deleteAllCookies();
	}

	@BeforeClass
	static public void begin() throws ParseException {
		MongoDBInsertion mongoDBInsertion = new MongoDBInsertion();
		mongoDBInsertion.insertData();
	}

	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	@Test
	public void testRegistrarDatosValidos() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("prueba6@prueba6.com");
		driver.findElement(By.name("nombre")).clear();
		driver.findElement(By.name("nombre")).sendKeys("prueba6");
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("prueba6");
		driver.findElement(By.name("apellidos")).click();
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("prueba6");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("prueba6");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("prueba6");
		driver.findElement(By.id("Resgistration")).click();
		SeleniumUtils.textoPresentePagina(driver, "Nuevo usuario registrado");
	}

	@Test
	public void testRegistrarEmailVacio() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("prueba6");
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("prueba6");
		driver.findElement(By.name("apellidos")).click();
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("prueba6");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("prueba6");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("prueba6");
		driver.findElement(By.id("Resgistration")).click();
		SeleniumUtils.textoPresentePagina(driver, "Registrar usuario");
		SeleniumUtils.textoPresentePagina(driver, "Nombre");
		SeleniumUtils.textoPresentePagina(driver, "Apellidos");
		SeleniumUtils.textoPresentePagina(driver, "Repita password");
	}

	@Test
	public void testRegistrarContraseñaRepetidaIncorrecta() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("prueba17@prueba17.com");
		driver.findElement(By.name("nombre")).click();
		driver.findElement(By.name("nombre")).clear();
		driver.findElement(By.name("nombre")).sendKeys("prueba17");
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("prueba17");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("prueba17");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("pruebaprueba");
		driver.findElement(By.id("Resgistration")).click();
		SeleniumUtils.textoPresentePagina(driver,
				"Las contraseñas deben ser iguales");
	}

	@Test
	public void testRegistrarEmailExistente() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("nombre")).click();
		driver.findElement(By.name("nombre")).clear();
		driver.findElement(By.name("nombre")).sendKeys("Fernando");
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("Ruiz Berciano");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("holahola");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("holahola");
		driver.findElement(By.id("Resgistration")).click();
		SeleniumUtils.textoPresentePagina(driver,
				"El email ya está registrado");
	}

	@Test
	public void testInicioSesionDatosValidos() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("mTienda"));
		driver.findElement(By.id("mPublicaciones"));
		driver.findElement(By.id("mCompras"));
	}

	@Test
	public void testInicioSesionContraseñaErronea() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("1234568");
		driver.findElement(By.id("identificarseButton")).click();
		SeleniumUtils.textoPresentePagina(driver,
				"Email o password incorrecto");
	}

	@Test
	public void testInicioSesionContraseñaVacia() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.id("identificarseButton")).click();
		SeleniumUtils.textoPresentePagina(driver, "Identificación de usuario");
	}

	@Test
	public void testInicioSesionEmailInexistente() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("emailnoexistente@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("fehwuiduw");
		driver.findElement(By.id("identificarseButton")).click();
		SeleniumUtils.textoPresentePagina(driver,
				"Email o password incorrecto");
	}

	@Test
	public void testFinDeSesiónIrLogin() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("desconectarse")).click();
		SeleniumUtils.textoPresentePagina(driver, "Identificación de usuario");
		SeleniumUtils.textoPresentePagina(driver, "Email");
		SeleniumUtils.textoPresentePagina(driver, "Password");
	}

	@Test
	public void testFinDeSesión2() throws Exception {
		driver.get(URL);
		SeleniumUtils.textoNoPresentePagina(driver, "Cerrar sesión");
	}

	@Test
	public void testListadoUsuarios() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("identificarseButton")).click();
		SeleniumUtils.textoPresentePagina(driver, "ruizbolyt@gmail.com");
		SeleniumUtils.textoPresentePagina(driver, "prueba@prueba.com");
		SeleniumUtils.textoPresentePagina(driver, "prueba1@prueba1.com");
		SeleniumUtils.textoPresentePagina(driver, "prueba2@prueba2.com");
	}

	@Test
	public void testBorradoUsuariosPrimerUsuario() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("usuarioeliminar1@email.com");
		driver.findElement(By.name("nombre")).clear();
		driver.findElement(By.name("nombre")).sendKeys("usuarioeliminar1");
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("usuarioeliminar1");
		driver.findElement(By.name("apellidos")).click();
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("usuarioeliminar1");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("usuarioeliminar1");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("usuarioeliminar1");
		driver.findElement(By.id("Resgistration")).click();
		driver.findElement(By.id("desconectarse")).click();
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='usuarioeliminar1@email.com'])[1]/following::input[1]"))
				.click();
		driver.findElement(By.id("DeleteButton")).click();
		SeleniumUtils.textoNoPresentePagina(driver, "usuarioeliminar1");
	}

	@Test
	public void testBorradoUsuariosUltimoUsuario() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("usuarioeliminar2@email.com");
		driver.findElement(By.name("nombre")).clear();
		driver.findElement(By.name("nombre")).sendKeys("usuarioeliminar2");
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("usuarioeliminar2");
		driver.findElement(By.name("apellidos")).click();
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("usuarioeliminar2");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("usuarioeliminar2");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("usuarioeliminar2");
		driver.findElement(By.id("Resgistration")).click();
		driver.findElement(By.id("desconectarse")).click();
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='usuarioeliminar2@email.com'])[1]/following::input[1]"))
				.click();
		driver.findElement(By.id("DeleteButton")).click();
		SeleniumUtils.textoNoPresentePagina(driver, "usuarioeliminar2");
	}

	@Test
	public void testBorradoUsuariosTresUsuarios() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("usuarioeliminar1@email.com");
		driver.findElement(By.name("nombre")).clear();
		driver.findElement(By.name("nombre")).sendKeys("usuarioeliminar1");
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("usuarioeliminar1");
		driver.findElement(By.name("apellidos")).click();
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("usuarioeliminar1");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("usuarioeliminar1");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("usuarioeliminar1");
		driver.findElement(By.id("Resgistration")).click();
		driver.findElement(By.id("desconectarse")).click();
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("usuarioeliminar2@email.com");
		driver.findElement(By.name("nombre")).clear();
		driver.findElement(By.name("nombre")).sendKeys("usuarioeliminar2");
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("usuarioeliminar2");
		driver.findElement(By.name("apellidos")).click();
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("usuarioeliminar2");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("usuarioeliminar2");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("usuarioeliminar2");
		driver.findElement(By.id("Resgistration")).click();
		driver.findElement(By.id("desconectarse")).click();
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("usuarioeliminar3@email.com");
		driver.findElement(By.name("nombre")).clear();
		driver.findElement(By.name("nombre")).sendKeys("usuarioeliminar3");
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("usuarioeliminar3");
		driver.findElement(By.name("apellidos")).click();
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("usuarioeliminar3");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("usuarioeliminar3");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("usuarioeliminar3");
		driver.findElement(By.id("Resgistration")).click();
		driver.findElement(By.id("desconectarse")).click();
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='usuarioeliminar1@email.com'])[1]/following::input[1]"))
				.click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='usuarioeliminar2@email.com'])[1]/following::input[1]"))
				.click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='usuarioeliminar3@email.com'])[1]/following::input[1]"))
				.click();
		driver.findElement(By.id("DeleteButton")).click();
		SeleniumUtils.textoNoPresentePagina(driver, "usuarioeliminar1");
		SeleniumUtils.textoNoPresentePagina(driver, "usuarioeliminar2");
		SeleniumUtils.textoNoPresentePagina(driver, "usuarioeliminar3");
	}

	@Test
	public void testWAltaOfertaDatosValidos() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("AgregarOferta")).click();
		driver.findElement(By.name("nombre")).click();
		driver.findElement(By.name("nombre")).clear();
		driver.findElement(By.name("nombre")).sendKeys("AñadirOferta");
		driver.findElement(By.name("detalle")).click();
		driver.findElement(By.name("detalle")).clear();
		driver.findElement(By.name("detalle")).sendKeys("NuevaOferta");
		driver.findElement(By.name("precio")).click();
		driver.findElement(By.name("precio")).clear();
		driver.findElement(By.name("precio")).sendKeys("10");
		driver.findElement(By.id("agregar")).click();
		SeleniumUtils.textoPresentePagina(driver, "AñadirOferta");
	}

	@Test
	public void testAltaOfertaDatosInvalidos() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("AgregarOferta")).click();
		driver.findElement(By.name("detalle")).click();
		driver.findElement(By.name("detalle")).clear();
		driver.findElement(By.name("detalle")).sendKeys("Prueba");
		driver.findElement(By.name("precio")).click();
		driver.findElement(By.name("precio")).clear();
		driver.findElement(By.name("precio")).sendKeys("100");
		driver.findElement(By.id("agregar")).click();
		SeleniumUtils.textoPresentePagina(driver, "Agregar oferta");
		SeleniumUtils.textoPresentePagina(driver, "Detalle");
		SeleniumUtils.textoPresentePagina(driver, "Precio (€)");
	}

	@Test
	public void testAListadoOfertasPropias1() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("mPublicaciones")).click();
		SeleniumUtils.textoPresentePagina(driver, "Bicicleta");
		SeleniumUtils.textoPresentePagina(driver, "PruebaDarDeBaja1");
		SeleniumUtils.textoPresentePagina(driver, "PruebaDarDeBajaUltima");
	}

	@Test
	public void testXDarBajaPrimeraOferta() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("mPublicaciones")).click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='PruebaDarDeBaja1'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Eliminar")).click();
		SeleniumUtils.textoNoPresentePagina(driver, "PruebaDarDeBaja1");
		SeleniumUtils.textoPresentePagina(driver,
				"La oferta se elimino correctamente");
	}

	@Test
	public void testXDarDeBajaUltimaOferta() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("mPublicaciones")).click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='PruebaDarDeBajaUltima'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Eliminar")).click();
		SeleniumUtils.textoNoPresentePagina(driver, "PruebaDarDeBajaUltima");
		SeleniumUtils.textoPresentePagina(driver,
				"La oferta se elimino correctamente");
	}

	@Test
	public void testBuscarOfertasCampoVacio() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("mTienda")).click();
		driver.findElement(By.id("buscador")).click();
		driver.findElement(By.id("buscador")).clear();
		driver.findElement(By.id("botonBuscar")).click();
		SeleniumUtils.textoPresentePagina(driver, "PruebaDarDeBaja1");
		SeleniumUtils.textoPresentePagina(driver, "Bicicleta");
		SeleniumUtils.textoPresentePagina(driver, "PruebaDarDeBajaUltima");
	}

	@Test
	public void testBuscarOfertasBusquedaInexistente() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("mTienda")).click();
		driver.findElement(By.id("buscador")).click();
		driver.findElement(By.id("buscador")).clear();
		driver.findElement(By.id("buscador")).sendKeys("X");
		driver.findElement(By.id("botonBuscar")).click();
		SeleniumUtils.textoNoPresentePagina(driver, "Prueba");
		SeleniumUtils.textoNoPresentePagina(driver, "PruebaDarDeBaja1");
		SeleniumUtils.textoNoPresentePagina(driver, "prueba2");
		SeleniumUtils.textoNoPresentePagina(driver, "Bicicleta");
	}

	@Test
	public void testBuscarOfertasBusquedaMinusculaOMayuscula()
			throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("mTienda")).click();
		driver.findElement(By.id("buscador")).click();
		driver.findElement(By.id("buscador")).clear();
		driver.findElement(By.id("buscador")).sendKeys("bicicleta");
		driver.findElement(By.id("botonBuscar")).click();
		SeleniumUtils.textoPresentePagina(driver, "Bicicleta");
		driver.findElement(By.id("mTienda")).click();
		driver.findElement(By.id("buscador")).click();
		driver.findElement(By.id("buscador")).clear();
		driver.findElement(By.id("buscador")).sendKeys("BICICLETA");
		driver.findElement(By.id("botonBuscar")).click();
		SeleniumUtils.textoPresentePagina(driver, "Bicicleta");
	}

	@Test
	public void testComprarOfertaSaldoAPositivo() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("prueba1@prueba1.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("prueba1");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("mTienda")).click();
		driver.findElement(By.id("buscador")).click();
		driver.findElement(By.id("buscador")).clear();
		driver.findElement(By.id("buscador")).sendKeys("Prueba2");
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='Ofertas'])[1]/preceding::span[1]"))
				.click();
		driver.findElement(By.linkText("Comprar")).click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='Prueba2'])[2]/following::a[1]"))
				.click();
		driver.findElement(By.id("desconectarse")).click();
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("prueba1@prueba1.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("prueba1");
		driver.findElement(By.id("identificarseButton")).click();
		SeleniumUtils.textoPresentePagina(driver, "96.5");
	}

	@Test
	public void testComprarOfertaSaldoBCero() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("mTienda")).click();
		driver.findElement(By.id("buscador")).click();
		driver.findElement(By.id("buscador")).clear();
		driver.findElement(By.id("buscador")).sendKeys("SaldoCero");
		driver.findElement(By.id("botonBuscar")).click();
		driver.findElement(By.linkText("Comprar")).click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='SaldoCero'])[2]/following::a[1]"))
				.click();
		driver.findElement(By.id("desconectarse")).click();
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		SeleniumUtils.textoPresentePagina(driver, "0");
	}

	@Test
	public void testComprarOfertaSaldoCNegativo() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("mTienda")).click();
		driver.findElement(By.id("buscador")).click();
		driver.findElement(By.id("buscador")).clear();
		driver.findElement(By.id("buscador")).sendKeys("Pablo");
		driver.findElement(By.id("botonBuscar")).click();
		driver.findElement(By.linkText("Comprar")).click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='Pablo'])[2]/following::a[1]"))
				.click();
		SeleniumUtils.textoPresentePagina(driver,
				"No tienes suficiente dinero para adquirir esa oferta");
	}

	@Test
	public void testListadoOfertasCompradas() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("mCompras")).click();
		SeleniumUtils.textoPresentePagina(driver, "SaldoCero");
		SeleniumUtils.textoPresentePagina(driver, "100");
	}

	@Test
	public void testWMarcarOfertaComoDestacada1() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("prueba@prueba.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("AgregarOferta")).click();
		driver.findElement(By.id("nombre")).click();
		driver.findElement(By.id("nombre")).clear();
		driver.findElement(By.id("nombre")).sendKeys("Destacada1");
		driver.findElement(By.id("detalle")).clear();
		driver.findElement(By.id("detalle")).sendKeys("Destacada1");
		driver.findElement(By.id("precio")).clear();
		driver.findElement(By.id("precio")).sendKeys("2");
		driver.findElement(By.id("destacada")).click();
		driver.findElement(By.id("agregar")).click();
		SeleniumUtils.textoPresentePagina(driver, "80");
		driver.findElement(By.id("desconectarse")).click();
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.linkText("Tienda")).click();
		SeleniumUtils.textoPresentePagina(driver, "DESTACADA");
	}

	@Test
	public void testWMarcarOfertaComoDestacada2() throws Exception {
		driver.get(URL);
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("prueba@prueba.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("AgregarOferta")).click();
		driver.findElement(By.id("nombre")).click();
		driver.findElement(By.id("nombre")).clear();
		driver.findElement(By.id("nombre")).sendKeys("DestacarLuego");
		driver.findElement(By.id("detalle")).clear();
		driver.findElement(By.id("detalle")).sendKeys("DestacarLuego");
		driver.findElement(By.id("precio")).clear();
		driver.findElement(By.id("precio")).sendKeys("3");
		driver.findElement(By.id("agregar")).click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='DestacarLuego'])[2]/following::a[1]"))
				.click();
		SeleniumUtils.textoPresentePagina(driver, "Publicacion destacada");
		driver.findElement(By.id("desconectarse")).click();
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.linkText("Tienda")).click();
		SeleniumUtils.textoPresentePagina(driver, "DESTACADA");
	}

	@Test
	public void testWMarcarOfertaComoDestacada3() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='NuevaOferta'])[1]/following::a[1]"))
				.click();
		SeleniumUtils.textoPresentePagina(driver,
				"No tienes suficiente dinero, necesitas 20€");
	}

	@Test
	public void testZinicioSesionApiDatosValidos() throws Exception {
		driver.get(URLApi);
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("boton-login")).click();
		SeleniumUtils.esperarSegundos(driver, 2);
		SeleniumUtils.textoPresentePagina(driver, "Titulo");
		SeleniumUtils.textoPresentePagina(driver, "Descripción");
		SeleniumUtils.textoPresentePagina(driver, "Precio");
	}

	@Test
	public void testZinicioSesionApiDatosInvalidos() throws Exception {
		driver.get(URLApi);
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("1234");
		driver.findElement(By.id("boton-login")).click();
		SeleniumUtils.esperarSegundos(driver, 2);
		SeleniumUtils.textoPresentePagina(driver, "Usuario no encontrado");
	}

	@Test
	public void testZinicioSesionApiCamposVaciosPassword() throws Exception {
		driver.get(URLApi);
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.id("boton-login")).click();
		SeleniumUtils.esperarSegundos(driver, 2);
		SeleniumUtils.textoPresentePagina(driver, "Usuario no encontrado");
	}

	@Test
	public void testZinicioSesionApiCamposVaciosEmail() throws Exception {
		driver.get(URLApi);
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("boton-login")).click();
		SeleniumUtils.esperarSegundos(driver, 2);
		SeleniumUtils.textoPresentePagina(driver, "Usuario no encontrado");
	}

	@Test
	public void testZListadoDeOfertasDisponibles() throws Exception {
		driver.get(URLApi);
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("boton-login")).click();
		SeleniumUtils.esperarSegundos(driver, 2);
		SeleniumUtils.textoPresentePagina(driver, "Titulo");
		SeleniumUtils.textoPresentePagina(driver, "Descripción");
		SeleniumUtils.textoPresentePagina(driver, "Precio");
		SeleniumUtils.textoPresentePagina(driver, "Prueba");
		SeleniumUtils.textoPresentePagina(driver, "Pablo");
		SeleniumUtils.textoPresentePagina(driver, "SaldoCero");
		SeleniumUtils.textoPresentePagina(driver, "Prueba2");
		SeleniumUtils.textoPresentePagina(driver, "Destacada1");
		SeleniumUtils.textoPresentePagina(driver, "DestacarLuego");
	}

	@Test
	public void ztestMensajeAparece() throws Exception {
		driver.get(URLApi);
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("boton-login")).click();
		SeleniumUtils.esperarSegundos(driver, 2);
		driver.findElement(By.linkText("Enviar mensaje")).click();
		driver.findElement(By.id("agregar-mensaje")).click();
		driver.findElement(By.id("agregar-mensaje")).clear();
		driver.findElement(By.id("agregar-mensaje")).sendKeys("Prueba Mensaje");
		driver.findElement(By.id("boton-enviar")).click();
		driver.findElement(By.id("navOfertas")).click();
		SeleniumUtils.esperarSegundos(driver, 4);
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='Ofertas'])[1]/following::button[1]"))
				.click();
		driver.findElement(By.linkText("Enviar mensaje")).click();
		SeleniumUtils.esperarSegundos(driver, 4);
		SeleniumUtils.textoPresentePagina(driver, "Prueba Mensaje");
	}

}
