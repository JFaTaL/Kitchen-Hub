import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Date;

public class boughtItemsTable() {
    public Hashtable createTable(String[] product, Date[] expiration) {
        Hashtable<String, Date> filledTable = new Hashtable<>();
        for (int i = 0; i < product.length(); i++) {
            filledTable.put(product[i], expiration[i]);
        }
        return filledTable;
    }

    

    private void sortTable() {

    }
}