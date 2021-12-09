package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.naming.OperationNotSupportedException;

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
public class TestInvalidUser {

	private User user = new User("1", "Antonio", "Perez", "Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
	private String idValido = "12345"; 
	private ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
	private InOrder ordered;
	
	AuthDAO authDAO = mock(AuthDAO.class);
	
	GenericDAO dao = mock(GenericDAO.class);

	private SystemManager manager;
	@BeforeEach
	public void initialization() {
		ordered = inOrder(authDAO, dao);
		manager = new SystemManager(authDAO, dao);
		when(authDAO.getAuthData(user.getId())).thenReturn(null);
	}
	@Test
	public void testStartRemoteSystemWithInvalidUserAndSystem() throws Exception {
		when(dao.getSomeData(user, "where id=" + idInvalido)).thenReturn(lista);
			
		Collection<Object> retorno = manager.startRemoteSystem(user.getId(), idInvalido);
		assertEquals(retorno.toString(), "[uno, dos]");
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).getSomeData(user, "where id=" + idInvalido);
	}
	@Test
	public void addRemoteTest() throws Exception {
		when(dao.updateSomeData(null, "tres")).thenThrow(new OperationNotSupportedException("Usuario no autorizado"));

		assertThrows(SystemManagerException.class, () -> {manager.addRemoteSystem(user.getId(), "tres");});

		
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).updateSomeData(null, "tres");
	}
	@Test
	public void stopRemoteTest() throws Exception{
		when(dao.getSomeData(null, "where id=" +idValido)).thenThrow(new OperationNotSupportedException("Usuario no autorizado"));

		assertThrows(SystemManagerException.class, () -> {manager.startRemoteSystem(user.getId(), idValido);});
		
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).getSomeData(null, "where id=" + idValido);
	}
	@Test
	public void deleteRemoteTest() throws Exception {

		when(dao.deleteSomeData(null, idValido)).thenThrow(new OperationNotSupportedException("Usuario no autorizado"));

		assertThrows(SystemManagerException.class, () -> {manager.deleteRemoteSystem(user.getId(), idValido);});

		
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).deleteSomeData(null, idValido);
	}

}
