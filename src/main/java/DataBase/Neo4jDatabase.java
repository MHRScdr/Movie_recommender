package DataBase;

import org.neo4j.driver.*;

public class Neo4jDatabase {
    private final Driver driver;

    public Neo4jDatabase(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public void close() {
        driver.close();
    }

    public void runQuery(String query) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run(query);
                return null;
            });
        }
    }
}