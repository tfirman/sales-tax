public class fulcrumexercise {
    public static void main(String[] args) {
        basket basket1 = new basket("./src/main/resources/Input3");
        for (int i = 0; i < basket1.purchases.size(); i++) {
            Double price = Double.valueOf(basket1.purchases.get(i).getPrice()/100.);
            System.out.println(basket1.purchases.get(i).getAmount().toString() + " " + basket1.purchases.get(i).getDescription() + " : " + String.format("%.2f", price));
        }
        System.out.println("Sales Taxes : " + String.format("%.2f", Double.valueOf(basket1.getTotalTaxes()/100.)));
        System.out.println("Total : " + String.format("%.2f", Double.valueOf(basket1.getTotalPrice()/100.)));
    }
}
