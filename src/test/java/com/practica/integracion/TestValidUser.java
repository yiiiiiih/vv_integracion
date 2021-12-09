package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;

@ExtendWith(MockitoExtension.class)
public class TestValidUser {

	private User user = new User("1", "Antonio", "Perez", "Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
	private String idValido = "12345";
	private String idInvalido= null;
	private ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
	private InOrder ordered;
	
	AuthDAO authDAO = mock(AuthDAO.class);
	
	GenericDAO dao = mock(GenericDAO.class);

	private SystemManager manager;
	@BeforeEach
	public void initialization() {
		ordered = inOrder(authDAO, dao);
		manager = new SystemManager(authDAO, dao);
		when(authDAO.getAuthData(user.getId())).thenReturn(user);
	}
	@Test
	public void testStartRemoteSystemWithValidUserAndSystem() throws Exception {
		when(dao.getSomeData(user, "where id=" + idValido)).thenReturn(lista);
			
		Collection<Object> retorno = manager.startRemoteSystem(user.getId(), idValido);
		assertEquals(retorno.toString(), "[uno, dos]");
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).getSomeData(user, "where id=" + idValido);
	}
	@Test
	public void testStartRemoteSystemWithValidUserInvalidSystem() throws Exception {
		when(dao.getSomeData(user, "where id=" + idInvalido)).thenReturn(null);
			
		Collection<Object> retorno = manager.startRemoteSystem(user.getId(), idInvalido);
		assertEquals(retorno, null);
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).getSomeData(user, "where id=" + idInvalido);
	}
	@Test
	public void testAddRemoteSystemWithValidUserAndSystem() throws Exception {
		when(dao.updateSomeData(user, "tres")).thenReturn(true);

		manager.addRemoteSystem(user.getId(), "tres");
		
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).updateSomeData(user, "tres");
	}
	@Test
	public void testAddRemoteSystemWithValidUserInvalidSystem() throws Exception {
		when(dao.updateSomeData(user, null)).thenReturn(false);

		assertThrows(SystemManagerException.class, () -> {manager.addRemoteSystem(user.getId(), null);});

		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).updateSomeData(user, null);
	}
	@Test
	public void testStopRemoteSystemWithValidUserAndSystem() throws Exception{
		when(dao.getSomeData(user, "where id=" +idValido)).thenReturn(lista);
		
		Collection<Object> retorno = manager.startRemoteSystem(user.getId(), idValido);
		assertEquals(retorno.toString(), "[uno, dos]");
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).getSomeData(user, "where id=" + idValido);
	}
	@Test
	public void testStopRemoteSystemWithValidUserInvalidSystem() throws Exception{
		when(dao.getSomeData(user, "where id=" +idInvalido)).thenReturn(null);
		
		Collection<Object> retorno = manager.startRemoteSystem(user.getId(), idInvalido);
		assertEquals(retorno,null);
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).getSomeData(user, "where id=" + idInvalido);
	}
	
	@Test
	public void testDeleteRemoteSystemValidUserAndSystem() throws Exception {

		when(dao.deleteSomeData(user, idValido)).thenReturn(true);
		
		manager.deleteRemoteSystem(user.getId(), idValido);
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).deleteSomeData(user, idValido);
	}
	@Test
	public void testDeleteRemoteSystemValidUserInvalidSystem() throws Exception {

		when(dao.deleteSomeData(user, idInvalido)).thenReturn(false);
		
		assertThrows(SystemManagerException.class, () -> {manager.deleteRemoteSystem(user.getId(), idInvalido);});
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).deleteSomeData(user, idInvalido);
	}


}
