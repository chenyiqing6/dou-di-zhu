package Poker01;

public class CardGroup {
    public final int num;  //牌的种类
    public int sum;  //每种牌的张数

    public CardGroup(int num, int sum) {
        this.num = num;
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "CardGroup{" +
                "num=" + num +
                ", sum=" + sum +
                '}';
    }
}
