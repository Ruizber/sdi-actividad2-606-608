package com.uniovi;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.util.SeleniumUtils;

public class ApplicationTests {

	// En Windows (Debe ser la versión 65.0.1 y desactivar las actualizacioens
	// automáticas)):
	static String PathFirefox64 = "C:\\Archivos de programa\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver022 = "C:\\Users\\Fernando Ruiz\\Desktop\\3º Curso 2º Semestre\\SDI\\Lab 5\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver024win64.exe";
	// Común a Windows y a MACOSX
	static WebDriver driver = getDriver(PathFirefox64, Geckdriver022);
	static String URL = "http://localhost:8081";
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
	static public void begin() {
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
		driver.findElement(By.name("nombre")).click();
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
		SeleniumUtils.textoPresentePagina(driver, "Las contraseñas deben ser iguales");
	}

	/*
	//Mirar este test
	@Test
	public void testRegistrarEmailExistente() throws Exception {
		driver.get(URL);
		driver.findElement(By.id("registrarse")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("nombre")).click();
		driver.findElement(By.name("nombre")).clear();
		driver.findElement(By.name("nombre")).sendKeys("Fernando");
		driver.findElement(By.name("apellidos")).clear();
		driver.findElement(By.name("apellidos")).sendKeys("Ruiz Berciano");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("holahola");
		driver.findElement(By.name("repassword")).clear();
		driver.findElement(By.name("repassword")).sendKeys("holahola");
		driver.findElement(By.id("buttonRegistrarse")).click();
		driver.findElement(By.id("registrarse"));
		driver.findElement(By.id("identificarse"));
	}*/

	// PR02. Inicio de sesión /
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
		SeleniumUtils.textoPresentePagina(driver, "Email o password incorrecto");
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
		SeleniumUtils.textoPresentePagina(driver, "Email o password incorrecto");
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
		driver.findElement(By.name("email")).sendKeys("admin@admin.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("identificarseButton")).click();
		SeleniumUtils.textoPresentePagina(driver, "ruizbolyt@gmail.com");
		SeleniumUtils.textoPresentePagina(driver, "prueba@prueba.com");
		SeleniumUtils.textoPresentePagina(driver, "prueba1@prueba1.com");
		SeleniumUtils.textoPresentePagina(driver, "prueba2@prueba2.com");
	}

	/*
	//Mirar
	@Test
	public void testBorradoUsuariosPrimerUsuario() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@admin.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("checkBox")).click();
		driver.findElement(By.id("DeleteButton")).click();
		SeleniumUtils.textoNoPresentePagina(driver, "Fernando ");
	}
	
	@Test
	public void testBorradoUsuariosUltimoUsuario() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@admin.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("checkBox")).click();
		driver.findElement(By.id("DeleteButton")).click();
		SeleniumUtils.textoNoPresentePagina(driver, "prueba10");
	}

	@Test
	public void testBorradoUsuariosTresUsuarios() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@admin.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.id("checkBox")).click();
		driver.findElement(By.id("checkBox")).click();
		driver.findElement(By.id("checkBox")).click();
		driver.findElement(By.id("DeleteButton")).click();
		SeleniumUtils.textoNoPresentePagina(driver, "Javier");
		SeleniumUtils.textoNoPresentePagina(driver, "Prueba2");
		SeleniumUtils.textoNoPresentePagina(driver, "prueba4");
	}*/

	@Test
	public void testAltaOfertaDatosValidos() throws Exception {
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
		driver.findElement(By.id("nombre")).click();
		driver.findElement(By.name("nombre")).clear();
		driver.findElement(By.name("nombre")).sendKeys("Bicicleta");
		driver.findElement(By.name("detalle")).click();
		driver.findElement(By.name("detalle")).clear();
		driver.findElement(By.name("detalle")).sendKeys("De carretera");
		driver.findElement(By.name("precio")).click();
		driver.findElement(By.name("precio")).clear();
		driver.findElement(By.name("precio")).sendKeys("100");
		driver.findElement(By.id("agregar")).click();
		SeleniumUtils.textoPresentePagina(driver, "De carretera");
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
		SeleniumUtils.textoPresentePagina(driver, "pruebaruiz");
	}
/*
	//Mirar
	@Test
	public void testDarBajaUnaOferta() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("ruizbolyt@gmail.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.id("identificarseButton")).click();
		driver.findElement(By.linkText("Gestion de Ofertas")).click();
		driver.findElement(By.linkText("Ver Ofertas Propias")).click();
		driver.findElement(By.linkText("Eliminar")).click();
		SeleniumUtils.textoNoPresentePagina(driver, "PlayStation 4 ");
	}*/

	
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
		SeleniumUtils.textoPresentePagina(driver, "Prueba");
		SeleniumUtils.textoPresentePagina(driver, "pruebaruiz");
		SeleniumUtils.textoPresentePagina(driver, "prueba3");
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
		SeleniumUtils.textoNoPresentePagina(driver, "pruebaruiz");
		SeleniumUtils.textoNoPresentePagina(driver, "prueba3");
	}
	/*
	@Test
	public void testComprarOferta1() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
				.sendKeys("fernandoruiz@gmail.com");
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
		driver.findElement(By.name("username"))
				.sendKeys("fernandoruiz@gmail.com");
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
		driver.findElement(By.name("username"))
				.sendKeys("fernandoruiz@gmail.com");
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
		SeleniumUtils.textoPresentePagina(driver,
				"Error, no tiene suficiente dinero");
	}

	@Test
	public void testListadoOfertasCompradas() throws Exception {
		driver.get(URL);
		driver.findElement(By.linkText("Inicia sesión")).click();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
				.sendKeys("fernandoruiz@gmail.com");
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
	}*/

}
