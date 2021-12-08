package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
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
	private ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
	private InOrder ordered;
	@Mock
	private AuthDAO authDAO;
	@Mock
	private GenericDAO dao;
	private SystemManager manager;
	@BeforeEach
	public void initialization() {
		ordered = inOrder(authDAO, dao);
		manager = new SystemManager(authDAO, dao);
		when(authDAO.getAuthData(user.getId())).thenReturn(user);
	}
	@Test
	public void startRemoteTest() throws Exception {
		// comportamiento del mock
		when(dao.getSomeData(user, "where id=" + idValido)).thenReturn(lista);
			
		Collection<Object> retorno = manager.startRemoteSystem(user.getId(), idValido);
		assertEquals(retorno.toString(), "[uno, dos]");
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).getSomeData(user, "where id=" + idValido);
	}
	@Test
	public void addRemoteTest() throws Exception {
		// comportamiento del mock
		when(dao.updateSomeData(user, "tres")).thenReturn(true);

		manager.addRemoteSystem(user.getId(), "tres");
		
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).updateSomeData(user, "tres");
	}
	@Test
	public void stopRemoteTest() throws Exception{
		// comportamiento del mock
		when(dao.getSomeData(user, "where id= " +idValido)).thenReturn(lista);
		
		Collection<Object> retorno = manager.startRemoteSystem(user.getId(), idValido);
		System.out.print(retorno.toString());
		retorno.toString();
		assertEquals(retorno.toString(), "[uno, dos]");
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).getSomeData(user, "where id=" + idValido);
	}
	@Test
	public void deleteRemoteTest() throws Exception {
		// comportamiento del mock
		when(dao.deleteSomeData(user, idValido)).thenReturn(true);
		
		manager.deleteRemoteSystem(user.getId(), idValido);
		ordered.verify(authDAO).getAuthData(user.getId());
		ordered.verify(dao).deleteSomeData(user, idValido);
	}

}
