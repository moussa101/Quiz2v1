package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

import org.junit.Test;


public class Q2TestV1 {

	String cannotSellPath = "exceptions.CannotSellException";
	String notEnoughBudgetPath = "exceptions.NotEnoughBudgetException";
	String draftablePath = "interfaces.Draftable";
	String clubPath = "clubs.Club";
	String playerPath = "players.Player";
	String individualPlayerPath = "players.IndividualPlayer";
	String teamPlayerPath = "players.TeamPlayer";
	String genderPath = "players.Gender";
	String IndividualSportTypesPath = "players.IndividualSportTypes";
	String PlayerPositionPath = "players.PlayerPosition";

	// ////////////////helper methods////////////////////
	private void testClassIsAbstract(Class aClass) {
		assertTrue(aClass.getSimpleName() + " should be an abtract class.",
				Modifier.isAbstract(aClass.getModifiers()));
	}

	private void testClassImplementsInterface(Class aClass, Class iClass) {
		assertTrue(
				aClass.getSimpleName() + " should implement "
						+ iClass.getSimpleName(),
				iClass.isAssignableFrom(aClass));
	}

	private void testIsInterface(Class iClass) {
		assertTrue(iClass.getSimpleName() + " should be interface",
				iClass.isInterface());
	};

	private void testConstructorExists(Class aClass, Class[] inputs) {
		boolean thrown = false;
		try {
			aClass.getConstructor(inputs);
		} catch (NoSuchMethodException e) {
			thrown = true;
		}

		if (inputs.length > 0) {
			String msg = "";
			int i = 0;
			do {
				msg += inputs[i].getSimpleName() + " and ";
				i++;
			} while (i < inputs.length);

			msg = msg.substring(0, msg.length() - 4);

			assertFalse(
					"Missing constructor with " + msg + " parameter"
							+ (inputs.length > 1 ? "s" : "") + " in "
							+ aClass.getSimpleName() + " class.",

					thrown);
		} else
			assertFalse(
					"Missing constructor with zero parameters in "
							+ aClass.getSimpleName() + " class.",

					thrown);

	}

	private void testConstructorInitialization(Object createdObject,
			String[] names, Object[] values) throws NoSuchMethodException,
			SecurityException, IllegalArgumentException, IllegalAccessException {

		for (int i = 0; i < names.length; i++) {

			Field f = null;
			Class curr = createdObject.getClass();
			String currName = names[i];
			Object currValue = values[i];

			while (f == null) {

				if (curr == Object.class)
					fail("Class " + createdObject.getClass().getSimpleName()
							+ " should have the instance variable \""
							+ currName + "\".");
				try {
					f = curr.getDeclaredField(currName);
				} catch (NoSuchFieldException e) {
					curr = curr.getSuperclass();
				}
			}
			f.setAccessible(true);

			assertEquals("The constructor of the "
					+ createdObject.getClass().getSimpleName()
					+ " class should initialize the instance variable \""
					+ currName + "\" correctly.", currValue,
					f.get(createdObject));

		}

	}

	@SuppressWarnings("rawtypes")
	private void testInstanceVariablesArePresent(Class aClass, String varName,
			boolean implementedVar) throws SecurityException {

		boolean thrown = false;
		try {
			aClass.getDeclaredField(varName);
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		if (implementedVar) {
			assertFalse("There should be " + varName
					+ " instance variable in class " + aClass.getName(), thrown);
		} else {
			assertTrue("There should not be " + varName
					+ " instance variable in class " + aClass.getName()
					+ ", it should be inherited from the super class", thrown);
		}
	}

	@SuppressWarnings("rawtypes")
	private void testInstanceVariablesArePrivate(Class aClass, String varName)
			throws NoSuchFieldException, SecurityException {
		Field f = aClass.getDeclaredField(varName);
		assertEquals(
				varName + " instance variable in class " + aClass.getName()
						+ " should not be accessed outside that class", 2,
				f.getModifiers());
	}

	@SuppressWarnings("rawtypes")
	private void testInstanceVariableIsPrivate(Class aClass, String varName)
			throws NoSuchFieldException, SecurityException {
		Field f = aClass.getDeclaredField(varName);
		assertEquals(
				varName + " instance variable in class " + aClass.getName()
						+ " should not be accessed outside that class", 2,
				f.getModifiers());
	}

	private static boolean containsMethodName(Method[] methods, String name) {
		for (Method method : methods) {
			if (method.getName().equals(name))
				return true;
		}
		return false;
	}

	private void testMethodAbsence(Class aClass, String methodName) {
		Method[] methods = aClass.getDeclaredMethods();
		assertFalse(aClass.getSimpleName() + " class should not override "
				+ methodName + " method",
				containsMethodName(methods, methodName));
	}

	private void testInterfaceMethod(Class iClass, String methodName,
			Class returnType, Class[] parameters) {
		Method[] methods = iClass.getDeclaredMethods();
		assertTrue(iClass.getSimpleName() + " interface should have "
				+ methodName + "method",
				containsMethodName(methods, methodName));

		Method m = null;
		boolean thrown = false;
		try {
			m = iClass.getDeclaredMethod(methodName, parameters);
		} catch (NoSuchMethodException e) {
			thrown = true;
		}

		assertTrue(
				"Method " + methodName
						+ " should have the following set of parameters : "
						+ Arrays.toString(parameters), !thrown);
		assertTrue("wrong return type", m.getReturnType().equals(returnType));

	}

	// ////Player calss//////
	@Test(timeout = 1000)
	public void testPlayerClassIsAbstract() throws ClassNotFoundException {
		testClassIsAbstract(Class.forName(playerPath));
	}

	@Test(timeout = 1000)
	public void testPlayerClassImplementsDraftableInterface() {
		try {
			testClassImplementsInterface(Class.forName(playerPath),
					Class.forName(draftablePath));
		} catch (ClassNotFoundException e) {
			assertTrue(e.getClass().getName() + " occurred: " + e.getMessage(),
					false);
		}
	}

	// ////////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testisDraftableMethodInDraftable() {
		try {
			testInterfaceMethod(Class.forName(draftablePath), "isDraftable",
					boolean.class, null);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Please check the console for the error, its an error from this catch statement."
					+ e.getClass() + " occurred");
		}
	}

	@Test(timeout = 1000)
	public void testgetSalaryMethodInDraftable() {
		try {
			testInterfaceMethod(Class.forName(draftablePath), "getSalary",
					int.class, null);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Please check the console for the error, its an error from this catch statement."
					+ e.getClass() + " occurred");
		}
	}

	@Test(timeout = 1000)
	public void testgetPriceMethodInDraftable() {
		try {
			testInterfaceMethod(Class.forName(draftablePath), "getPrice",
					int.class, null);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Please check the console for the error, its an error from this catch statement."
					+ e.getClass() + " occurred");
		}
	}

	@Test(timeout = 1000)
	public void testInstanceVariablesInClub() throws Exception {

		testInstanceVariablesArePresent(Class.forName(clubPath),
				"individualPlayers", true);
		testInstanceVariablesArePresent(Class.forName(clubPath), "teamPlayers",
				true);
		testInstanceVariablesArePresent(Class.forName(clubPath), "budget", true);

	}

	@Test(timeout = 1000)
	public void testConstructorClub() throws ClassNotFoundException {
		Class[] inputs = { int.class };
		testConstructorExists(Class.forName(clubPath), inputs);
	}

	// ////////need to add methods test logic of club//////////////////////
	
	@Test(timeout = 1000)
	public void testAddPlayerMethodInClubClassThrowException()
			throws NoSuchMethodException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException {
		assertNotNull(Class.forName(clubPath));
		assertNotNull(Class.forName(individualPlayerPath));
		assertNotNull(Class.forName(genderPath));
		Class<?> notEnoughBudgetException = Class.forName(notEnoughBudgetPath);

		Constructor<?> constructor = Class.forName(individualPlayerPath)
				.getConstructor(String.class, Class.forName(genderPath),
						int.class, int.class,
						Class.forName(IndividualSportTypesPath), int.class);
		int numberOfMedals = (int) (Math.random() * (200 - 100 + 1)) + 100;
		
		int randomYear = (int) (Math.random() * (1900 - 1800 + 1)) + 1;
		int exp = 2024 - randomYear;

		Object g = Enum.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
		Object ist = Enum.valueOf((Class<Enum>) Class.forName(IndividualSportTypesPath),				"GYMNASTICS");

		Object player = constructor.newInstance("Messi", g, 32, randomYear,	ist, numberOfMedals);

		Constructor<?> clubConstructor = Class.forName(clubPath).getConstructor(int.class);

		Object createdClub = clubConstructor.newInstance(500);

		Method m = createdClub.getClass().getMethod("addPlayer",Class.forName(playerPath));
		try {
			m.invoke(createdClub, player);
			fail("Expected NotEnoughBudgetException was not thrown,the exception should be thrown if the player's salary is greater than the club's budget");

		} catch (InvocationTargetException e) {
			Throwable thrownException = e.getTargetException();
			assertNotNull("Expected exception was thrown", thrownException);
			assertTrue(
					"Expected NotEnoughBudgetException was not thrown,the exception should be thrown if the player's salary is greater than the club's budget",
					notEnoughBudgetException.isInstance(thrownException));
		}

	}

	
	
	
	@Test(timeout = 1000)
	public void testSellIndvidualPlayerMethodInClubClassLogic()
			throws NoSuchMethodException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException {assertNotNull(Class.forName(clubPath));
			assertNotNull(Class.forName(individualPlayerPath));
			assertNotNull(Class.forName(genderPath));
			Class<?> noEnoughBudgetException = Class.forName(notEnoughBudgetPath);

			Constructor<?> constructor = Class.forName(individualPlayerPath)
					.getConstructor(String.class, Class.forName(genderPath),
							int.class, int.class,
							Class.forName(IndividualSportTypesPath), int.class);
			int numberOfMedals = (int) (Math.random() * (200 - 100 + 1)) + 100;
			int randomYear = (int) (Math.random() * (1900 - 1800 + 1)) + 1;
			int exp = 2024 - randomYear;

			Object g = Enum.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
			Object ist = Enum.valueOf(	(Class<Enum>) Class.forName(IndividualSportTypesPath),"GYMNASTICS");

			Object player = constructor.newInstance("Messi", g, 32, randomYear,ist, numberOfMedals);
			int salary = exp*numberOfMedals*50;
			int price = salary/2;
			int originalBudget = salary*2;
			Constructor<?> clubConstructor = Class.forName(clubPath).getConstructor(int.class);

			Object createdClub = clubConstructor.newInstance(originalBudget);
			Method m = createdClub.getClass().getMethod("sellPlayer",Class.forName(playerPath));
			
			try {
				
				Field budget = Class.forName(clubPath).getDeclaredField("budget");
				Field teamplayers = Class.forName(clubPath).getDeclaredField("teamPlayers");
				Field indivplayers = Class.forName(clubPath).getDeclaredField("individualPlayers");
				teamplayers.setAccessible(true);
				indivplayers.setAccessible(true);
				budget.setAccessible(true);
		
				
				ArrayList<Object> indivAfterAdd = (ArrayList<Object>) indivplayers.get(createdClub);
				indivAfterAdd.add(player);
				//ArrayList<Object> teamAfterAdd = (ArrayList<Object>) teamplayers.get(createdClub);

				m.invoke(createdClub, player);
				
				
				int budgetafterAdd = budget.getInt(createdClub);
				
				assertEquals(
						"The player's price should be added from budget when you sell an individual player to your club",
						(originalBudget+price), budgetafterAdd);
				
		
				
				
			} catch (InvocationTargetException e) {
				Throwable thrownException = e.getTargetException();
				assertNull("UnExpected exception was thrown,The indvidual player should not be added to teamsPlayers array in club", thrownException);
						}

	}
	
	
	@Test(timeout = 1000)
	public void testSellTeamPlayerMethodInClubClassLogic()
			throws NoSuchMethodException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException {assertNotNull(Class.forName(clubPath));
			assertNotNull(Class.forName(individualPlayerPath));
			assertNotNull(Class.forName(genderPath));
			Class<?> noEnoughBudgetException = Class.forName(notEnoughBudgetPath);

			Constructor<?> constructor = Class.forName(teamPlayerPath)
					.getConstructor(String.class, Class.forName(genderPath),
							int.class, int.class, String.class,
							Class.forName(PlayerPositionPath));
			Object g = Enum
					.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
			Object ppa = Enum.valueOf(
					(Class<Enum>) Class.forName(PlayerPositionPath), "ATTACKER");
			int randomYear = (int) (Math.random() * (1900 - 1800 + 1)) + 1800;
			int exp = 2024 - randomYear;

			Object player = constructor.newInstance("Messi", g, 32,randomYear, "Barc", ppa);
			int salary = exp*1500;
			int originalBudget = salary*2;
			int price = salary/2;
			Constructor<?> clubConstructor = Class.forName(clubPath).getConstructor(int.class);

			Object createdClub = clubConstructor.newInstance(originalBudget);
			Method m = createdClub.getClass().getMethod("sellPlayer",Class.forName(playerPath));
			
			try {
				
				Field budget = Class.forName(clubPath).getDeclaredField("budget");
				Field teamplayers = Class.forName(clubPath).getDeclaredField("teamPlayers");
				Field indivplayers = Class.forName(clubPath).getDeclaredField("individualPlayers");
				teamplayers.setAccessible(true);
				indivplayers.setAccessible(true);
				budget.setAccessible(true);
		
				
				//ArrayList<Object> indivAfterAdd = (ArrayList<Object>) indivplayers.get(createdClub);
				
				ArrayList<Object> teamAfterAdd = (ArrayList<Object>) teamplayers.get(createdClub);
				teamAfterAdd.add(player);
				m.invoke(createdClub, player);
				
				
				int budgetafterAdd = budget.getInt(createdClub);
				
				assertEquals(
						"The player's price should be added from budget when you sell a Team player to your club",
						(originalBudget+price), budgetafterAdd);
				
		
				
				
			} catch (InvocationTargetException e) {
				Throwable thrownException = e.getTargetException();
				assertNull("UnExpected exception was thrown,The indvidual player should not be added to teamsPlayers array in club", thrownException);
						}

	}
	
	@Test(timeout = 1000)
	public void testAddIndvidualPlayerMethodInClubClassLogic()
			throws NoSuchMethodException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException {
		assertNotNull(Class.forName(clubPath));
		assertNotNull(Class.forName(individualPlayerPath));
		assertNotNull(Class.forName(genderPath));
		Class<?> noEnoughBudgetException = Class.forName(notEnoughBudgetPath);

		Constructor<?> constructor = Class.forName(individualPlayerPath)
				.getConstructor(String.class, Class.forName(genderPath),
						int.class, int.class,
						Class.forName(IndividualSportTypesPath), int.class);
		int numberOfMedals = (int) (Math.random() * (200 - 100 + 1)) + 100;
		int randomYear = (int) (Math.random() * (1900 - 1800 + 1)) + 1;
		int exp = 2024 - randomYear;

		Object g = Enum.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
		Object ist = Enum.valueOf(	(Class<Enum>) Class.forName(IndividualSportTypesPath),"GYMNASTICS");

		Object player = constructor.newInstance("Messi", g, 32, randomYear,ist, numberOfMedals);
		int price = exp*numberOfMedals*50;
		int originalBudget = price*2;
		Constructor<?> clubConstructor = Class.forName(clubPath).getConstructor(int.class);

		Object createdClub = clubConstructor.newInstance(originalBudget);
		Method m = createdClub.getClass().getMethod("addPlayer",Class.forName(playerPath));
		
		try {
			m.invoke(createdClub, player);
			Field budget = Class.forName(clubPath).getDeclaredField("budget");
			Field teamplayers = Class.forName(clubPath).getDeclaredField("teamPlayers");
			Field indivplayers = Class.forName(clubPath).getDeclaredField("individualPlayers");

			teamplayers.setAccessible(true);
			indivplayers.setAccessible(true);
			budget.setAccessible(true);
			ArrayList<Object> teamAfterAdd = (ArrayList<Object>) teamplayers.get(createdClub);
			
			ArrayList<Object> indivAfterAdd = (ArrayList<Object>) indivplayers.get(createdClub);
			int budgetafterAdd = budget.getInt(createdClub);
			
			assertEquals(
					"The player's salary should be subtracted from budget when you add an individual player to your club",
					(originalBudget-price), budgetafterAdd);
			
			assertEquals(
					"The player should be added to individualPlayers array in club",
					1, indivAfterAdd.size());
			assertEquals(
					"The indvidual player should not be added to teamsPlayers array in club",
					0,teamAfterAdd.size());
			
			
			
		} catch (InvocationTargetException e) {
			Throwable thrownException = e.getTargetException();
			assertNull("UnExpected exception was thrown,The indvidual player should not be added to teamsPlayers array in club", thrownException);
					}

		
		

		
		
	}

	@Test(timeout = 1000)
	public void testAddTeamPlayerMethodInClubClassThrowLogic()
			throws NoSuchMethodException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException {
		assertNotNull(Class.forName(clubPath));
		assertNotNull(Class.forName(individualPlayerPath));
		assertNotNull(Class.forName(genderPath));
		assertNotNull(Class.forName(PlayerPositionPath));
		Class<?> noEnoughBudgetException = Class.forName(notEnoughBudgetPath);

		Constructor<?> constructor = Class.forName(teamPlayerPath)
				.getConstructor(String.class, Class.forName(genderPath),
						int.class, int.class, String.class,
						Class.forName(PlayerPositionPath));
		Object g = Enum
				.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
		Object ppa = Enum.valueOf(
				(Class<Enum>) Class.forName(PlayerPositionPath), "ATTACKER");
		int randomYear = (int) (Math.random() * (1900 - 1800 + 1)) + 1800;
		int exp = 2024 - randomYear;

		Object player = constructor.newInstance("Messi", g, 32,randomYear, "Barc", ppa);
		int price = exp*1500;
		int originalBudget = price*2;
		Constructor<?> clubConstructor = Class.forName(clubPath).getConstructor(int.class);

		Object createdClub = clubConstructor.newInstance(originalBudget);
		Method m = createdClub.getClass().getMethod("addPlayer",Class.forName(playerPath));
		
		try {
			m.invoke(createdClub, player);
			Field budget = Class.forName(clubPath).getDeclaredField("budget");
			budget.setAccessible(true);
			Field teamplayers = Class.forName(clubPath).getDeclaredField("teamPlayers");
			Field indivplayers = Class.forName(clubPath).getDeclaredField("individualPlayers");

			teamplayers.setAccessible(true);
			indivplayers.setAccessible(true);
			budget.setAccessible(true);
			ArrayList<Object> teamAfterAdd = (ArrayList<Object>) teamplayers.get(createdClub);
			
			ArrayList<Object> indivAfterAdd = (ArrayList<Object>) indivplayers.get(createdClub);
			int budgetafterAdd = budget.getInt(createdClub);
			
			assertEquals(
					"The player's salary should be subtracted from budget when you add an individual player to your club",
					(originalBudget-price), budgetafterAdd);
			
			assertEquals(
					"The player should be added to teamsPlayers array in club",
					1, teamAfterAdd.size());
			assertEquals(
					"The indvidual player should not be added to individualPlayers array in club",
					0,indivAfterAdd.size());
			
			
			
		} catch (InvocationTargetException e) {
			Throwable thrownException = e.getTargetException();
			assertNull("UnExpected exception was thrown,The team player should not be added to individualPlayers array in club", thrownException);
					}

		
	

		
		
	}

	
	// ////////////////////////////////////////////////////////////////////

	@Test(timeout = 1000)
	public void testConstructorCannotSellExceptionEmpty()
			throws ClassNotFoundException {
		Class[] inputs = {};
		testConstructorExists(Class.forName(cannotSellPath), inputs);
	}

	@Test(timeout = 1000)
	public void testConstructorCannotSellExceptionWithString()
			throws ClassNotFoundException {
		Class[] inputs = { String.class };
		testConstructorExists(Class.forName(cannotSellPath), inputs);
	}

	@Test(timeout = 1000)
	public void testConstructorNotEnoughBudgetExceptionEmpty()
			throws ClassNotFoundException {
		Class[] inputs = {};
		testConstructorExists(Class.forName(notEnoughBudgetPath), inputs);
	}

	@Test(timeout = 1000)
	public void testConstructorNotEnoughBudgetExceptionWithString()
			throws ClassNotFoundException {
		Class[] inputs = { String.class };
		testConstructorExists(Class.forName(notEnoughBudgetPath), inputs);

	}

	@Test(timeout = 1000)
	public void testMethodIndividualPlayerIsDraftable() throws Exception {
		Method[] methods = Class.forName(individualPlayerPath)
				.getDeclaredMethods();
		assertTrue(
				"The method \"isDraftable\" should be declared in the IndividualPlayer class",
				containsMethodName(methods, "isDraftable"));

	}

	@Test(timeout = 1000)
	public void testMethodIndividualPlayerIsDraftableLogicPass()
			throws Exception {
		assertNotNull(Class.forName(individualPlayerPath));
		assertNotNull(Class.forName(genderPath));
		Constructor<?> constructor = Class.forName(individualPlayerPath)
				.getConstructor(String.class, Class.forName(genderPath),
						int.class, int.class,
						Class.forName(IndividualSportTypesPath), int.class);
		int numberOfMedals = (int) (Math.random() * (200 - 100 + 1)) + 100;
		;
		int randomYear = (int) (Math.random() * (2022 - 1990 + 1)) + 1990;
		int exp = 2024 - randomYear;

		boolean f = (numberOfMedals > exp) ? true : false;
		Object g = Enum
				.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
		Object ist = Enum.valueOf(
				(Class<Enum>) Class.forName(IndividualSportTypesPath),
				"GYMNASTICS");

		Object createdObject = constructor.newInstance("Messi", g, 32,
				randomYear, ist, numberOfMedals);
		Method m = createdObject.getClass().getMethod("isDraftable");
		assertEquals(
				"a player with number of medals greater than his years of experience in a club shoud be draftable , otherwise the player is not draftable",
				f, ((boolean) m.invoke(createdObject)));

	}

	@Test(timeout = 1000)
	public void testMethodIndividualPlayerIsDraftableLogicFail()
			throws Exception {
		assertNotNull(Class.forName(individualPlayerPath));
		assertNotNull(Class.forName(genderPath));
		Constructor<?> constructor = Class.forName(individualPlayerPath)
				.getConstructor(String.class, Class.forName(genderPath),
						int.class, int.class,
						Class.forName(IndividualSportTypesPath), int.class);
		int numberOfMedals = (int) (Math.random() * (200 - 100 + 1)) + 100;
		;
		int randomYear = (int) (Math.random() * (1900 - 1800 + 1)) + 1;
		int exp = 2024 - randomYear;

		boolean f = (numberOfMedals > exp) ? true : false;
		Object g = Enum
				.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
		Object ist = Enum.valueOf(
				(Class<Enum>) Class.forName(IndividualSportTypesPath),
				"GYMNASTICS");

		Object createdObject = constructor.newInstance("Messi", g, 32,
				randomYear, ist, numberOfMedals);
		Method m = createdObject.getClass().getMethod("isDraftable");
		assertEquals(
				"a player with number of medals greater than his years of experience in a club shoud be draftable , otherwise the player is not draftable",
				f, ((boolean) m.invoke(createdObject)));

	}

	// //////// test getSalary() /////////////////////////

	@Test(timeout = 1000)
	public void testMethodIndividualPlayerGetSalary() throws Exception {
		Method[] methods = Class.forName(individualPlayerPath)
				.getDeclaredMethods();
		assertTrue(
				"The method \"getSalary\" should be declared in the IndividualPlayer class",
				containsMethodName(methods, "getSalary"));

	}

	@Test(timeout = 1000)
	public void testMethodIndividualPlayerGetSalaryLogic() throws Exception {
		assertNotNull(Class.forName(individualPlayerPath));
		assertNotNull(Class.forName(genderPath));

		Constructor<?> constructor = Class.forName(individualPlayerPath)
				.getConstructor(String.class, Class.forName(genderPath),
						int.class, int.class,
						Class.forName(IndividualSportTypesPath), int.class);
		int numberOfMedals = (int) (Math.random() * (20 - 0 + 1)) + 1;
		int randomYear = (int) (Math.random() * (2022 - 1990 + 1)) + 1990;
		int exp = 2024 - randomYear;
		// boolean f = (numberOfMedals > exp) ? true : false;
		Object g = Enum
				.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
		Object ist = Enum.valueOf(
				(Class<Enum>) Class.forName(IndividualSportTypesPath),
				"GYMNASTICS");
		Object createdObject = constructor.newInstance("Messi", g, 32,
				randomYear, ist, numberOfMedals);
		Method m = createdObject.getClass().getMethod("getSalary");

		int actualSalary = numberOfMedals * exp * 50;

		assertEquals(
				"The salary of each player is caluclated based on the number of medals the player has earned multiply years of experience multiply 50",
				actualSalary, ((int) m.invoke(createdObject)));

	}

	// ///////////////////test team player //////////////////////////

	@Test(timeout = 1000)
	public void testMethodTeamPlayerPlayerIsDraftable() throws Exception {
		Method[] methods = Class.forName(teamPlayerPath).getDeclaredMethods();
		assertTrue(
				"The method \"isDraftable\" should be declared in the TeamPlayer class",
				containsMethodName(methods, "isDraftable"));

	}

	@Test(timeout = 1000)
	public void testMethodTeamPlayerIsDraftableLogicPass() throws Exception {
		assertNotNull(Class.forName(PlayerPositionPath));

		Constructor<?> constructor = Class.forName(teamPlayerPath)
				.getConstructor(String.class, Class.forName(genderPath),
						int.class, int.class, String.class,
						Class.forName(PlayerPositionPath));
		Object g = Enum
				.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
		Object ppa = Enum.valueOf(
				(Class<Enum>) Class.forName(PlayerPositionPath), "ATTACKER");
		int randomYear = (int) (Math.random() * (1900 - 1800 + 1)) + 1800;
		int exp = 2024 - randomYear;
		boolean f = (exp > 5) ? true : false;

		Object createdObject = constructor.newInstance("Messi", g, 32,
				randomYear, "Barc", ppa);

		Method m = createdObject.getClass().getMethod("isDraftable");

		assertEquals(
				"a player with years of experience in a club greater than 5 shoud be draftable , otherwise the player is not draftable",
				f, ((boolean) m.invoke(createdObject)));

	}

	@Test(timeout = 1000)
	public void testMethodTeamPlayerIsDraftableLogicFail() throws Exception {
		assertNotNull(Class.forName(PlayerPositionPath));

		Constructor<?> constructor = Class.forName(teamPlayerPath)
				.getConstructor(String.class, Class.forName(genderPath),
						int.class, int.class, String.class,
						Class.forName(PlayerPositionPath));
		Object g = Enum
				.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
		Object ppa = Enum.valueOf(
				(Class<Enum>) Class.forName(PlayerPositionPath), "ATTACKER");
		int randomYear = (int) (Math.random() * (2024 - 2022 + 1)) + 2022;
		int exp = 2024 - randomYear;
		boolean f = (exp > 5) ? true : false;

		Object createdObject = constructor.newInstance("Messi", g, 32,
				randomYear, "Barc", ppa);

		Method m = createdObject.getClass().getMethod("isDraftable");

		assertEquals(
				"a player with years of experience in a club greater than 5 shoud be draftable , otherwise the player is not draftable",
				f, ((boolean) m.invoke(createdObject)));

	}

	// //////// test getSalary() /////////////////////////

	@Test(timeout = 1000)
	public void testMethodTeamPlayerGetSalary() throws Exception {
		Method[] methods = Class.forName(teamPlayerPath).getDeclaredMethods();
		assertTrue(
				"The method \"getSalary\" should be declared in the teamPlayerPath class",
				containsMethodName(methods, "getSalary"));

	}

	@Test(timeout = 1000)
	public void testMethodTeamPlayerGetSalaryATTACKERLogic() throws Exception {
		assertNotNull(Class.forName(PlayerPositionPath));

		Constructor<?> constructor = Class.forName(teamPlayerPath)
				.getConstructor(String.class, Class.forName(genderPath),
						int.class, int.class, String.class,
						Class.forName(PlayerPositionPath));
		Object g = Enum
				.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
		Object ppa = Enum.valueOf(
				(Class<Enum>) Class.forName(PlayerPositionPath), "ATTACKER");
		int randomYear = (int) (Math.random() * (2022 - 1990 + 1)) + 1990;
		int exp = 2024 - randomYear;
		int actualSalary = exp * 1500;

		Object createdObject = constructor.newInstance("Messi", g, 32,
				randomYear, "Barc", ppa);
		Method m = createdObject.getClass().getMethod("getSalary");

		assertEquals(
				"ATTACKER salary is = 1500 multiply the years of experience",
				actualSalary, ((int) m.invoke(createdObject)));
	}

	@Test(timeout = 1000)
	public void testMethodTeamPlayerGetSalaryDEFENDERLogic() throws Exception {
		assertNotNull(Class.forName(PlayerPositionPath));

		Constructor<?> constructor = Class.forName(teamPlayerPath)
				.getConstructor(String.class, Class.forName(genderPath),
						int.class, int.class, String.class,
						Class.forName(PlayerPositionPath));
		Object g = Enum
				.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
		Object ppa = Enum.valueOf(
				(Class<Enum>) Class.forName(PlayerPositionPath), "DEFENDER");
		int randomYear = (int) (Math.random() * (2022 - 1990 + 1)) + 1990;
		int exp = 2024 - randomYear;
		int actualSalary = exp * 1000;

		Object createdObject = constructor.newInstance("Messi", g, 32,
				randomYear, "Barc", ppa);
		Method m = createdObject.getClass().getMethod("getSalary");

		assertEquals(
				"DEFENDER salary is = 1000 multiply the years of experience",
				actualSalary, ((int) m.invoke(createdObject)));
	}

	@Test(timeout = 1000)
	public void testMethodTeamPlayerGetSalaryMIDFIELDERLogic() throws Exception {
		assertNotNull(Class.forName(PlayerPositionPath));

		Constructor<?> constructor = Class.forName(teamPlayerPath)
				.getConstructor(String.class, Class.forName(genderPath),
						int.class, int.class, String.class,
						Class.forName(PlayerPositionPath));
		Object g = Enum
				.valueOf((Class<Enum>) Class.forName(genderPath), "MALE");
		Object ppa = Enum.valueOf(
				(Class<Enum>) Class.forName(PlayerPositionPath), "MIDFIELDER");
		int randomYear = (int) (Math.random() * (2022 - 1990 + 1)) + 1990;
		int exp = 2024 - randomYear;
		int actualSalary = exp * 500;

		Object createdObject = constructor.newInstance("Messi", g, 32,
				randomYear, "Barc", ppa);
		Method m = createdObject.getClass().getMethod("getSalary");

		assertEquals(
				"MIDFIELDER salary is = 500 multiply the years of experience",
				actualSalary, ((int) m.invoke(createdObject)));
	}

	// ///////////////////////////////////////////////////////////////

}
