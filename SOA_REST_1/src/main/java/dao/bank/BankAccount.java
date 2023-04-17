package dao.bank;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import resource.Link;

@XmlRootElement(name = "bankAccount")
@XmlType(propOrder = {"branchCode", "accountNumber", "custName", "custAddress", "custRating", "balance", "link"})
public class BankAccount {

    private String branchCode;
    private int accountNumber;
    private String custName;
    private String custAddress;
    private int custRating;
    private float balance;
    
    //HATEOAS
    private List<Link> link;
    
    public List<Link> getLink() {
    	return link;
    }
    
    public void setLink(List<Link> link) {
    	this.link = link;
    }
    
    public BankAccount() {
    }

    public BankAccount(String branchCode, int accountNumber, String custName, String custAddress, int custRating, float balance) {
        this.branchCode = branchCode;
        this.accountNumber = accountNumber;
        this.custName = custName;
        this.custAddress = custAddress;
        this.custRating = custRating;
        this.balance = balance;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public int getCustRating() {
        return custRating;
    }

    public void setCustRating(int custRating) {
        this.custRating = custRating;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
