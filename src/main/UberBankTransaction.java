package main;

import java.util.*;

class Transaction {
    String payee;
    String payer;
    int amount;

    public Transaction(String payee, String payer, int amount) {
        this.payee = payee;
        this.payer = payer;
        this.amount = amount;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
public class UberBankTransaction {

    public static void main(String [] args){
        List<Transaction> transactions = new ArrayList();

        transactions.add( new Transaction("ING","Bunq",300));
        transactions.add( new Transaction("ING","Bunq",100));
        transactions.add( new Transaction("ING","ABNAMRO",700));
        transactions.add( new Transaction("Bunq","ING",900));
        transactions.add( new Transaction("ABNAMRO","Bunq",120));

        Map<String,Transaction> map = getBalance(transactions );
        map.forEach((key,transaction)-> System.out.println("(" + transaction.payee + "," +  transaction.payer + ") -> " + transaction.amount ));
    }

    public  static Map<String,Transaction> getBalance(List<Transaction> Transactions ){
        Map<String,Transaction> map = new HashMap<>();
        Transactions.stream().forEach(transaction -> {
            Optional<String> optionalKey = map.keySet().stream().filter(x->x.contains(transaction.payee) && x.contains(transaction.payer)).findFirst();
            if(optionalKey.isPresent()){
                String key = optionalKey.get();
                if(map.get(key).payee.contains(transaction.payee)&&map.get(key).payer.contains(transaction.payer)){
                    map.get(key).setAmount( map.get(key).getAmount()+transaction.getAmount());
                }else if(map.get(key).payee.contains(transaction.payer)&&map.get(key).payer.contains(transaction.payee)){
                    map.get(key).setAmount( map.get(key).getAmount()-transaction.getAmount());
                }
            }else{
                map.put(transaction.payee+transaction.payer,transaction);
            }
        });
        return map;
    }

}
