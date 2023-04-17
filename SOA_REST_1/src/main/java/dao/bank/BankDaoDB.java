package dao.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.bank.BankAccount;

public enum BankDaoDB {
	instance;

	Connection con;
	Statement stmt;
	

	private BankDaoDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/SOA_REST?useTimezone=true&serverTimezone=UTC";
			con = DriverManager.getConnection(url, "root", "");
			stmt = con.createStatement();
		} catch (Exception e) {
			System.out.println("Error: failed to connect to the database\n" + e.getMessage());
		}
	}
	
	public int getNextAccountNumber(String branchCode) {
		String query = "select MAX(account_number) as 'account_number' from bankaccount where branch_code = ?;";
		
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, branchCode);
			ResultSet rs = pstmt.executeQuery();
			int maxAccountNumber = 0;
			while (rs.next()) {
				maxAccountNumber = rs.getInt("account_number");
			}
			
			//increment to produce next account number
			maxAccountNumber++;
			return maxAccountNumber;
		} catch (Exception e) {
			System.out.println("Error getting a next account number\n" + e.getMessage());
			return -1;
		}
	}
	
	public int deleteAllAccounts() {
		/*	return -1 if SQL error (return -1 from the catch block).
		return 1 if something was deleted.
		return 0 if nothing was deleted (resource does not exist).*/
		
		
		String deleteQuery = "delete from bankaccount;";
		
		// count how many records
		if (countBankAcounts() > 0) {
			try {
				PreparedStatement pstmt = con.prepareStatement(deleteQuery);
				int row = pstmt.executeUpdate();
				return 1;
			} catch (Exception e) {
				System.out.println("Error deleting bank accounts\n" + e.getMessage());
				return -1;
			}
		}
		else {
			return 0;
		}
		
	}
	
	public int countBankAcounts() {
		String countQuery = "select count(*) as count from bankaccount;";

		int numOfAccounts = 0;
		try {
			PreparedStatement pstmt = con.prepareStatement(countQuery);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				numOfAccounts = rs.getInt("count");
			}	
		} catch (Exception e) {
			System.out.println("Error counting the number of bank accounts\n" + e.getMessage());
		}
		
		return numOfAccounts;
	}
	
	public int addBankAccount(BankAccount bankAccount) {
		// 	return -1 if unsuccessful, 1 if successful.
		try {
			PreparedStatement preparedStatement = con
					.prepareStatement("insert into bankaccount values(?,?,?,?,?, ?);");
			// insert into bankaccount values("ATH001", 123, "Billy Smith", "Willow Park", 70, 1000);
			preparedStatement.setString(1, bankAccount.getBranchCode());
			preparedStatement.setInt(2, bankAccount.getAccountNumber());
			preparedStatement.setString(3, bankAccount.getCustName());
			preparedStatement.setString(4, bankAccount.getCustAddress());
			preparedStatement.setInt(5, bankAccount.getCustRating());
			preparedStatement.setDouble(6, bankAccount.getBalance());
			int row = preparedStatement.executeUpdate();
			return 1;
		} catch (Exception e) {
			System.out.println("Error creating a bank account\n" + e.getMessage());
			return -1;
		}
	}
	
	public int updateBankAccount(BankAccount bankAccount) {
		// 	return -1 if unsuccessful, 1 if successful.
		try {
			PreparedStatement preparedStatement = con
					.prepareStatement("UPDATE bankaccount \r\n"
							+ "SET \r\n"
							+ "    cust_name = ?,\r\n"
							+ "    cust_address = ?,\r\n"
							+ "    cust_rating = ?,\r\n"
							+ "    balance = ?\r\n"
							+ "WHERE\r\n"
							+ "    branch_code = ? and account_number = ?;");
			preparedStatement.setString(1, bankAccount.getCustName());
			preparedStatement.setString(2, bankAccount.getCustAddress());
			preparedStatement.setInt(3, bankAccount.getCustRating());
			preparedStatement.setDouble(4, bankAccount.getBalance());
			preparedStatement.setString(5, bankAccount.getBranchCode());
			preparedStatement.setInt(6, bankAccount.getAccountNumber());
			int row = preparedStatement.executeUpdate();
			return 1;
		} catch (Exception e) {
			System.out.println("Error updating bank account\n" + e.getMessage());
			return -1;
		}
	}
	
	public BankAccount getAccountDetails(String branchCode, String accountNo) {
	    String query = "select * from bankaccount WHERE branch_code = ? and account_number = ?;";

	    try {
	        PreparedStatement pstmt = con.prepareStatement(query);
	        pstmt.setString(1, branchCode);
	        pstmt.setInt(2, Integer.parseInt(accountNo));
	        ResultSet rs = pstmt.executeQuery();

	        BankAccount bankAccount = new BankAccount();
	        while (rs.next()) {
	            bankAccount.setBranchCode(branchCode); // Add this line
	            bankAccount.setAccountNumber(Integer.parseInt(accountNo)); // Add this line
	            bankAccount.setCustName(rs.getString("cust_name"));
	            bankAccount.setCustAddress(rs.getString("cust_address"));
	            bankAccount.setBalance(rs.getFloat("balance"));
	            bankAccount.setCustRating(rs.getInt("cust_rating"));
	        }

	        return bankAccount;
	    } catch (Exception e) {
	        System.out.println("Error getting account details\n" + e.getMessage());
	        return null;
	    }
	}

	
	public ArrayList<BankAccount> getAllAccounts() {
		ArrayList<BankAccount> bankAccounts = new ArrayList<BankAccount>();
		String query = "select * from bankaccount;";
		
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BankAccount bankAccount = new BankAccount();
				bankAccount.setAccountNumber(rs.getInt("account_number"));
				bankAccount.setBranchCode(rs.getString("branch_code"));
				bankAccount.setCustName(rs.getString("cust_name"));
				bankAccount.setCustAddress(rs.getString("cust_address"));
				bankAccount.setBalance(rs.getFloat("balance"));
				bankAccount.setCustRating(rs.getInt("cust_rating"));
				bankAccounts.add(bankAccount);
			}
			
		} catch (Exception e) {
			System.out.println("Error getting a list of bank account\n" + e.getMessage());
		}
		
		return bankAccounts;
	}

	public int deleteBankAccount(String branchCode, String accountNo) {
		/*return -1 if SQL error (return -1 from the catch block).
			return 1 if something was deleted.
			return 0 if nothing was deleted (resource does not exist).*/
		
		// check if resource exists
		if ( getAccountDetails( branchCode, accountNo) != null ) {
			String query = "delete from bankaccount WHERE branch_code = ? and account_number = ?;";

			try {
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.setString(1, branchCode);
				pstmt.setInt(2, Integer.parseInt(accountNo));
				int rs = pstmt.executeUpdate();
				return 1;
				
			} catch (Exception e) {
				System.out.println("Error deleting a bank account\n" + e.getMessage());
				return -1;
			}
		}
		else {
			return 0;
		}
		
		
	}
	
//	public BankAccount getBankAccount(String branchCode, String accountNumber) {
//	    BankAccount bankAccount = null;
//	    try {
//	        String query = "SELECT * FROM bankaccount WHERE branch_code = ? AND account_number = ?";
//	        PreparedStatement pstmt = con.prepareStatement(query);
//	        pstmt.setString(1, branchCode);
//	        pstmt.setString(2, accountNumber);
//	        ResultSet rs = pstmt.executeQuery();
//
//	        if (rs.next()) {
//	            String custName = rs.getString("cust_name");
//	            String custAddress = rs.getString("cust_address");
//	            int custRating = rs.getInt("cust_rating");
//	            float balance = rs.getFloat("balance");
//
//	            bankAccount = new BankAccount(branchCode, Integer.parseInt(accountNumber), custName, custAddress, custRating, balance);
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//
//	    return bankAccount;
//	}


}
