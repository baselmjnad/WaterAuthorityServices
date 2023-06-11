package Classes;

import java.util.ArrayList;
import java.util.List;

import Classes.User;

public class Consumer {
    public Integer id;
    public String consumerName;
    public String consumerPhone;
    public String consumerAddress;
    public User user;
    public String consumerGender;
    public Integer consumerAge;
    public List<Subscription> subscriptions;

    public String getConsumerGender() {
        return consumerGender;
    }

    public void setConsumerGender(String consumerGender) {
        this.consumerGender = consumerGender;
    }

    public Integer getConsumerAge() {
        return consumerAge;
    }

    public void setConsumerAge(Integer consumerAge) {
        this.consumerAge = consumerAge;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerPhone() {
        return consumerPhone;
    }

    public void setConsumerPhone(String consumerPhone) {
        this.consumerPhone = consumerPhone;
    }

    public String getConsumerAddress() {
        return consumerAddress;
    }

    public void setConsumerAddress(String consumerAddress) {
        this.consumerAddress = consumerAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
