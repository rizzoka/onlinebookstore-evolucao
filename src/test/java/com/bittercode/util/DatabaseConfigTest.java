package com.bittercode.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bittercode.model.StoreException;

class DatabaseConfigTest {

    @Test
    @DisplayName("Deve carregar todas as propriedades do application.properties e montar a CONNECTION_STRING")
    void deveCarregarPropriedadesEConnectionString() {
        assertNotNull(DatabaseConfig.DRIVER_NAME);
        assertFalse(DatabaseConfig.DRIVER_NAME.isBlank());

        assertNotNull(DatabaseConfig.DB_HOST);
        assertNotNull(DatabaseConfig.DB_PORT);
        assertNotNull(DatabaseConfig.DB_NAME);
        assertNotNull(DatabaseConfig.DB_USER_NAME);
        assertNotNull(DatabaseConfig.DB_PASSWORD);

        String expected = DatabaseConfig.DB_HOST + ":" + DatabaseConfig.DB_PORT + "/" + DatabaseConfig.DB_NAME;
        assertEquals(expected, DatabaseConfig.CONNECTION_STRING);
    }

    @Test
    @DisplayName("Deve ser uma classe utilitária final com construtor privado")
    void deveSerClasseUtilitariaBlindada() throws NoSuchMethodException {
        assertTrue(Modifier.isFinal(DatabaseConfig.class.getModifiers()),
                "A classe utilitária DatabaseConfig deve ser final");

        Constructor<DatabaseConfig> constructor = DatabaseConfig.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()),
                "O construtor de DatabaseConfig deve ser privado");
    }

    @Test
    @DisplayName("Não deve expor o objeto Properties mutável fora da própria classe")
    void naoDeveExporObjetoPropertiesMutavel() {
        for (Field field : DatabaseConfig.class.getDeclaredFields()) {
            if (Properties.class.isAssignableFrom(field.getType())) {
                int modifiers = field.getModifiers();
                assertTrue(Modifier.isPrivate(modifiers) && Modifier.isFinal(modifiers),
                        "O campo Properties deve ser private final para evitar alteração externa");
            }
        }
    }

    @Test
    @DisplayName("Deve priorizar variável de ambiente quando preenchida e usar fallback do properties quando ausente")
    void devePriorizarVariavelDeAmbienteSobreProperties() throws Exception {
        Method resolveMethod;
        try {
            resolveMethod = DatabaseConfig.class.getDeclaredMethod("resolveValue", String.class, String.class);
        } catch (NoSuchMethodException e) {
            fail("Suporte a variáveis de ambiente (resolveValue) não implementado na classe original");
            return;
        }

        String comVariavelAmbiente = (String) resolveMethod.invoke(null, "db.host", "jdbc:mysql://mysql-docker");
        String comNulo = (String) resolveMethod.invoke(null, "db.host", null);
        String comEspacos = (String) resolveMethod.invoke(null, "db.host", "   ");

        assertEquals("jdbc:mysql://mysql-docker", comVariavelAmbiente);
        assertEquals(DatabaseConfig.DB_HOST, comNulo);
        assertEquals(DatabaseConfig.DB_HOST, comEspacos);
    }

    @Test
    @DisplayName("Deve lançar StoreException quando application.properties estiver ausente ou corrompido")
    void deveLancarExcecaoControladaQuandoPropertiesAusenteOuCorrompido() throws Exception {
        Method loadMethod;
        try {
            loadMethod = DatabaseConfig.class.getDeclaredMethod("loadProperties", InputStream.class);
        } catch (NoSuchMethodException e) {
            fail("Carregamento seguro com tratamento de InputStream (loadProperties) não implementado na classe original");
            return;
        }

        InvocationTargetException erroNulo = assertThrows(InvocationTargetException.class,
                () -> loadMethod.invoke(null, (InputStream) null));
        assertInstanceOf(StoreException.class, erroNulo.getCause());

        InputStream streamCorrompido = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Falha simulada de leitura do arquivo");
            }
        };

        InvocationTargetException erroIO = assertThrows(InvocationTargetException.class,
                () -> loadMethod.invoke(null, streamCorrompido));
        assertInstanceOf(StoreException.class, erroIO.getCause());
    }
}
