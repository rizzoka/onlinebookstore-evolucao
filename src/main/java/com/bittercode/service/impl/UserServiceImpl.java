package com.bittercode.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.bittercode.constant.ResponseCode;
import com.bittercode.constant.db.UsersDBConstants;
import com.bittercode.model.StoreException;
import com.bittercode.model.User;
import com.bittercode.model.UserRole;
import com.bittercode.service.UserService;
import com.bittercode.util.DBUtil;
import com.bittercode.util.UserValidator;

public class UserServiceImpl implements UserService {

    // constantes para identificar o tipo de usuário no banco de dados para que possamos diferenciar entre vendedores e clientes
    private static final int SELLER_ROLE_ID = 1;
    private static final int CUSTOMER_ROLE_ID = 2;

    private static final String REGISTER_QUERY = "INSERT INTO " + UsersDBConstants.TABLE_USERS
            + "  VALUES(?,?,?,?,?,?,?,?)";

    private static final String LOGIN_QUERY = "SELECT * FROM " + UsersDBConstants.TABLE_USERS + " WHERE "
            + UsersDBConstants.COLUMN_USERNAME + "=? AND "
            + UsersDBConstants.COLUMN_PASSWORD + "=? AND "
            + UsersDBConstants.COLUMN_USERTYPE + "=?";

    // essa conecction permite testes unitários com mockito sem depender totalmente do MySQL
    protected Connection getConnection() throws StoreException {
        return DBUtil.getConnection();
    }


    @Override
    public User login(UserRole role, String email, String password, HttpSession session) throws StoreException {
        UserValidator.validateLoginCredentials(email, password);
        Connection con = getConnection();

        //fechando o preparedStatement e resultSet automaticamente para evitar vazamento de recursos
        try (PreparedStatement ps = con.prepareStatement(LOGIN_QUERY)){
            ps.setString(1, email.trim());
            ps.setString(2, password);
            ps.setInt(3, resolveUserType(role));

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    User user = mapResultSetToUser(rs, email.trim());
                    if (session != null) {
                        session.setAttribute(role.toString(), user.getEmailId());
                    }
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new StoreException("Erro ao fazer login: " + e.getMessage());
        }
        return null;
    }


    @Override
    public boolean isLoggedIn(UserRole role, HttpSession session) {
        if (role == null){
            role = UserRole.CUSTOMER;
        }
        return session != null && session.getAttribute(role.toString()) != null;
    }


    @Override
    public boolean logout(HttpSession session) {
        if (session != null){
            session.removeAttribute(UserRole.CUSTOMER.toString());
            session.removeAttribute(UserRole.SELLER.toString());
            session.invalidate();
        }
        return true;
    }


    @Override
    public String register(UserRole role, User user) throws StoreException {
        UserValidator.validateRegistration(user);
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(REGISTER_QUERY);
            ps.setString(1, user.getEmailId().trim());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName().trim());
            ps.setString(4, user.getLastName() != null ? user.getLastName().trim() : "");
            ps.setString(5, user.getAddress() != null ? user.getAddress().trim() : "");
            ps.setLong(6, user.getPhone());
            ps.setString(7, user.getEmailId().trim());
            ps.setInt(8, resolveUserType(role));

            int responseCode = ps.executeUpdate();
            if (responseCode == 1) {
                return ResponseCode.SUCCESS.name();
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate")){
                return "Usuário já registrado com esse email!!";
            }

            throw new StoreException("Erro ao registrar usuário." + e.getMessage());
        }
        return ResponseCode.FAILURE.name();
    }

    private int resolveUserType(UserRole role) {
        return UserRole.SELLER.equals(role) ? SELLER_ROLE_ID : CUSTOMER_ROLE_ID;
    }


    private User mapResultSetToUser(ResultSet rs, String email) throws SQLException {
        User user = new User();
        user.setFirstName(rs.getString(UsersDBConstants.COLUMN_FIRSTNAME));
        user.setLastName(rs.getString(UsersDBConstants.COLUMN_LASTNAME));
        user.setPhone(rs.getLong(UsersDBConstants.COLUMN_PHONE));
        user.setEmailId(email);
        user.setAddress(rs.getString(UsersDBConstants.COLUMN_ADDRESS));
        return user;
    }
}
