package com.bittercode.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bittercode.model.StoreException;

@ExtendWith(MockitoExtension.class)
class DBUtilTest {

    @Mock
    private Connection mockConnection;

    @Test
    @DisplayName("Deve retornar uma Connection válida quando o DriverManager conectar com sucesso")
    void getConnection_QuandoSucesso_DeveRetornarConexao() throws Exception {
        try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
            driverManagerMock
                    .when(() -> DriverManager.getConnection(
                            DatabaseConfig.CONNECTION_STRING,
                            DatabaseConfig.DB_USER_NAME,
                            DatabaseConfig.DB_PASSWORD))
                    .thenReturn(mockConnection);

            Connection result = DBUtil.getConnection();

            assertNotNull(result);
            assertSame(mockConnection, result);
            driverManagerMock.verify(
                    () -> DriverManager.getConnection(
                            DatabaseConfig.CONNECTION_STRING,
                            DatabaseConfig.DB_USER_NAME,
                            DatabaseConfig.DB_PASSWORD),
                    times(1));
        }
    }

    @Test
    @DisplayName("Deve abrir uma nova Connection a cada chamada em vez de reutilizar instância estática")
    void getConnection_QuandoChamadoMultiplasVezes_DeveRetornarNovasConexoes() throws Exception {
        Connection primeiraConexao = mock(Connection.class);
        Connection segundaConexao = mock(Connection.class);

        try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
            driverManagerMock
                    .when(() -> DriverManager.getConnection(
                            DatabaseConfig.CONNECTION_STRING,
                            DatabaseConfig.DB_USER_NAME,
                            DatabaseConfig.DB_PASSWORD))
                    .thenReturn(primeiraConexao, segundaConexao);

            Connection resultado1 = DBUtil.getConnection();
            Connection resultado2 = DBUtil.getConnection();

            assertNotSame(resultado1, resultado2);
            driverManagerMock.verify(
                    () -> DriverManager.getConnection(
                            DatabaseConfig.CONNECTION_STRING,
                            DatabaseConfig.DB_USER_NAME,
                            DatabaseConfig.DB_PASSWORD),
                    times(2));
        }
    }

    @Test
    @DisplayName("Deve lançar StoreException quando o DriverManager falhar com SQLException")
    void getConnection_QuandoFalhaSQL_DeveLancarStoreException() {
        SQLException sqlException = new SQLException("Falha simulada de conexão");

        try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
            driverManagerMock
                    .when(() -> DriverManager.getConnection(
                            DatabaseConfig.CONNECTION_STRING,
                            DatabaseConfig.DB_USER_NAME,
                            DatabaseConfig.DB_PASSWORD))
                    .thenThrow(sqlException);

            StoreException exception = assertThrows(StoreException.class, DBUtil::getConnection);
            assertNotNull(exception);
        }
    }
}
