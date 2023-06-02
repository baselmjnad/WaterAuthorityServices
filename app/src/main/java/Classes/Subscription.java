package Classes;

public class Subscription {
    public Integer id;
    public String consumerBarCode;
    public String consumerSubscriptionNo;
    public String subscriptionUsingType;
    public String subscriptionStatus;
    public String subscriptionAddress;
    public Consumer consumer;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConsumerBarCode() {
        return consumerBarCode;
    }

    public void setConsumerBarCode(String consumerBarCode) {
        this.consumerBarCode = consumerBarCode;
    }

    public String getConsumerSubscriptionNo() {
        return consumerSubscriptionNo;
    }

    public void setConsumerSubscriptionNo(String consumerSubscriptionNo) {
        this.consumerSubscriptionNo = consumerSubscriptionNo;
    }

    public String getSubscriptionUsingType() {
        return subscriptionUsingType;
    }

    public void setSubscriptionUsingType(String subscriptionUsingType) {
        this.subscriptionUsingType = subscriptionUsingType;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getSubscriptionAddress() {
        return subscriptionAddress;
    }

    public void setSubscriptionAddress(String subscriptionAddress) {
        this.subscriptionAddress = subscriptionAddress;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }



}
