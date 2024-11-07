import java.io.Serializable;

public class CryptoObject implements Serializable {
    private String headline;
    private String content;
    private String topicName;
    private double price;
    private String cryptoName;

    // Constructor to initialize all the fields
    public CryptoObject(String headline, String content, String topicName, double price, String cryptoName) {
        this.headline = headline;
        this.content = content;
        this.topicName = topicName;
        this.price = price;
        this.cryptoName = cryptoName;
    }

    // Getters for each field
    public String getHeadline() {
        return headline;
    }

    public String getContent() {
        return content;
    }

    public String getTopicName() {
        return topicName;
    }

    public double getPrice() {
        return price;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    // Setters for each field
    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    // toString method to return a string representation of the CryptoObject
    @Override
    public String toString() {
        return "Topic: " + topicName + "\n" +
                "Headline: " + headline + "\n" +
                "Content: " + content + "\n" +
                "Crypto: " + cryptoName + "\n" +
                "Price: " + price;
    }
}
