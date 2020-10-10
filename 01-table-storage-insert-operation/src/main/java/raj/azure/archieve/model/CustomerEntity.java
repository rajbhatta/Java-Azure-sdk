package raj.azure.archieve.model;

import com.microsoft.azure.storage.table.TableServiceEntity;

public class CustomerEntity extends TableServiceEntity {
    private String email;
    private String phoneNumber;

    public CustomerEntity() {
    }

    public CustomerEntity(String firstName, String lastName) {
        this.partitionKey = firstName;
        this.rowKey = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
