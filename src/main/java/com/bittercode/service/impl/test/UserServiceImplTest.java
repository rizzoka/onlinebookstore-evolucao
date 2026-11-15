package com.bittercode.service.impl.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.HttpSession;

import com.bittercode.constant.db.UsersDBConstants;
import com.bittercode.model.StoreException;
import com.bittercode.model.User;
import com.bittercode.model.UserRole;
import com.bittercode.service.impl.UserServiceImpl;
import com.bittercode.util.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserServiceImplTest {

    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private HttpSession mockSession;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() throws Exception {
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        mockSession = mock(HttpSession.class);

        // Subclasse de teste sobrescrevendo getConnection()
        userService = new UserServiceImpl() {
            @Override
            protected Connection getConnection() throws StoreException {
                return mockConnection;
            }
        };

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    @DisplayName("Deve autenticar usuário válido, preencher sessão e não reter senha em memória")
    void shouldAuthenticateValidCustomer() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(UsersDBConstants.COLUMN_FIRSTNAME)).thenReturn("Kauã");
        when(mockResultSet.getString(UsersDBConstants.COLUMN_LASTNAME)).thenReturn("Cunha");
        when(mockResultSet.getLong(UsersDBConstants.COLUMN_PHONE)).thenReturn(71999999999L);

        User user = userService.login(UserRole.CUSTOMER, "kaua@ucsal.br", "senha123", mockSession);

        assertNotNull(user);
        assertEquals("Kauã", user.getFirstName());
        assertEquals("kaua@ucsal.br", user.getEmailId());
        assertNull(user.getPassword(), "A senha não deve permanecer exposta no objeto de sessão");
        verify(mockSession).setAttribute(UserRole.CUSTOMER.toString(), "kaua@ucsal.br");
    }

    @Test
    @DisplayName("Deve retornar null quando as credenciais não existirem no banco")
    void shouldReturnNullWhenCredentialsAreInvalid() throws Exception {
        when(mockResultSet.next()).thenReturn(false);

        User user = userService.login(UserRole.CUSTOMER, "invalido@ucsal.br", "errada", mockSession);

        assertNull(user);
        verify(mockSession, never()).setAttribute(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar logar com email ou senha em branco")
    void shouldRejectBlankLoginCredentials() {
        assertThrows(IllegalArgumentException.class, () ->
            userService.login(UserRole.CUSTOMER, "   ", "senha123", mockSession)
        );
    }

    @Test
    @DisplayName("Deve rejeitar cadastro de usuário com email inválido ou senha curta")
    void shouldValidateUserRegistrationRules() {
        User invalidUser = new User();
        invalidUser.setEmailId("emaiSemArroba");
        invalidUser.setPassword("123");
        invalidUser.setFirstName("Teste");

        assertThrows(IllegalArgumentException.class, () ->
            UserValidator.validateRegistration(invalidUser)
        );
    }
}