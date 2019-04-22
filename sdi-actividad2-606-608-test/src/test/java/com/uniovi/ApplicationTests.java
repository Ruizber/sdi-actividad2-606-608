package com.uniovi;



import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import com.uniovi.util.SeleniumUtils;


//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	
	
	
	// En Windows (Debe ser la versión 65.0.1 y desactivar las actualizacioens
	// automáticas)):
	static String PathFirefox64 = "C:\\Archivos de programa\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver022 = "C:\\Users\\Fernando Ruiz\\Desktop\\3º Curso 2º Semestre\\SDI\\Lab 5\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver024win64.exe";
	// Común a Windows y a MACOSX
	static WebDriver driver = getDriver(PathFirefox64, Geckdriver022);
	static String URL = "http://localhost:8090";
	static String URLRemota = "http://52.39.96.241:8090";

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
	static public void begin() {
	}

	// Al finalizar la última prueba
	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	// PR01. Registrarse como usuario /
	@Test
	public void testRegistrar1() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("Fernando");
		driver.findElement(By.name("surname")).clear();
		driver.findElement(By.name("surname")).sendKeys("Ruiz");
		driver.findElement(By.name("surname")).click();
		driver.findElement(By.name("surname")).clear();
		driver.findElement(By.name("surname")).sendKeys("Ruiz Berciano");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("holahola");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("holahola");
		driver.findElement(By.id("buttonRegistrarse")).click();
	}

	@Test
	public void testRegistrar2() throws Exception {
		driver.get(URL + "/signup");
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("holahola");
		driver.findElement(By.name("repassword")).click();
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("holahola");
		driver.findElement(By.id("buttonRegistrarse")).click();
	}

	@Test
	public void testRegistrar3() throws Exception {
		driver.get(URL + "/signup");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("soyfer@gmail.com");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("Fernando");
		driver.findElement(By.name("surname")).clear();
		driver.findElement(By.name("surname")).sendKeys("Ruiz Berciano");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("holahola");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("holahol");
		driver.findElement(By.id("buttonRegistrarse")).click();
	}

	@Test
	public void testRegistrar4() throws Exception {
		driver.get(URL + "/signup");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("Pablo");
		driver.findElement(By.name("surname")).clear();
		driver.findElement(By.name("surname")).sendKeys("Martinez Arango");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("holahola");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("holahola");
		driver.findElement(By.id("buttonRegistrarse")).click();
	}

	// PR02. Inicio de sesión /
	@Test
	public void testInicioSesion1() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("buttonLoggearse")).click();
		SeleniumUtils.textoPresentePagina(driver, "admin@email.com");
	}

	@Test
	public void testInicioSesion2() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		SeleniumUtils.textoPresentePagina(driver, "pmartinez@gmail.com");
	}

	@Test
	public void testInicioSesion3() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.id("buttonLoggearse")).click();
		SeleniumUtils.textoPresentePagina(driver, "Identifícate");
	}

	@Test
	public void testInicioSesion4() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("fehwuiduw");
		driver.findElement(By.id("buttonLoggearse")).click();
		SeleniumUtils.textoPresentePagina(driver, "Identifícate");
	}

	@Test
	public void testInicioSesion5() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("dnjkdnasdldwh");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("dhahd");
		driver.findElement(By.id("buttonLoggearse")).click();
		SeleniumUtils.textoPresentePagina(driver, "Identifícate");
	}

	@Test
	public void testFinDeSesión1() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Desconectar")).click();
		SeleniumUtils.textoPresentePagina(driver, "Identifícate");
	}
	@Test
	public void testFinDeSesión2() throws Exception {
		driver.get(URL);
		SeleniumUtils.textoNoPresentePagina(driver, "Desconectar");
	}

	@Test
	public void testListadoUsuarios1() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestión de Usuarios")).click();
		driver.findElement(By.linkText("Ver Usuarios")).click();
		SeleniumUtils.textoPresentePagina(driver, "pmartinez@gmail.com");
	}
	
	@Test
	public void testXBorradoUsuarios1() throws Exception {
	    driver.get(URL);
	    driver.findElement(By.linkText("Inicia sesión")).click();
	    driver.findElement(By.name("username")).click();
	    driver.findElement(By.name("username")).clear();
	    driver.findElement(By.name("username")).sendKeys("admin@email.com");
	    driver.findElement(By.name("password")).click();
	    driver.findElement(By.name("password")).clear();
	    driver.findElement(By.name("password")).sendKeys("admin");
	    driver.findElement(By.id("buttonLoggearse")).click();
	    driver.findElement(By.linkText("Gestión de Usuarios")).click();
	    driver.findElement(By.linkText("Ver Usuarios")).click();
	    driver.findElement(By.id("checkBox")).click();
	    driver.findElement(By.id("buttonEliminar")).click();
	    SeleniumUtils.textoNoPresentePagina(driver, "Pablo ");
	 }
	
	@Test
	  public void testXBorradoUsuarios2() throws Exception {
	    driver.get(URL);
	    driver.findElement(By.linkText("Inicia sesión")).click();
	    driver.findElement(By.name("username")).click();
	    driver.findElement(By.name("username")).clear();
	    driver.findElement(By.name("username")).sendKeys("admin@email.com");
	    driver.findElement(By.name("password")).click();
	    driver.findElement(By.name("password")).clear();
	    driver.findElement(By.name("password")).sendKeys("admin");
	    driver.findElement(By.id("buttonLoggearse")).click();
	    driver.findElement(By.linkText("Gestión de Usuarios")).click();
	    driver.findElement(By.linkText("Ver Usuarios")).click();
	    driver.findElement(By.id("checkBox")).click();
	    driver.findElement(By.id("buttonEliminar")).click();
	    SeleniumUtils.textoNoPresentePagina(driver, "Fernando ");
	 }
	
	@Test
	  public void testXBorradoUsuarios3() throws Exception {
	    driver.get(URL);
	    driver.findElement(By.linkText("Registrate")).click();
	    driver.findElement(By.name("email")).click();
	    driver.findElement(By.name("email")).clear();
	    driver.findElement(By.name("email")).sendKeys("pablo@gmail.com");
	    driver.findElement(By.name("name")).clear();
	    driver.findElement(By.name("name")).sendKeys("Pablo");
	    driver.findElement(By.name("surname")).clear();
	    driver.findElement(By.name("surname")).sendKeys("Fernandez Pelaez");
	    driver.findElement(By.name("password")).clear();
	    driver.findElement(By.name("password")).sendKeys("holahola");
	    driver.findElement(By.name("repassword")).clear();
	    driver.findElement(By.name("repassword")).sendKeys("holahola");
	    driver.findElement(By.id("buttonRegistrarse")).click();
	    driver.findElement(By.linkText("Desconectar")).click();
	    driver.findElement(By.name("username")).click();
	    driver.findElement(By.name("username")).clear();
	    driver.findElement(By.name("username")).sendKeys("admin@email.com");
	    driver.findElement(By.name("password")).click();
	    driver.findElement(By.name("password")).clear();
	    driver.findElement(By.name("password")).sendKeys("admin");
	    driver.findElement(By.id("buttonLoggearse")).click();
	    driver.findElement(By.linkText("Gestión de Usuarios")).click();
	    driver.findElement(By.linkText("Ver Usuarios")).click();
	    driver.findElement(By.id("checkBox")).click();
	    driver.findElement(By.id("checkBox")).click();
	    driver.findElement(By.id("checkBox")).click();
	    driver.findElement(By.id("buttonEliminar")).click();
	    SeleniumUtils.textoNoPresentePagina(driver, "pablo@gmail.com ");
	  }
	@Test
	public void testAltaOferta1() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Agregar Oferta")).click();
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("Bicicleta");
		driver.findElement(By.name("details")).click();
		driver.findElement(By.name("details")).clear();
		driver.findElement(By.name("details")).sendKeys("De carretera");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("100");
		driver.findElement(By.id("createSale")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Ver Ofertas Propias")).click();
		SeleniumUtils.textoPresentePagina(driver, "De carretera");
	}

	@Test
	public void testAltaOferta2() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Agregar Oferta")).click();
		driver.findElement(By.name("details")).click();
		driver.findElement(By.name("details")).clear();
		driver.findElement(By.name("details")).sendKeys("Prueba");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("100");
		driver.findElement(By.id("createSale")).click();
		SeleniumUtils.textoPresentePagina(driver, "Agregar Oferta");
	}

	@Test
	public void testAListadoOfertasPropias1() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Ver Ofertas Propias")).click();
		SeleniumUtils.textoPresentePagina(driver, "Bicicleta");
		SeleniumUtils.textoPresentePagina(driver, "PlayStation 4");
		SeleniumUtils.textoPresentePagina(driver, "Television");
	}

	@Test
	public void testDarBajaUnaOferta() throws Exception {
		driver.get(URL + "/login?logout");
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Ver Ofertas Propias")).click();
		driver.findElement(By.linkText("Eliminar")).click();
		SeleniumUtils.textoNoPresentePagina(driver, "PlayStation 4 ");
	}

	@Test
	public void testBuscarOfertas1() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Buscar Ofertas")).click();
		driver.findElement(By.name("searchText")).click();
		driver.findElement(By.id("buttonBuscar")).click();
		SeleniumUtils.textoPresentePagina(driver, "Bicicleta");
		SeleniumUtils.textoPresentePagina(driver, "Television");
		SeleniumUtils.textoPresentePagina(driver, "PlayStation 4");
	}

	@Test
	public void testBuscarOfertas2() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Buscar Ofertas")).click();
		driver.findElement(By.name("searchText")).click();
		driver.findElement(By.name("searchText")).clear();
		driver.findElement(By.name("searchText")).sendKeys("X");
		driver.findElement(By.id("buttonBuscar")).click();
		SeleniumUtils.textoNoPresentePagina(driver, "Bicicleta");
		SeleniumUtils.textoNoPresentePagina(driver, "Television");
		SeleniumUtils.textoNoPresentePagina(driver, "PlayStation 4");
	}

	@Test
	public void testComprarOferta1() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("fernandoruiz@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Buscar Ofertas")).click();
		driver.findElement(By.name("searchText")).click();
		driver.findElement(By.name("searchText")).clear();
		driver.findElement(By.name("searchText")).sendKeys("B");
		driver.findElement(By.id("buttonBuscar")).click();
		driver.findElement(By.id("saleStatusButton")).click();
		SeleniumUtils.textoPresentePagina(driver, "400.0 €");
	}

	@Test
	public void testComprarOferta2() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("fernandoruiz@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Buscar Ofertas")).click();
		driver.findElement(By.name("searchText")).click();
		driver.findElement(By.name("searchText")).clear();
		driver.findElement(By.name("searchText")).sendKeys("Te");
		driver.findElement(By.id("buttonBuscar")).click();
		driver.findElement(By.id("saleStatusButton")).click();
		SeleniumUtils.textoPresentePagina(driver, "0.0 €");
	}

	@Test
	public void testComprarOferta3() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("fernandoruiz@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Buscar Ofertas")).click();
		driver.findElement(By.name("searchText")).click();
		driver.findElement(By.name("searchText")).clear();
		driver.findElement(By.name("searchText")).sendKeys("Play");
		driver.findElement(By.id("buttonBuscar")).click();
		driver.findElement(By.id("saleStatusButton")).click();
		SeleniumUtils.textoPresentePagina(driver, "Error, no tiene suficiente dinero");
	}
	
	 @Test
	  public void testListadoOfertasCompradas() throws Exception {
	    driver.get(URL);
	    driver.findElement(By.linkText("Inicia sesión")).click();
	    driver.findElement(By.name("username")).click();
	    driver.findElement(By.name("username")).clear();
	    driver.findElement(By.name("username")).sendKeys("fernandoruiz@gmail.com");
	    driver.findElement(By.name("password")).click();
	    driver.findElement(By.name("password")).clear();
	    driver.findElement(By.name("password")).sendKeys("123456");
	    driver.findElement(By.id("buttonLoggearse")).click();
	    driver.findElement(By.linkText("Gestion de Ofertas")).click();
	    driver.findElement(By.linkText("Buscar Ofertas")).click();
	    driver.findElement(By.id("saleStatusButton")).click();
	    driver.findElement(By.linkText("Gestion de Ofertas")).click();
	    driver.findElement(By.linkText("Lista de compras")).click();
	    SeleniumUtils.textoPresentePagina(driver, "Bicicleta");
	  }

	@Test
	public void testaInternacionalizacion1() throws Exception {
		driver.get(URL);
		driver.findElement(By.id("btnLanguage")).click();
		driver.findElement(By.id("btnEnglish")).click();
		driver.findElement(By.id("btnLanguage")).click();
		driver.findElement(By.id("btnSpanish")).click();
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Buscar Ofertas")).click();
		driver.findElement(By.id("btnLanguage")).click();
		driver.findElement(By.id("btnEnglish")).click();
		SeleniumUtils.textoPresentePagina(driver, "Detail");
		driver.findElement(By.linkText("Sales Management")).click();
		driver.findElement(By.id("btnLanguage")).click();
		driver.findElement(By.id("btnSpanish")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.id("btnLanguage")).click();
		driver.findElement(By.id("btnEnglish")).click();
		driver.findElement(By.id("btnLanguage")).click();
		driver.findElement(By.id("btnSpanish")).click();
	}

	@Test
	public void testaInternacionalizacion2() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestión de Usuarios")).click();
		driver.findElement(By.linkText("Ver Usuarios")).click();
		driver.findElement(By.id("btnLanguage")).click();
		driver.findElement(By.id("btnEnglish")).click();
		SeleniumUtils.textoPresentePagina(driver, "The users that actually "
				+ "appears in the system are the next ones:");
		driver.findElement(By.id("btnLanguage")).click();
		driver.findElement(By.id("btnSpanish")).click();
		SeleniumUtils.textoPresentePagina(driver, "Los usuarios que actualmente "
				+ "figuran en el sistema son los siguientes:");
	}

	@Test
	 public void testaInternacionalizacion3() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Agregar Oferta")).click();
		driver.findElement(By.id("btnLanguage")).click();
		driver.findElement(By.id("btnEnglish")).click();
		SeleniumUtils.textoPresentePagina(driver, "Add Sale");
	}
	@Test
	public void testaInternacionalizacion4() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
	    driver.findElement(By.name("username")).click();
	    driver.findElement(By.name("username")).clear();
	    driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
	    driver.findElement(By.name("password")).click();
	    driver.findElement(By.name("password")).clear();
	    driver.findElement(By.name("password")).sendKeys("123456");
	    driver.findElement(By.id("buttonLoggearse")).click();
	    driver.findElement(By.linkText("Gestion de Ofertas")).click();
	    driver.findElement(By.linkText("Buscar Ofertas")).click();
	    driver.findElement(By.id("btnLanguage")).click();
	    driver.findElement(By.id("btnSpanish")).click();
	    driver.findElement(By.id("btnLanguage")).click();
		driver.findElement(By.id("btnEnglish")).click();
		SeleniumUtils.textoPresentePagina(driver, "Last");
		SeleniumUtils.textoPresentePagina(driver, "First");
	}
	
	@Test
	public void testSeguridad1() throws Exception {
		driver.get(URL);
		driver.navigate().to(URL +"/user/list");
		SeleniumUtils.textoPresentePagina(driver, "Email");
	}
	
	@Test
	public void testSeguridad2() throws Exception {
		driver.get(URL);
		driver.navigate().to(URL +"/sale/list");
		SeleniumUtils.textoPresentePagina(driver, "Email");
	}
	
	@Test
	public void testSeguridad3() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("pmartinez@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("buttonLoggearse")).click();
		driver.navigate().to(URL +"/user/list");
		SeleniumUtils.textoPresentePagina(driver, "Forbidden");
	}
	
	
	 
}
