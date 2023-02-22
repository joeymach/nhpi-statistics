package database;
import database.ConnectDatabase;

public class Main {
    public static void main(String[] args) throws Exception {
        ConnectDatabase dao = new ConnectDatabase();
        dao.establishConnection();
    }

}