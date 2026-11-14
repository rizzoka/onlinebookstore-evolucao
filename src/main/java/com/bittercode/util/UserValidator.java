package com.bittercode.util;

import com.bittercode.model.User;

// classe de validação de usuário para verificar os dados do usuário durante o registro
public class UserValidator {
    private UserValidator(){

    }

    public static void validateRegistration(User user){
        if (user == null){
            throw new IllegalArgumentException("Dados de usuário não podem ser nulos.");
        }
        if (isBlank(user.getEmailId()) || !user.getEmailId().contains("@")){
            throw new IllegalArgumentException("Email inválido.");
        }
        if (isBlank(user.getPassword()) || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("A senha tem que possuir pelo menos 6 caracteres.");
        }
        if (isBlank(user.getFirstName())) {
            throw new IllegalArgumentException("O primeiro nome é obrigatório.");
        }
        //convertemos o telefone para string para verificar se contém apenas dígitos e se tem entre 8 e 15 caracteres
        if (isBlank(user.getPhone().toString()) || !user.getPhone().toString().matches("\\d{8,15}")) {
            throw new IllegalArgumentException("O telefone deve conter entre 8 e 15 dígitos numéricos.");
        }
    }

    // método para validar as credenciais de login do usuário
    public static void validateLoginCredentials(String username, String password) {
        if(isBlank(username) || isBlank(password)){
            throw new IllegalArgumentException("Nome de usuário e senha são obrigatórios.");
        }
    }

    // método pra verificar se uma string é nula ou vazia
    private static boolean isBlank(String value){
        return value == null || value.trim().isEmpty();
    }
}
