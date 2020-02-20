package de.bbq.haeckel;

public class Main {
    public static void main(String[] args) throws Exception {
        MySqlAccess dao = new MySqlAccess();
        dao.readDataBase();
    }

}