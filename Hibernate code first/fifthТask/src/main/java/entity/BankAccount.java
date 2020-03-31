package entity;

import javax.persistence.*;

@Entity
@Table(name = "bank_accounts")
public class BankAccount extends BillingDetail{
    private String bankName;
    private String swiftCode;

    public BankAccount() {
    }

    @Column(name = "bank_name")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "swift_code")
    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

}
